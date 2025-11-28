package com.jodexindustries.vertexfx;

import com.jodexindustries.vertexfx.geom.Point3D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record LinearInterpolation(Point3D start, Point3D end) {

    @Contract("_ -> new")
    public @NotNull Point3D getPoint(double t) {
        return start.lerp(end, t);
    }

    public @NotNull List<Point3D> generatePoints(double step) {
        if (step <= 0 || step > 1) throw new IllegalArgumentException("Step must be > 0 and ≤ 1, but was: " + step);

        List<Point3D> list = new ArrayList<>();
        for (double t = 0; t < 1; t += step) {
            list.add(start.lerp(end, t));
        }

        return list;
    }
}
