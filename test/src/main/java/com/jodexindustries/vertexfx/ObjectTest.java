package com.jodexindustries.vertexfx;

import com.jodexindustries.vertexfx.frame.PreviewPanel;
import com.jodexindustries.vertexfx.frame.VertexFrame;
import com.jodexindustries.vertexfx.geom.Point3D;

import javax.swing.*;
import java.awt.*;

public class ObjectTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VertexFrame frame = new VertexFrame();
            frame.addPreview(new Preview(frame));

            frame.open();
        });
    }


    static class Preview extends PreviewPanel {

        private int dots = 10;

        public Preview(VertexFrame frame) {
            super(frame);

            JPanel dotsRow = VertexFrame.labeledSliderRow("Dots:", 0, 100, dots, 200, 50, 1, 10);
            JLabel dotsLabel = (JLabel) dotsRow.getClientProperty("label");
            JSlider dotsSlider = (JSlider) dotsRow.getClientProperty("slider");
            frame.addControls(dotsRow);

            dotsSlider.addChangeListener(e -> {
                this.dots = dotsSlider.getValue();
                dotsLabel.setText("Dots: " + this.dots);

                JSlider stepSlider = (JSlider) frame.stepRow.getClientProperty("slider");
                frame.step = 1.0 / this.dots;
                stepSlider.setValue((int) (frame.step * 1000));
            });
        }

        @Override
        public void paintComponent(Graphics2D g2) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int dot = Math.max(1, (int)(frame.scale / 15.0));

            int samples = (int)(1.0 / frame.step);

            for (int i = 0; i < samples; i++) {
                double t = (double) i / samples;

                Point3D p = VertexFX.circle(0, 0, 0, 2, t).round(13);

                int x = (int) ((p.x() + 5) * frame.scale);
                int y = (int) ((p.z() + 5) * frame.scale);

                g2.fillRect(x, y, dot, dot);
            }

        }
    }
}
