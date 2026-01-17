package GUI;

import Model.Model3D;
import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    private Model3D model;

    private JSpinner scaleXSpinner, scaleYSpinner, scaleZSpinner;
    private JSpinner rotateXSpinner, rotateYSpinner, rotateZSpinner;
    private JSpinner translateXSpinner, translateYSpinner, translateZSpinner;

    public Panel(Model3D model) {
        this.model = model;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 3, 5, 5));

        add(new JLabel("Scale X:"));
        scaleXSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        add(scaleXSpinner);

        add(new JLabel("Scale Y:"));
        scaleYSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        add(scaleYSpinner);

        add(new JLabel("Scale Z:"));
        scaleZSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        add(scaleZSpinner);

        add(new JLabel("Rotate X:"));
        rotateXSpinner = new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0));
        add(rotateXSpinner);

        add(new JLabel("Rotate Y:"));
        rotateYSpinner = new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0));
        add(rotateYSpinner);

        add(new JLabel("Rotate Z:"));
        rotateZSpinner = new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0));
        add(rotateZSpinner);

        add(new JLabel("Translate X:"));
        translateXSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.5));
        add(translateXSpinner);

        add(new JLabel("Translate Y:"));
        translateYSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.5));
        add(translateYSpinner);

        add(new JLabel("Translate Z:"));
        translateZSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.5));
        add(translateZSpinner);

        JButton applyButton = new JButton("Apply Transformations");
        applyButton.addActionListener(e -> applyTransformations());
        add(applyButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetTransformations());
        add(resetButton);
    }

    private void applyTransformations() {

        float rotX = (float) Math.toRadians((Double) rotateXSpinner.getValue());
        float rotY = (float) Math.toRadians((Double) rotateYSpinner.getValue());
        float rotZ = (float) Math.toRadians((Double) rotateZSpinner.getValue());

        model.getaTransform().setRotation(rotX, rotY, rotZ);
        model.getaTransform().setScale(
                ((Double) scaleXSpinner.getValue()).floatValue(),
                ((Double) scaleYSpinner.getValue()).floatValue(),
                ((Double) scaleZSpinner.getValue()).floatValue()
        );
        model.getaTransform().setTranslation(
                ((Double) translateXSpinner.getValue()).floatValue(),
                ((Double) translateYSpinner.getValue()).floatValue(),
                ((Double) translateZSpinner.getValue()).floatValue()
        );
    }

    private void resetTransformations() {
        model.resetaTransformations();
        scaleXSpinner.setValue(1.0);
        scaleYSpinner.setValue(1.0);
        scaleZSpinner.setValue(1.0);
        rotateXSpinner.setValue(0.0);
        rotateYSpinner.setValue(0.0);
        rotateZSpinner.setValue(0.0);
        translateXSpinner.setValue(0.0);
        translateYSpinner.setValue(0.0);
        translateZSpinner.setValue(0.0);
    }
}