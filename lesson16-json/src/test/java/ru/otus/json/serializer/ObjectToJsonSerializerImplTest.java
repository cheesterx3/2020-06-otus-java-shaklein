package ru.otus.json.serializer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.json.models.ObjectToTest;
import ru.otus.json.models.ObjectToTestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectToJsonSerializerImplTest {
    private Gson gson;
    private ObjectToJsonSerializer serializer;

    private static Stream<Arguments> processObjectsToTest() {
        return Stream.of(new ObjectToTestBuilder()
                        .setA(1).setB(1.5d).setC(4.5f).setD((byte) 1).setE((short) 3)
                        .setF(12312L).setH('s').setG(true).setValues(new int[]{1, 2, 3, 4, 5})
                        .setDoubles(new double[]{1.6d, 3453.3453534d, 23542332.1123323232d})
                        .setMultidim(new int[][][]{{{0}, {1}}})
                        .setIntegers(List.of(5, 4, 3, 2, 1))
                        .setDoublesMulti(List.of(List.of(1.4, 2.4), List.of(2.7)))
                        .createObjectToTest(),
                new ObjectToTestBuilder().setA(1).setB(1.5d).setC(4.5f).createObjectToTest(),
                new ObjectToTestBuilder().setDoublesMulti(List.of(List.of(1.4, 2.4), List.of(2.7))).createObjectToTest(),
                new ObjectToTestBuilder().setDoubles(null).setDoublesMulti(null).createObjectToTest(),
                new ObjectToTestBuilder().setChars(new char[]{'a', 'b', 'c', '5', '!'}).createObjectToTest()
        ).map(Arguments::of);
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte) 1), Arguments.of((short) 2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new byte[]{1, 2, 3}),
                Arguments.of(new short[]{4, 5, 6}),
                Arguments.of(new int[]{7, 8, 9}),
                Arguments.of(new float[]{10f, 11f, 12f}),
                Arguments.of(new double[]{13d, 14d, 15d}),
                Arguments.of(List.of(16, 17, 18)),
                Arguments.of(Collections.singletonList(19))
        );
    }

    @BeforeEach
    void setUp() {
        gson = new Gson();
        serializer = new ObjectToJsonSerializerImpl();
    }

    @ParameterizedTest
    @MethodSource("processObjectsToTest")
    void shouldCorrectlyConvertObjectToJsonAndParseByGSON(Object object) {
        String json = serializer.toJson(object);
        Object expected = gson.fromJson(json, object.getClass());
        assertThat(object).isEqualTo(expected);
    }

    @Test
    void shouldCorrectlyConvertCollectionToJsonAndParseByGSON() {
        final List<ObjectToTest> object = new ArrayList<>(List.of(new ObjectToTestBuilder().setA(1).createObjectToTest(),
                new ObjectToTestBuilder().setC(1.4f).createObjectToTest()));
        String json = serializer.toJson(object);
        Object expected = gson.fromJson(json, TypeToken.getParameterized(List.class, ObjectToTest.class).getType());
        assertThat(object).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    void customTest(Object o) {
        assertThat(serializer.toJson(o)).isEqualTo(gson.toJson(o));
    }
}