package ru.otus.json.serializer;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.json.models.ObjectToTest;
import ru.otus.json.models.ObjectToTestBuilder;

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
                new ObjectToTestBuilder().setDoubles(null).setDoublesMulti(null).createObjectToTest()
        ).map(Arguments::of);
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
}