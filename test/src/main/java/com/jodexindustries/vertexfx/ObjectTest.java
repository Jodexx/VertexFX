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

        public Preview(VertexFrame frame) {
            super(frame);
        }

        @Override
        public void paintComponent(Graphics2D g2) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (double t = 0; t < 1; t += frame.step) {
                Point3D p = VertexFX.circle(0, 0, 0, 2, t).round(13);

                int x = (int) ((p.x() + 20) * frame.scale);
                int y = (int) ((p.z() + 5) * frame.scale);

                g2.fillRect(x, y, 2, 2);
            }
        }
    }
}
