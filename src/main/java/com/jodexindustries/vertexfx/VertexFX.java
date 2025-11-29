package com.jodexindustries.vertexfx;

import com.jodexindustries.vertexfx.geom.Point3D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VertexFX {

    /**
     * Generates a point on a circle based on t in range [0,1].
     *
     * @param cx center X coordinate
     * @param cy center Y coordinate
     * @param cz center Z coordinate
     * @param r  radius of the circle
     * @param t  normalized time [0..1]
     * @return position on the circle
     */
    @Contract("_, _, _, _, _ -> new")
    public static @NotNull Point3D circle(double cx, double cy, double cz, double r, double t) {
        double angle = t * 2 * Math.PI;
        return new Point3D(
                cx + r * Math.cos(angle),
                cy,
                cz + r * Math.sin(angle)
        );
    }

    /**
     * Generates a point on an ellipse based on t in range [0,1].
     *
     * @param cx center X coordinate
     * @param cy center Y coordinate
     * @param cz center Z coordinate
     * @param a  horizontal radius
     * @param b  vertical radius
     * @param t  normalized time [0..1]
     * @return position on the ellipse
     */
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Point3D ellipse(double cx, double cy, double cz, double a, double b, double t) {
        double angle = t * 2 * Math.PI;
        return new Point3D(
                cx + a * Math.cos(angle),
                cy,
                cz + b * Math.sin(angle)
        );
    }

    /**
     * Generates a 3D spiral position.
     *
     * @param cx center X coordinate
     * @param cy center Y coordinate
     * @param cz center Z coordinate
     * @param r  spiral radius
     * @param h  total height change
     * @param t  normalized time [0..1]
     * @return position on a spiral
     */
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Point3D spiral(double cx, double cy, double cz, double r, double h, double t) {
        double angle = t * 10 * Math.PI;
        return new Point3D(
                cx + r * Math.cos(angle),
                cy + h * t,
                cz + r * Math.sin(angle)
        );
    }

    /**
     * Simulates a pendulum oscillation angle.
     *
     * @param maxAngle maximum angle in degrees or radians (your choice)
     * @param t        normalized time [0..1]
     * @return oscillating angle
     */
    @Contract(pure = true)
    public static double pendulumAngle(double maxAngle, double t) {
        return maxAngle * Math.cos(t * Math.PI * 2);
    }

    /**
     * Produces a sinusoidal wave value.
     *
     * @param amplitude peak height of the wave
     * @param frequency number of cycles per 1 unit of t
     * @param t         normalized time [0..1]
     * @return wave value
     */
    @Contract(pure = true)
    public static double wave(double amplitude, double frequency, double t) {
        return amplitude * Math.sin(t * frequency * 2 * Math.PI);
    }

    /**
     * Quadratic Bezier interpolation between 3 points.
     *
     * @param p0 start point
     * @param p1 control point
     * @param p2 end point
     * @param t  normalized time [0..1]
     * @return interpolated position
     */
    @Contract("_, _, _, _ -> new")
    public static @NotNull Point3D bezier(Point3D p0, Point3D p1, Point3D p2, double t) {
        double u = 1 - t;
        return new Point3D(
                u * u * p0.x() + 2 * u * t * p1.x() + t * t * p2.x(),
                u * u * p0.y() + 2 * u * t * p1.y() + t * t * p2.y(),
                u * u * p0.z() + 2 * u * t * p1.z() + t * t * p2.z()
        );
    }

    /**
     * Catmull-Rom spline interpolation generating a smooth curve through the points.
     *
     * @param p0 previous control point
     * @param p1 start point of the segment
     * @param p2 end point of the segment
     * @param p3 next control point
     * @param t  normalized time [0..1]
     * @return smoothly interpolated position
     */
    @Contract("_, _, _, _, _ -> new")
    public static @NotNull Point3D catmullRom(Point3D p0, Point3D p1, Point3D p2, Point3D p3, double t) {
        double t2 = t * t, t3 = t2 * t;
        return new Point3D(
                0.5 * ((2 * p1.x()) + (-p0.x() + p2.x()) * t + (2 * p0.x() - 5 * p1.x() + 4 * p2.x() - p3.x()) * t2 + (-p0.x() + 3 * p1.x() - 3 * p2.x() + p3.x()) * t3),
                0.5 * ((2 * p1.y()) + (-p0.y() + p2.y()) * t + (2 * p0.y() - 5 * p1.y() + 4 * p2.y() - p3.y()) * t2 + (-p0.y() + 3 * p1.y() - 3 * p2.y() + p3.y()) * t3),
                0.5 * ((2 * p1.z()) + (-p0.z() + p2.z()) * t + (2 * p0.z() - 5 * p1.z() + 4 * p2.z() - p3.z()) * t2 + (-p0.z() + 3 * p1.z() - 3 * p2.z() + p3.z()) * t3)
        );
    }

    /**
     * Creates an arc between two points by using a Bézier curve with midpoint raised by height.
     *
     * @param start  starting position
     * @param end    ending position
     * @param height height of the arc's peak
     * @param t      normalized time [0..1]
     * @return point along the arc
     */
    public static @NotNull Point3D arc(Point3D start, Point3D end, double height, double t) {
        Point3D mid = start.add(end).multiply(0.5).add(new Point3D(0, height, 0));
        return bezier(start, mid, end, t);
    }
}
