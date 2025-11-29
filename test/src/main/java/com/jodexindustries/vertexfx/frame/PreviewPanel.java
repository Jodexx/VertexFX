package com.jodexindustries.vertexfx.frame;

import javax.swing.*;
import java.awt.*;

public abstract class PreviewPanel extends JPanel {

    public double t = 0;

    public double currentPosition = 0;

    private final VLabel locationLabel = new VLabel("Location: " + this.currentPosition);

    private final VLabel tLabel = new VLabel("t: " + this.t);

    protected final VertexFrame frame;

    public PreviewPanel(VertexFrame frame) {
        this.frame = frame;

        setLayout(new OverlayLayout(this));

        setDoubleBuffered(true);
        setBackground(Color.WHITE);
        setOpaque(true);

        setPreferredSize(new Dimension(400, 120));

        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        labelPanel.add(locationLabel);
        labelPanel.add(tLabel, BorderLayout.SOUTH);

        add(labelPanel);
    }

    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintComponent(g2);
    }

    public abstract void paintComponent(Graphics2D g);

    public void updateAnimation() {
        if (!frame.paused) {
            t += frame.speed;
            if (t > 1) t = 0;
        }

        locationLabel.setText("Location: " + this.currentPosition);
        tLabel.setText("t: " + this.t);

        repaint();
    }

    public static class VLabel extends JLabel {

        public VLabel(String text) {
            super(text);

            setAlignmentX(Component.LEFT_ALIGNMENT);
            setOpaque(true);
            setBackground(new Color(0, 0, 0, 220));
            setForeground(Color.GREEN);
            setFont(new Font("Consolas", Font.BOLD, 12));
        }
    }
}
