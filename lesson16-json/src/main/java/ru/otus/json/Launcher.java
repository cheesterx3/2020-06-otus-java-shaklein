package ru.otus.json;

import com.google.gson.Gson;
import ru.otus.json.serializer.ObjectToJsonSerializer;
import ru.otus.json.serializer.ObjectToJsonSerializerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Launcher {

    public static void main(String[] args) {
        Gson gson = new Gson();
        ObjectToJsonSerializer jsonSerializer = new ObjectToJsonSerializerImpl();
        TestClass object = new TestClass();
        String json = jsonSerializer.toJson(new TestClass());
        System.out.println("json representation = " + json);
        TestClass aClass = gson.fromJson(json, TestClass.class);
        System.out.println("Checking gson parsing = " + aClass.equals(object));
    }

    private static class TestClass {
        int a = 1;
        double b = 1.1;
        float c = 1.2f;
        byte d = 3;
        short e = 4;
        long f = 5L;
        boolean g = true;
        char h = '3';

        char[] chars=new char[]{'a','b','c','5','!'};
        int[] values = new int[]{1, 2, 3, 4, 5};
        int[][][] multidim = {{{0}, {1}}};
        double[] doubles = new double[]{1.1, 2.3, 4.3};
        long[] nullarray = null;

        List<Integer> integers = List.of(1, 2, 3);
        List<List<Double>> doublesMulti = List.of(List.of(1.1, 1.2, 1.3), List.of(2.1, 2.2, 2.3));
        List<int[][]> listOfArray = new ArrayList<>();
        {
            listOfArray.add(null);
            listOfArray.add(new int[][]{{}, {}});
            listOfArray.add(null);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestClass testClass = (TestClass) o;

            if (a != testClass.a) return false;
            if (Double.compare(testClass.b, b) != 0) return false;
            if (Float.compare(testClass.c, c) != 0) return false;
            if (d != testClass.d) return false;
            if (e != testClass.e) return false;
            if (f != testClass.f) return false;
            if (g != testClass.g) return false;
            if (h != testClass.h) return false;
            if (!Arrays.equals(values, testClass.values)) return false;
            if (!Arrays.deepEquals(multidim, testClass.multidim)) return false;
            if (!Arrays.equals(doubles, testClass.doubles)) return false;
            if (!Arrays.equals(nullarray, testClass.nullarray)) return false;
            if (!Objects.equals(integers, testClass.integers)) return false;
            if (!Objects.equals(doublesMulti, testClass.doublesMulti))
                return false;
            return listOfArray == testClass.listOfArray
                    || (listOfArray != null
                    && testClass.listOfArray != null
                    && Arrays.deepEquals(listOfArray.toArray(), testClass.listOfArray.toArray()));
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
            result = 31 * result + Arrays.hashCode(nullarray);
            result = 31 * result + (integers != null ? integers.hashCode() : 0);
            result = 31 * result + (doublesMulti != null ? doublesMulti.hashCode() : 0);
            result = 31 * result + (listOfArray != null ? listOfArray.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "TestClass{" +
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
                    ", nullarray=" + Arrays.toString(nullarray) +
                    ", integers=" + integers +
                    ", doublesMulti=" + doublesMulti +
                    ", listOfArray=" + listOfArray +
                    '}';
        }
    }

}
