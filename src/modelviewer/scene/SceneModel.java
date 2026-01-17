package modelviewer.scene;
import model.Model;
import model.Polygon;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
// SceneModel.java - модель на сцене с возможностью выделения элементов
public class SceneModel {
    private final String name;
    private final Model model;
    private final List<Integer> selectedVertices;
    private final List<Integer> selectedPolygons;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scale;
    private float translateX;
    private float translateY;

    public SceneModel(String name, Model model) {
        this.name = name;
        this.model = model;
        this.selectedVertices = new ArrayList<>();
        this.selectedPolygons = new ArrayList<>();
        this.rotationX = 0;
        this.rotationY = 0;
        this.rotationZ = 0;
        this.scale = 1.0f;
        this.translateX = 0;
        this.translateY = 0;
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public List<Integer> getSelectedVertices() {
        return new ArrayList<>(selectedVertices);
    }

    public List<Integer> getSelectedPolygons() {
        return new ArrayList<>(selectedPolygons);
    }

    public void selectVertex(int index) {
        if (!selectedVertices.contains(index)) {
            selectedVertices.add(index);
        }
    }

    public void selectPolygon(int index) {
        if (!selectedPolygons.contains(index)) {
            selectedPolygons.add(index);
        }
    }

    public void toggleVertexSelection(int index) {
        if (selectedVertices.contains(index)) {
            selectedVertices.remove((Integer) index);
        } else {
            selectedVertices.add(index);
        }
    }

    public void togglePolygonSelection(int index) {
        if (selectedPolygons.contains(index)) {
            selectedPolygons.remove((Integer) index);
        } else {
            selectedPolygons.add(index);
        }
    }

    public void clearSelection() {
        selectedVertices.clear();
        selectedPolygons.clear();
    }

    public void deleteSelectedElements() {
        // Удаление выбранных полигонов
        deleteSelectedPolygons();
        // Удаление выбранных вершин (сложнее, так как нужно обновить индексы)
        deleteSelectedVertices();
    }

    private void deleteSelectedPolygons() {
        if (selectedPolygons.isEmpty()) return;

        // Сортируем индексы в обратном порядке для удаления
        selectedPolygons.sort((a, b) -> b - a);
        for (int polygonIndex : selectedPolygons) {
            if (polygonIndex >= 0 && polygonIndex < model.polygons.size()) {
                model.polygons.remove(polygonIndex);
            }
        }
        selectedPolygons.clear();
    }

    private void deleteSelectedVertices() {
        if (selectedVertices.isEmpty()) return;

        // Сортируем индексы в обратном порядке
        selectedVertices.sort((a, b) -> b - a);

        for (int vertexIndex : selectedVertices) {
            if (vertexIndex >= 0 && vertexIndex < model.vertices.size()) {
                // Удаляем вершину
                model.vertices.remove(vertexIndex);

                // Обновляем индексы вершин в полигонах
                for (Polygon polygon : model.polygons) {
                    List<Integer> vertexIndices = polygon.getVertexIndices();
                    for (int i = 0; i < vertexIndices.size(); i++) {
                        int idx = vertexIndices.get(i);
                        if (idx > vertexIndex) {
                            vertexIndices.set(i, idx - 1);
                        } else if (idx == vertexIndex) {
                            // Если полигон ссылается на удаляемую вершину, удаляем полигон
                            model.polygons.remove(polygon);
                            break;
                        }
                    }
                }
            }
        }
        selectedVertices.clear();
    }

    // Геттеры и сеттеры для трансформаций
    public float getRotationX() { return rotationX; }
    public void setRotationX(float rotationX) { this.rotationX = rotationX; }

    public float getRotationY() { return rotationY; }
    public void setRotationY(float rotationY) { this.rotationY = rotationY; }

    public float getRotationZ() { return rotationZ; }
    public void setRotationZ(float rotationZ) { this.rotationZ = rotationZ; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }

    public float getTranslateX() { return translateX; }
    public void setTranslateX(float translateX) { this.translateX = translateX; }

    public float getTranslateY() { return translateY; }
    public void setTranslateY(float translateY) { this.translateY = translateY; }
}
