package modelviewer;

import modelviewer.scene.ModelScene;
import modelviewer.scene.SceneModel;
import modelviewer.ui.ModelListPanel;
import modelviewer.ui.ModelPropertiesPanel;
import modelviewer.ui.ModelViewerPanel;
import model.Model;
import objreader.ObjReader;
import objreader.ObjReaderException;
import objwriter.ObjWriter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelViewerApp extends JFrame {
    private final ModelScene scene;
    private final ModelListPanel modelListPanel;
    private final ModelPropertiesPanel propertiesPanel;
    private final ModelViewerPanel viewerPanel;
    private final JMenuBar menuBar;

    public ModelViewerApp() {
        super("3D Model Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);

        // Инициализация компонентов
        scene = new ModelScene();
        modelListPanel = new ModelListPanel(scene);
        propertiesPanel = new ModelPropertiesPanel(scene);
        viewerPanel = new ModelViewerPanel(scene);

        // Создание меню
        menuBar = createMenuBar();
        setJMenuBar(menuBar);

        setupLayout();

        scene.addSceneChangeListener(() -> {
            modelListPanel.updateModelList();
            propertiesPanel.updateProperties();
            viewerPanel.repaint();
        });

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");

        JMenuItem openItem = new JMenuItem("Открыть модель...");
        openItem.addActionListener(e -> openModel());
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Сохранить модель...");
        saveItem.addActionListener(e -> saveModel());
        fileMenu.add(saveItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Правка");

        JMenuItem deleteItem = new JMenuItem("Удалить выделенное");
        deleteItem.addActionListener(e -> deleteSelected());
        editMenu.add(deleteItem);

        JMenuItem clearSelectionItem = new JMenuItem("Снять выделение");
        clearSelectionItem.addActionListener(e -> scene.clearSelection());
        editMenu.add(clearSelectionItem);

        JMenu viewMenu = new JMenu("Вид");

        JCheckBoxMenuItem wireframeItem = new JCheckBoxMenuItem("Каркасный режим");
        wireframeItem.addActionListener(e -> viewerPanel.setWireframeMode(wireframeItem.isSelected()));
        viewMenu.add(wireframeItem);

        JCheckBoxMenuItem showNormalsItem = new JCheckBoxMenuItem("Показать нормали");
        showNormalsItem.addActionListener(e -> viewerPanel.setShowNormals(showNormalsItem.isSelected()));
        viewMenu.add(showNormalsItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        return menuBar;
    }

    private void setupLayout() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Левая панель - список моделей
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Модели"));
        leftPanel.add(modelListPanel, BorderLayout.CENTER);

        // Правая панель - свойства
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Свойства"));
        rightPanel.add(propertiesPanel, BorderLayout.CENTER);

        // Центральная панель - просмотрщик
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Просмотр"));
        centerPanel.add(viewerPanel, BorderLayout.CENTER);

        // Панель инструментов
        JToolBar toolBar = createToolBar();
        centerPanel.add(toolBar, BorderLayout.NORTH);

        // Добавление всех панелей
        container.add(leftPanel, BorderLayout.WEST);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Создаем простые кнопки с текстом, так как у нас нет иконок
        JButton openButton = new JButton("Открыть");
        openButton.setToolTipText("Открыть модель");
        openButton.addActionListener(e -> openModel());

        JButton saveButton = new JButton("Сохранить");
        saveButton.setToolTipText("Сохранить модель");
        saveButton.addActionListener(e -> saveModel());

        JButton deleteButton = new JButton("Удалить");
        deleteButton.setToolTipText("Удалить выделенное");
        deleteButton.addActionListener(e -> deleteSelected());

        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);

        return toolBar;
    }

    private void openModel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".obj");
            }

            @Override
            public String getDescription() {
                return "OBJ Files (*.obj)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path fileName = fileChooser.getSelectedFile().toPath();
                String fileContent = Files.readString(fileName);

                Model model = ObjReader.read(fileContent);
                String modelName = fileChooser.getSelectedFile().getName();
                scene.addModel(new SceneModel(modelName, model));

            } catch (ObjReaderException e) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка чтения файла:\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Неизвестная ошибка:\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveModel() {
        SceneModel selectedModel = scene.getSelectedModel();
        if (selectedModel == null) {
            JOptionPane.showMessageDialog(this,
                    "Не выбрана модель для сохранения",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(selectedModel.getName() + ".obj"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".obj");
            }

            @Override
            public String getDescription() {
                return "OBJ Files (*.obj)";
            }
        });

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".obj")) {
                filePath += ".obj";
            }

            try {
                ObjWriter.write(selectedModel.getModel(), filePath);
                JOptionPane.showMessageDialog(this,
                        "Модель успешно сохранена в:\n" + filePath,
                        "Сохранение",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка при сохранении модели:\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        scene.deleteSelectedElements();
    }

    // Метод для уведомления слушателей (нужен для ModelScene)
    public void notifySceneChanged() {
        scene.notifyListeners();
    }
}