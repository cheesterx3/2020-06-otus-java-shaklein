package ru.otus.json.models;

import java.util.List;

public class ObjectToTestBuilder {
    private int a;
    private double b;
    private float c;
    private byte d;
    private short e;
    private long f;
    private boolean g;
    private char h;
    private int[] values;
    private int[][][] multidim;
    private double[] doubles;
    private List<Integer> integers;
    private List<List<Double>> doublesMulti;
    private char[] chars;

    public ObjectToTestBuilder setA(int a) {
        this.a = a;
        return this;
    }

    public ObjectToTestBuilder setB(double b) {
        this.b = b;
        return this;
    }

    public ObjectToTestBuilder setC(float c) {
        this.c = c;
        return this;
    }

    public ObjectToTestBuilder setD(byte d) {
        this.d = d;
        return this;
    }

    public ObjectToTestBuilder setE(short e) {
        this.e = e;
        return this;
    }

    public ObjectToTestBuilder setF(long f) {
        this.f = f;
        return this;
    }

    public ObjectToTestBuilder setG(boolean g) {
        this.g = g;
        return this;
    }

    public ObjectToTestBuilder setH(char h) {
        this.h = h;
        return this;
    }

    public ObjectToTestBuilder setValues(int[] values) {
        this.values = values;
        return this;
    }

    public ObjectToTestBuilder setMultidim(int[][][] multidim) {
        this.multidim = multidim;
        return this;
    }

    public ObjectToTestBuilder setDoubles(double[] doubles) {
        this.doubles = doubles;
        return this;
    }

    public ObjectToTestBuilder setIntegers(List<Integer> integers) {
        this.integers = integers;
        return this;
    }

    public ObjectToTestBuilder setDoublesMulti(List<List<Double>> doublesMulti) {
        this.doublesMulti = doublesMulti;
        return this;
    }

    public ObjectToTest createObjectToTest() {
        return new ObjectToTest(a, b, c, d, e, f, g, h, values, multidim, doubles, chars, integers, doublesMulti);
    }

    public ObjectToTestBuilder setChars(char[] chars) {
        this.chars = chars;
        return this;
    }
}