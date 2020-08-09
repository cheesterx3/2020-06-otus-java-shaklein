package ru.otus.bytecodes;

import ru.otus.bytecodes.impl.DefaultObjectCreatorImpl;
import ru.otus.bytecodes.objects.TestContainer;
import ru.otus.bytecodes.objects.TestContainerImpl;

public class Launcher {
    public static void main(String[] args) throws InstantiationException {
        final ObjectCreator objectCreator = new DefaultObjectCreatorImpl();
        TestContainer container = objectCreator.createObject(TestContainerImpl.class, TestContainer.class);
        container.calculate();
        container.calculate(1);
        container.calculate(3, "433", (byte) 5);
        container.calculate(5, (byte) 5, "5344333");
        container.otherMethod("");
        container.sum(3, 4);
        System.out.println("-----------------");
        System.out.println("Calc result is: " + container.calculate(2, 3));
        System.out.println("-----------------");
        container.calculate(1, (byte) 1, null);
    }
}
