package com.jodexindustries.vertexfx.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an immutable 3D point or vector in Cartesian space.
 */
public record Point3D(double x, double y, double z) {

    /**
     * Adds this vector to another.
     *
     * @param o the other vector
     * @return a new vector representing the sum
     */
    @Contract("_ -> new")
    public @NotNull Point3D add(Point3D o) {
        return new Point3D(x + o.x, y + o.y, z + o.z);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @param o the vector to subtract
     * @return a new vector representing the difference
     */
    @Contract("_ -> new")
    public @NotNull Point3D subtract(Point3D o) {
        return new Point3D(x - o.x, y - o.y, z - o.z);
    }

    /**
     * Multiplies this vector by a scalar.
     *
     * @param m the scalar multiplier
     * @return a scaled vector
     */
    @Contract("_ -> new")
    public @NotNull Point3D multiply(double m) {
        return new Point3D(x * m, y * m, z * m);
    }

    /**
     * Divides this vector by a scalar.
     *
     * @param d the scalar divisor (must not be zero)
     * @return a scaled vector
     */
    @Contract("_ -> new")
    public @NotNull Point3D divide(double d) {
        return new Point3D(x / d, y / d, z / d);
    }

    /**
     * Returns the squared magnitude of the vector.
     * This avoids a square root and is useful for comparisons.
     *
     * @return the squared length
     */
    @Contract(pure = true)
    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Returns the magnitude (length) of the vector.
     *
     * @return the vector's length
     */
    @Contract(pure = true)
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns the normalized version of this vector.
     * If the vector has zero length, the original instance is returned.
     *
     * @return the normalized unit vector
     */
    public Point3D normalize() {
        double len = length();
        return len == 0 ? this : divide(len);
    }

    /**
     * Computes the Euclidean distance between this point and another.
     *
     * @param o the other point
     * @return the distance
     */
    @Contract(pure = true)
    public double distance(@NotNull Point3D o) {
        return Math.sqrt(
                (x - o.x) * (x - o.x) +
                        (y - o.y) * (y - o.y) +
                        (z - o.z) * (z - o.z)
        );
    }

    /**
     * Computes the dot (scalar) product between this vector and another.
     *
     * @param o the other vector
     * @return the dot product
     */
    @Contract(pure = true)
    public double dot(@NotNull Point3D o) {
        return x * o.x + y * o.y + z * o.z;
    }

    /**
     * Computes the cross (vector) product between this vector and another.
     * The result is a vector perpendicular to both inputs.
     *
     * @param o the other vector
     * @return the cross product vector
     */
    @Contract("_ -> new")
    public @NotNull Point3D cross(@NotNull Point3D o) {
        return new Point3D(
                y * o.z - z * o.y,
                z * o.x - x * o.z,
                x * o.y - y * o.x
        );
    }

    /**
     * Rotates this point around the X-axis.
     *
     * @param radians angle in radians
     * @return the rotated point
     */
    @Contract("_ -> new")
    public @NotNull Point3D rotateX(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Point3D(
                x,
                y * cos - z * sin,
                y * sin + z * cos
        );
    }

    /**
     * Rotates this point around the Y-axis.
     *
     * @param radians angle in radians
     * @return the rotated point
     */
    @Contract("_ -> new")
    public @NotNull Point3D rotateY(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Point3D(
                x * cos + z * sin,
                y,
                -x * sin + z * cos
        );
    }

    /**
     * Rotates this point around the Z-axis.
     *
     * @param radians angle in radians
     * @return the rotated point
     */
    @Contract("_ -> new")
    public @NotNull Point3D rotateZ(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Point3D(
                x * cos - y * sin,
                x * sin + y * cos,
                z
        );
    }

    /**
     * Rotates the point around a pivot using Euler angles.
     * Rotation order: X → Y → Z.
     *
     * @param pivot  the pivot point
     * @param pitch  rotation around X-axis (radians)
     * @param yaw    rotation around Y-axis (radians)
     * @param roll   rotation around Z-axis (radians)
     * @return the rotated point
     */
    public @NotNull Point3D rotateAround(Point3D pivot, double pitch, double yaw, double roll) {
        Point3D shifted = this.subtract(pivot);
        shifted = shifted.rotateX(pitch).rotateY(yaw).rotateZ(roll);
        return shifted.add(pivot);
    }

    /**
     * Performs linear interpolation between this point and another.
     *
     * @param o the target point
     * @param t interpolation factor (0 = this, 1 = o)
     * @return the interpolated point
     */
    @Contract("_, _ -> new")
    public @NotNull Point3D lerp(Point3D o, double t) {
        return new Point3D(
                x + (o.x - x) * t,
                y + (o.y - y) * t,
                z + (o.z - z) * t
        );
    }

    @Contract("_ -> new")
    public static @NotNull Point3D of(double x) {
        return of(x, 0);
    }

    @Contract("_, _ -> new")
    public static @NotNull Point3D of(double x, double y) {
        return of(x, y, 0);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Point3D of(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z))
            throw new IllegalArgumentException("Coordinates must not be NaN");
        return new Point3D(x, y, z);
    }

}
