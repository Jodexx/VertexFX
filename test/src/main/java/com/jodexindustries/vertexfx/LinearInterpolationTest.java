package com.jodexindustries.vertexfx;

import com.jodexindustries.vertexfx.frame.PreviewPanel;
import com.jodexindustries.vertexfx.frame.VertexFrame;
import com.jodexindustries.vertexfx.geom.Point3D;

import javax.swing.*;
import java.awt.*;

public class LinearInterpolationTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VertexFrame frame = new VertexFrame();
            frame.addPreview(new Preview(frame));

            frame.open();
        });
    }

    static class Preview extends PreviewPanel {

        private LinearInterpolation lerp = new LinearInterpolation(Point3D.of(0), Point3D.of(25));

        private double offset = -lerp.start().x();

        private final JSpinner startXField = new JSpinner(new SpinnerNumberModel(0.0, null, null, 0.1));
        private final JSpinner endXField = new JSpinner(new SpinnerNumberModel(25.0, null, null, 0.1));

        public Preview(VertexFrame frame) {
            super(frame);

            // points
            JPanel pointsRow = VertexFrame.row();
            pointsRow.add(new JLabel("Start X:"));
            pointsRow.add(startXField);
            pointsRow.add(new JLabel("End X:"));
            pointsRow.add(endXField);
            frame.addControls(pointsRow);

            startXField.addChangeListener(e -> updateLine());
            endXField.addChangeListener(e -> updateLine());
        }

        private void updateLine() {
            try {
                double sx = (double) startXField.getValue();
                double ex = (double) endXField.getValue();

                this.lerp = new LinearInterpolation(Point3D.of(sx, 0), Point3D.of(ex, 0));
                this.offset = -lerp.start().x();
                this.t = 0;
                this.repaint();
            } catch (Exception exn) {
                JOptionPane.showMessageDialog(frame, "Invalid number format!");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Point3D p : lerp.generatePoints(frame.step)) {
                int x = (int) ((p.x() + offset) * frame.scale);
                int y = (int) (p.y() * frame.scale);
                g2.fillRect(x, y, 1, 8);
                g2.drawString(String.valueOf(p.x()), x, y + 20);
            }

            g2.drawLine(0, 8, (int) ((lerp.end().x() + offset) * frame.scale), 8);

            Point3D moving = lerp.getPoint(t);
            g2.setColor(new Color(255, 0, 0, 200));
            int mx = (int) ((moving.x() + offset) * frame.scale);
            int my = (int) (moving.y() * frame.scale);
            g2.fillRect(mx, my, 3, 10);

            this.currentPosition = moving.x();
        }
    }

}
