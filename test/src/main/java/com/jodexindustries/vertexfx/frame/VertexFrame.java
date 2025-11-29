package com.jodexindustries.vertexfx.frame;

import javax.swing.*;
import java.awt.*;

public class VertexFrame extends JFrame {

    static {
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private static final int DEFAULT_DELAY = 32;

    private final JPanel controls = new JPanel();

    private final Timer timer = new Timer(DEFAULT_DELAY, e -> this.preview.updateAnimation());

    public boolean paused = false;

    public double speed = 0.1;

    public double scale = 15;

    public double step = speed;

    private PreviewPanel preview;

    public VertexFrame() {
        super("VertexFX Preview");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFont(new Font("Arial", Font.PLAIN, 10));

        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(controls, BorderLayout.SOUTH);

        JPanel pauseRow = row();
        JButton pauseButton = new JButton("Pause");
        pauseButton.setFocusPainted(false);
        pauseRow.add(pauseButton);
        addControls(pauseRow);

        JPanel delayRow = labeledSliderRow("Delay:", 0, 512, DEFAULT_DELAY, 200, 50, 4, 64);
        JLabel delayLabel = (JLabel) delayRow.getClientProperty("label");
        JSlider delaySlider = (JSlider) delayRow.getClientProperty("slider");
        addControls(delayRow);

        JPanel speedRow = labeledSliderRow("Speed:", 0, 100, 100, 200, 50, 1, 10, 0.1);
        JLabel speedLabel = (JLabel) speedRow.getClientProperty("label");
        JSlider speedSlider = (JSlider) speedRow.getClientProperty("slider");
        addControls(speedRow);

        JPanel scaleRow = labeledSliderRow("Scale:", 0, 1000, 15, 200, 50, 1, 200);
        JLabel scaleLabel = (JLabel) scaleRow.getClientProperty("label");
        JSlider scaleSlider = (JSlider) scaleRow.getClientProperty("slider");
        addControls(scaleRow);

        JPanel stepRow = VertexFrame.labeledSliderRow("Step:", 10, 410, 100, 200, 50, 10, 100, 0.1);
        JLabel stepLabel = (JLabel) stepRow.getClientProperty("label");
        JSlider stepSlider = (JSlider) stepRow.getClientProperty("slider");
        addControls(stepRow);

        pauseButton.addActionListener(e -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        delaySlider.addChangeListener(e -> {
            int delay = delaySlider.getValue();
            delayLabel.setText("Delay: " + delay);
            timer.setDelay(delay);
        });

        speedSlider.addChangeListener(e -> {
            speed = Math.max(0, speedSlider.getValue()) / 1000.0;
            speedLabel.setText("Speed: " + speed);
        });

        scaleSlider.addChangeListener(e -> {
            scale = scaleSlider.getValue();
            scaleLabel.setText("Scale: " + scale);
        });

        stepSlider.addChangeListener(e -> {
            this.step = stepSlider.getValue() / 1000.0;
            stepLabel.setText("Step: " + this.step);
        });

        setLocationRelativeTo(null);
    }

    public void open() {
        pack();
        setVisible(true);

        if (this.preview != null) timer.start();
    }

    public void addPreview(PreviewPanel preview) {
        this.preview = preview;

        add(preview, BorderLayout.CENTER);
    }

    /**
     * Adds a component as a separate row with spacing
     */
    public void addControls(Component component) {
        controls.add(component);
        controls.add(Box.createRigidArea(new Dimension(0, 6)));
    }

    public static JPanel row() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    public static JPanel labeledSliderRow(String labelText, int min, int max, int value, int width, int height, int minorTick, int majorTick) {
        return labeledSliderRow(labelText, min, max, value, width, height, minorTick, majorTick, value);
    }

    public static JPanel labeledSliderRow(String labelText, int min, int max, int value, int width, int height, int minorTick, int majorTick, double realValue) {
        JPanel row = row();

        JLabel label = new JLabel(labelText + " ");
        row.add(label);

        JSlider slider = new JSlider(min, max, value);
        slider.setFocusable(false);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMaximumSize(new Dimension(width, height));
        slider.setPreferredSize(new Dimension(width, height));
        slider.setMinorTickSpacing(minorTick);
        slider.setMajorTickSpacing(majorTick);
        row.add(slider);

        JLabel valueLabel = new JLabel(labelText + " " + realValue);
        row.add(Box.createRigidArea(new Dimension(8, 0)));
        row.add(valueLabel);

        row.putClientProperty("slider", slider);
        row.putClientProperty("label", valueLabel);

        return row;
    }
}
