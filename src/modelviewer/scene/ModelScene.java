package modelviewer.scene;

import java.util.ArrayList;
import java.util.List;
public class ModelScene {
    private final List<SceneModel> models;
    private final List<Integer> selectedModelIndices;
    private SceneModel selectedModel;
    private final List<SceneChangeListener> listeners;

    public ModelScene() {
        models = new ArrayList<>();
        selectedModelIndices = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void addModel(SceneModel model) {
        models.add(model);
        selectModel(models.size() - 1);
        notifyListeners();
    }

    public void removeModel(int index) {
        if (index >= 0 && index < models.size()) {
            models.remove(index);
            selectedModelIndices.remove((Integer) index);
            updateSelectedIndicesAfterRemoval(index);
            updateSelectedModel();
            notifyListeners();
        }
    }

    public List<SceneModel> getModels() {
        return new ArrayList<>(models);
    }

    public void selectModel(int index) {
        if (index >= 0 && index < models.size()) {
            selectedModelIndices.clear();
            selectedModelIndices.add(index);
            selectedModel = models.get(index);
            notifyListeners();
        }
    }

    public void toggleModelSelection(int index) {
        if (index >= 0 && index < models.size()) {
            if (selectedModelIndices.contains(index)) {
                selectedModelIndices.remove((Integer) index);
            } else {
                selectedModelIndices.add(index);
            }
            updateSelectedModel();
            notifyListeners();
        }
    }

    public boolean isModelSelected(int index) {
        return selectedModelIndices.contains(index);
    }

    public void clearSelection() {
        selectedModelIndices.clear();
        selectedModel = null;
        notifyListeners();
    }

    public SceneModel getSelectedModel() {
        return selectedModel;
    }

    public List<SceneModel> getSelectedModels() {
        List<SceneModel> selected = new ArrayList<>();
        for (int index : selectedModelIndices) {
            selected.add(models.get(index));
        }
        return selected;
    }

    public void deleteSelectedElements() {
        if (selectedModel != null) {
            selectedModel.deleteSelectedElements();
            notifyListeners();
        }
    }

    public void addSceneChangeListener(SceneChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (SceneChangeListener listener : listeners) {
            listener.onSceneChanged();
        }
    }

    private void updateSelectedIndicesAfterRemoval(int removedIndex) {
        List<Integer> newIndices = new ArrayList<>();
        for (int index : selectedModelIndices) {
            if (index < removedIndex) {
                newIndices.add(index);
            } else if (index > removedIndex) {
                newIndices.add(index - 1);
            }
        }
        selectedModelIndices.clear();
        selectedModelIndices.addAll(newIndices);
    }

    private void updateSelectedModel() {
        if (!selectedModelIndices.isEmpty()) {
            selectedModel = models.get(selectedModelIndices.get(selectedModelIndices.size() - 1));
        } else {
            selectedModel = null;
        }
    }
}
