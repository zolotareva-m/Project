package modelviewer.ui;
import modelviewer.scene.ModelScene;
import modelviewer.scene.SceneModel;
import math.Vector3f;
import model.Model;
import model.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
public class ModelViewerPanel extends JPanel {
    private final ModelScene scene;
    private boolean wireframeMode;
    private boolean showNormals;

    public ModelViewerPanel(ModelScene scene) {
        this.scene = scene;
        this.wireframeMode = false;
        this.showNormals = false;

        setBackground(new Color(240, 240, 240));

        // Обработка мыши для выделения
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    public void setWireframeMode(boolean wireframeMode) {
        this.wireframeMode = wireframeMode;
        repaint();
    }

    public void setShowNormals(boolean showNormals) {
        this.showNormals = showNormals;
        repaint();
    }

    private void handleMouseClick(java.awt.event.MouseEvent e) {
        SceneModel selectedModel = scene.getSelectedModel();
        if (selectedModel != null && e.isControlDown()) {
            // Здесь можно реализовать логику выделения вершин/полигонов
            // по клику мыши (требует проекции 3D в 2D)
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Включение сглаживания
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Центрирование
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Отрисовка всех моделей
        List<SceneModel> models = scene.getModels();
        for (SceneModel sceneModel : models) {
            drawModel(g2d, sceneModel, centerX, centerY);
        }

        // Отрисовка информации
        drawInfo(g2d);
    }

    private void drawModel(Graphics2D g2d, SceneModel sceneModel, int centerX, int centerY) {
        Model model = sceneModel.getModel();
        boolean isSelected = scene.getSelectedModels().contains(sceneModel);

        // Применение трансформаций
        g2d.translate(centerX + sceneModel.getTranslateX(), centerY + sceneModel.getTranslateY());
        g2d.scale(sceneModel.getScale(), sceneModel.getScale());

        // Цвет в зависимости от выделения
        if (isSelected) {
            g2d.setColor(new Color(0, 120, 215));
        } else {
            g2d.setColor(new Color(100, 100, 100));
        }

        // Отрисовка полигонов
        if (wireframeMode) {
            g2d.setStroke(new BasicStroke(1));
            for (Polygon polygon : model.polygons) {
                List<Integer> vertexIndices = polygon.getVertexIndices();
                int[] xPoints = new int[vertexIndices.size()];
                int[] yPoints = new int[vertexIndices.size()];

                for (int i = 0; i < vertexIndices.size(); i++) {
                    Vector3f vertex = model.vertices.get(vertexIndices.get(i));
                    xPoints[i] = (int) vertex.X();
                    yPoints[i] = (int) vertex.Y();
                }

                g2d.drawPolygon(xPoints, yPoints, vertexIndices.size());
            }
        } else {
            for (Polygon polygon : model.polygons) {
                List<Integer> vertexIndices = polygon.getVertexIndices();
                int[] xPoints = new int[vertexIndices.size()];
                int[] yPoints = new int[vertexIndices.size()];

                for (int i = 0; i < vertexIndices.size(); i++) {
                    Vector3f vertex = model.vertices.get(vertexIndices.get(i));
                    xPoints[i] = (int) vertex.X();
                    yPoints[i] = (int) vertex.Y();
                }

                g2d.fillPolygon(xPoints, yPoints, vertexIndices.size());
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(xPoints, yPoints, vertexIndices.size());
            }
        }

        // Отрисовка нормалей
        if (showNormals && model.normals.size() > 0) {
            g2d.setColor(Color.RED);
            for (int i = 0; i < Math.min(model.vertices.size(), model.normals.size()); i++) {
                Vector3f vertex = model.vertices.get(i);
                Vector3f normal = model.normals.get(i);

                int x1 = (int) vertex.X();
                int y1 = (int) vertex.Y();
                int x2 = (int) (vertex.X() + normal.X() * 10);
                int y2 = (int) (vertex.Y() + normal.Y() * 10);

                g2d.drawLine(x1, y1, x2, y2);
                g2d.fillOval(x2 - 2, y2 - 2, 4, 4);
            }
        }

        // Отрисовка выбранных вершин
        g2d.setColor(Color.YELLOW);
        for (int vertexIndex : sceneModel.getSelectedVertices()) {
            if (vertexIndex >= 0 && vertexIndex < model.vertices.size()) {
                Vector3f vertex = model.vertices.get(vertexIndex);
                g2d.fillOval((int) vertex.X() - 3, (int) vertex.Y() - 3, 6, 6);
            }
        }

        // Сброс трансформаций
        g2d.setTransform(new AffineTransform());
    }

    private void drawInfo(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        String info = "Управление: Ctrl+Клик - выделение, Del - удаление выделенного";
        g2d.drawString(info, 10, getHeight() - 20);

        SceneModel selectedModel = scene.getSelectedModel();
        if (selectedModel != null) {
            String selectionInfo = String.format("Выделено: %d вершин, %d полигонов",
                    selectedModel.getSelectedVertices().size(),
                    selectedModel.getSelectedPolygons().size());
            g2d.drawString(selectionInfo, 10, getHeight() - 40);
        }
    }
}
