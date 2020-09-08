package ru.otus.json.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ObjectToTest {
    private final int a;
    private final double b;
    private final float c;
    private final byte d;
    private final short e;
    private final long f;
    private final boolean g;
    private final char h;

    private final int[] values;
    private final int[][][] multidim;
    private final double[] doubles;
    private final char[] chars;

    private final List<Integer> integers;
    private final List<List<Double>> doublesMulti;

    public ObjectToTest(int a,
                        double b,
                        float c,
                        byte d,
                        short e,
                        long f,
                        boolean g,
                        char h,
                        int[] values,
                        int[][][] multidim,
                        double[] doubles,
                        char[] chars, List<Integer> integers,
                        List<List<Double>> doublesMulti) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.values = values;
        this.multidim = multidim;
        this.doubles = doubles;
        this.chars = chars;
        this.integers = integers;
        this.doublesMulti = doublesMulti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectToTest that = (ObjectToTest) o;

        if (a != that.a) return false;
        if (Double.compare(that.b, b) != 0) return false;
        if (Float.compare(that.c, c) != 0) return false;
        if (d != that.d) return false;
        if (e != that.e) return false;
        if (f != that.f) return false;
        if (g != that.g) return false;
        if (h != that.h) return false;
        if (!Arrays.equals(values, that.values)) return false;
        if (!Arrays.deepEquals(multidim, that.multidim)) return false;
        if (!Arrays.equals(doubles, that.doubles)) return false;
        if (!Arrays.equals(chars, that.chars)) return false;
        if (!Objects.equals(integers, that.integers)) return false;
        return Objects.equals(doublesMulti, that.doublesMulti);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = a;
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (c != +0.0f ? Float.floatToIntBits(c) : 0);
        result = 31 * result + (int) d;
        result = 31 * result + (int) e;
        result = 31 * result + (int) (f ^ (f >>> 32));
        result = 31 * result + (g ? 1 : 0);
        result = 31 * result + (int) h;
        result = 31 * result + Arrays.hashCode(values);
        result = 31 * result + Arrays.deepHashCode(multidim);
        result = 31 * result + Arrays.hashCode(doubles);
        result = 31 * result + Arrays.hashCode(chars);
        result = 31 * result + (integers != null ? integers.hashCode() : 0);
        result = 31 * result + (doublesMulti != null ? doublesMulti.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectToTest{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                ", values=" + Arrays.toString(values) +
                ", multidim=" + Arrays.toString(multidim) +
                ", doubles=" + Arrays.toString(doubles) +
                ", chars=" + Arrays.toString(chars) +
                ", integers=" + integers +
                ", doublesMulti=" + doublesMulti +
                '}';
    }
}
