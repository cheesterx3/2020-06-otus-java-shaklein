package ru.otus.bytecodes;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.otus.bytecodes.impl.DefaultObjectCreatorImpl;
import ru.otus.bytecodes.objects.TestContainer;
import ru.otus.bytecodes.objects.TestContainerImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ObjectCreatorTest {
    private TestContainer container;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(ObjectCreatorTest.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }

    @Setup
    public void setUp() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        container = new DefaultObjectCreatorImpl().createObject(TestContainerImpl.class,TestContainer.class);
    }

    //@Benchmark
    //@Measurement(iterations = 10)
    public void testSumBench() {
        container.sum(5, 3);
    }

    @Benchmark
    @Measurement(iterations = 50)
    public void testCalcBench() {
        container.calculate(5, 3);
    }
}