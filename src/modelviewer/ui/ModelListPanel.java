package modelviewer.ui;
import modelviewer.scene.ModelScene;
import modelviewer.scene.SceneModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class ModelListPanel extends JPanel {
    private final ModelScene scene;
    private final DefaultListModel<String> listModel;
    private final JList<String> modelList;

    public ModelListPanel(ModelScene scene) {
        this.scene = scene;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        modelList = new JList<>(listModel);
        modelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        modelList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] selectedIndices = modelList.getSelectedIndices();
                if (selectedIndices.length == 0) {
                    scene.clearSelection();
                } else {
                    for (int i = 0; i < listModel.size(); i++) {
                        if (modelList.isSelectedIndex(i)) {
                            scene.toggleModelSelection(i);
                        }
                    }
                }
            }
        });

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Удалить модель");
        deleteMenuItem.addActionListener(e -> deleteSelectedModel());
        contextMenu.add(deleteMenuItem);

        modelList.setComponentPopupMenu(contextMenu);

        JScrollPane scrollPane = new JScrollPane(modelList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(e -> deleteSelectedModel());
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void updateModelList() {
        listModel.clear();
        List<SceneModel> models = scene.getModels();
        for (SceneModel model : models) {
            listModel.addElement(model.getName());
        }

        modelList.clearSelection();
        for (int i = 0; i < listModel.size(); i++) {
            if (scene.isModelSelected(i)) {
                modelList.addSelectionInterval(i, i);
            }
        }
    }

    private void deleteSelectedModel() {
        int[] selectedIndices = modelList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Удалить выбранные модели?",
                    "Подтверждение удаления",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    scene.removeModel(selectedIndices[i]);
                }
            }
        }
    }
}
