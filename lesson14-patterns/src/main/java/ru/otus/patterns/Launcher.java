package ru.otus.patterns;

import ru.otus.patterns.model.Message;
import ru.otus.patterns.observable.DefaultMessageObservable;
import ru.otus.patterns.observable.MessageHistory;
import ru.otus.patterns.processor.DefaultProcessorFactoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Launcher {
    public static void main(String[] args) throws InterruptedException {
        final DefaultMessageObservable messageObservable = new DefaultMessageObservable();
        final DefaultProcessorFactoryImpl processorFactory = new DefaultProcessorFactoryImpl();
        final MessageHistory messageHistory = new MessageHistory();

        var processors = List.of(processorFactory.createFieldExchanger(),
                processorFactory.createThrowable(message -> message));

        final ComplexProcessor processor = new ComplexProcessor(messageObservable,
                processors,
                processorFactory,
                (message, throwable) -> System.out.printf("%s. Exception for message %s fired. %s%n", LocalDateTime.now(), message, throwable.getMessage()));
        processor.addListener(messageHistory);

        for (int i = 0; i < 100; i++) {
            processor.process(new Message.Builder()
                    .field5("field5-" + i)
                    .field11("field11-" + i)
                    .field13("field13-" + i)
                    .build());
            Thread.sleep(100);
        }

        processor.removeListener(messageHistory);
        System.out.println("messageHistory = " + messageHistory.getReadOnlyHistory().stream()
                .map(Objects::toString)
                .collect(Collectors.joining(System.lineSeparator())));

    }
}
