package com.jodexindustries.vertexfx;

import com.jodexindustries.vertexfx.geom.Point3D;

import javax.swing.*;
import java.awt.*;

public class LinearInterpolationTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            RepaintManager.currentManager(null).setDoubleBufferingEnabled(true);

            JFrame frame = new JFrame("VertexFX Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Preview preview = new Preview(new LinearInterpolation(Point3D.of(0), Point3D.of(25)));
            preview.setLayout(new OverlayLayout(preview));

            preview.setFont(new Font("Arial", Font.PLAIN, 10));

            // location label
            JLabel locationLabel = new JLabel("Location: " + String.format("%.4f", preview.currentPosition));
            locationLabel.setAlignmentX(0f);
            locationLabel.setAlignmentY(1f);
            locationLabel.setOpaque(true);
            locationLabel.setBackground(new Color(0,0,0, 220));
            locationLabel.setForeground(Color.GREEN);
            locationLabel.setFont(new Font("Consolas", Font.BOLD, 12));
            preview.add(locationLabel);

            JPanel controls = new JPanel();
            controls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

            // points
            JPanel pointsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField startXField = new JTextField("0", 5);
            JTextField endXField = new JTextField("25", 5);
            startXField.setMaximumSize(new Dimension(50, 20));
            endXField.setMaximumSize(new Dimension(50, 20));
            pointsRow.add(new JLabel("Start X:"));
            pointsRow.add(startXField);
            pointsRow.add(new JLabel("End X:"));
            pointsRow.add(endXField);
            JButton updatePoints = new JButton("Update Points");
            updatePoints.setFocusPainted(false);
            pointsRow.add(updatePoints);

            JButton pauseButton = new JButton("Pause");
            pauseButton.setFocusPainted(false);
            pointsRow.add(pauseButton);
            controls.add(pointsRow);

            // speed
            JPanel speedRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JSlider speedSlider = new JSlider(10, 100, 100);
            speedSlider.setMajorTickSpacing(100);
            speedSlider.setMinorTickSpacing(10);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintLabels(true);
            speedSlider.setFocusable(false);
            speedSlider.setPreferredSize(new Dimension(100, 20));

            speedRow.add(new JLabel("Speed:"));
            speedRow.add(speedSlider);
            JLabel speedLabel = new JLabel(String.valueOf(preview.speed));
            speedRow.add(speedLabel);
            controls.add(speedRow);

            // pairwise
            JPanel pairwiseRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JSlider pairwiseSlider = new JSlider(10, 200, 100);
            pairwiseSlider.setMajorTickSpacing(100);
            pairwiseSlider.setMinorTickSpacing(10);
            pairwiseSlider.setPaintTicks(true);
            pairwiseSlider.setPaintLabels(true);
            pairwiseSlider.setFocusable(false);
            pairwiseSlider.setPreferredSize(new Dimension(200, 20));
            pairwiseRow.add(new JLabel("Pairwise distance:"));
            pairwiseRow.add(pairwiseSlider);
            JLabel pairwiseLabel = new JLabel(String.valueOf(preview.pairwiseDistance));
            pairwiseRow.add(pairwiseLabel);
            controls.add(pairwiseRow);

            frame.add(preview, BorderLayout.CENTER);
            frame.add(controls, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            new Timer(32, e -> {
                preview.updateAnimation();
                locationLabel.setText("Location: " + String.format("%.2f", preview.currentPosition));
            }).start();

            updatePoints.addActionListener(e -> {
                try {
                    double sx = Double.parseDouble(startXField.getText());
                    double ex = Double.parseDouble(endXField.getText());

                    preview.lerp = new LinearInterpolation(Point3D.of(sx, 0), Point3D.of(ex, 0));
                    preview.t = 0;
                    preview.repaint();
                } catch (NumberFormatException exn) {
                    JOptionPane.showMessageDialog(frame, "Invalid number format!");
                }
            });

            pauseButton.addActionListener(e -> {
                preview.paused = !preview.paused;
                pauseButton.setText(preview.paused ? "Resume" : "Pause");
            });

            speedSlider.addChangeListener(e -> {
                preview.speed = speedSlider.getValue() / 1000.0;
                speedLabel.setText(String.valueOf(preview.speed));
            });

            pairwiseSlider.addChangeListener(e -> {
                preview.pairwiseDistance = pairwiseSlider.getValue() / 1000.0;
                pairwiseLabel.setText(String.valueOf(preview.pairwiseDistance));
            });
        });
    }

    static class Preview extends JPanel {
        private LinearInterpolation lerp;
        double t = 0;
        double speed = 0.1;
        double pairwiseDistance = speed;

        boolean paused = false;

        double currentPosition = 0;

        public Preview(LinearInterpolation lerp) {
            this.lerp = lerp;

            setDoubleBuffered(true);
            setBackground(Color.WHITE);
            setOpaque(true);

            setPreferredSize(new Dimension(400, 120));
        }

        public void updateAnimation() {
            if (!paused) {
                t += speed;
                if (t > 1) t = 0;
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            for (Point3D p : lerp.generatePoints(pairwiseDistance)) {
                int x = (int) (p.x() * 15);
                int y = (int) (p.y() * 10);
                g2.fillRect(x, y, 1, 8);
                g2.drawString(String.format("%.2f", p.x()), x, y + 20);
            }

            g2.drawLine(0, 8, (int) (lerp.end().x() * 15), 8);

            Point3D moving = lerp.getPoint(t);
            g2.setColor(new Color(255,0,0,200));
            int mx = (int) (moving.x() * 15);
            int my = (int) (moving.y() * 10);
            g2.fillRect(mx, my, 3, 10);

            this.currentPosition = moving.x();
        }
    }

}
