package modelviewer.ui;
import modelviewer.scene.ModelScene;
import modelviewer.scene.SceneModel;
import model.Model;

import javax.swing.*;
import java.awt.*;
public class ModelPropertiesPanel extends JPanel {
    private final ModelScene scene;
    private final JLabel modelNameLabel;
    private final JLabel vertexCountLabel;
    private final JLabel polygonCountLabel;
    private final JPanel transformPanel;

    public ModelPropertiesPanel(ModelScene scene) {
        this.scene = scene;
        setLayout(new BorderLayout());

        // Панель информации
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Информация"));

        modelNameLabel = new JLabel("Модель: не выбрана");
        vertexCountLabel = new JLabel("Вершин: 0");
        polygonCountLabel = new JLabel("Полигонов: 0");
        JLabel selectionLabel = new JLabel("Выделено: 0 вершин, 0 полигонов");

        infoPanel.add(modelNameLabel);
        infoPanel.add(vertexCountLabel);
        infoPanel.add(polygonCountLabel);
        infoPanel.add(selectionLabel);

        // Панель трансформаций
        transformPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        transformPanel.setBorder(BorderFactory.createTitledBorder("Трансформации"));

        addTransformControls();

        JScrollPane scrollPane = new JScrollPane(transformPanel);
        scrollPane.setPreferredSize(new Dimension(280, 300));

        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addTransformControls() {
        transformPanel.add(new JLabel("Поворот X:"));
        JSpinner rotationXSpinner = new JSpinner(new SpinnerNumberModel(0, -180, 180, 1));
        transformPanel.add(rotationXSpinner);

        transformPanel.add(new JLabel("Поворот Y:"));
        JSpinner rotationYSpinner = new JSpinner(new SpinnerNumberModel(0, -180, 180, 1));
        transformPanel.add(rotationYSpinner);

        transformPanel.add(new JLabel("Поворот Z:"));
        JSpinner rotationZSpinner = new JSpinner(new SpinnerNumberModel(0, -180, 180, 1));
        transformPanel.add(rotationZSpinner);

        transformPanel.add(new JLabel("Масштаб:"));
        JSpinner scaleSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        transformPanel.add(scaleSpinner);

        transformPanel.add(new JLabel("Смещение X:"));
        JSpinner translateXSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
        transformPanel.add(translateXSpinner);

        transformPanel.add(new JLabel("Смещение Y:"));
        JSpinner translateYSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
        transformPanel.add(translateYSpinner);

        // Обработчики изменений
        rotationXSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setRotationX(((Number) rotationXSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });

        rotationYSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setRotationY(((Number) rotationYSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });

        rotationZSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setRotationZ(((Number) rotationZSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });

        scaleSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setScale(((Number) scaleSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });

        translateXSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setTranslateX(((Number) translateXSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });

        translateYSpinner.addChangeListener(e -> {
            SceneModel model = scene.getSelectedModel();
            if (model != null) {
                model.setTranslateY(((Number) translateYSpinner.getValue()).floatValue());
                scene.notifyListeners();
            }
        });
    }

    public void updateProperties() {
        SceneModel selectedModel = scene.getSelectedModel();
        if (selectedModel != null) {
            Model model = selectedModel.getModel();
            modelNameLabel.setText("Модель: " + selectedModel.getName());
            vertexCountLabel.setText("Вершин: " + model.vertices.size());
            polygonCountLabel.setText("Полигонов: " + model.polygons.size());

            // Обновление значений спиннеров
            Component[] components = transformPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof JSpinner) {
                    JSpinner spinner = (JSpinner) components[i];
                    switch (i / 2) {
                        case 0: // rotationX
                            spinner.setValue(selectedModel.getRotationX());
                            break;
                        case 1: // rotationY
                            spinner.setValue(selectedModel.getRotationY());
                            break;
                        case 2: // rotationZ
                            spinner.setValue(selectedModel.getRotationZ());
                            break;
                        case 3: // scale
                            spinner.setValue(selectedModel.getScale());
                            break;
                        case 4: // translateX
                            spinner.setValue(selectedModel.getTranslateX());
                            break;
                        case 5: // translateY
                            spinner.setValue(selectedModel.getTranslateY());
                            break;
                    }
                }
            }
        } else {
            modelNameLabel.setText("Модель: не выбрана");
            vertexCountLabel.setText("Вершин: 0");
            polygonCountLabel.setText("Полигонов: 0");
        }
    }
}
