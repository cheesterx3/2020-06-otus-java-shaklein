package ru.otus.study;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DIYArrayListTest {

    private static Stream<Arguments> provideItemsCollection() {
        return Stream.of(
                Arguments.of(LongStream.range(0, 500).boxed().collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().map(value -> "val_" + value).collect(Collectors.toList()))
        );
    }

    private static Stream<Arguments> provideItemsForCopyCollection() {
        return Stream.of(
                Arguments.of(LongStream.range(0, 500).boxed().collect(Collectors.toList()), LongStream.range(500, 1000).boxed().collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().collect(Collectors.toList()), IntStream.range(500, 1000).boxed().collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().map(value -> "val_" + value).collect(Collectors.toList()),
                        IntStream.range(500, 1000).boxed().map(value -> "val_" + value).collect(Collectors.toList()))
        );
    }

    private static Stream<Arguments> provideItemsForSortCollection() {
        return Stream.of(
                Arguments.of(LongStream.range(0, 500).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList())),
                Arguments.of(IntStream.range(0, 500).boxed().sorted(Comparator.reverseOrder()).map(value -> "val_" + value).collect(Collectors.toList()))
        );
    }

    @ParameterizedTest
    @MethodSource("provideItemsCollection")
    void shouldCallCollectionsAddAllProperly(Collection<?> source) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        Collections.addAll(list, source.toArray());
        assertThat(list.size()).isEqualTo(source.size());
        assertThat(list.containsAll(source)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideItemsForSortCollection")
    void shouldCallCollectionsSortProperly(Collection<? extends Comparable<? super Object>> source) {
        final DIYArrayList<Comparable<? super Object>> list = new DIYArrayList<>();
        list.addAll(source);
        Collections.sort(list, Comparator.naturalOrder());
        assertThat(list).isSorted();
    }

    @ParameterizedTest
    @MethodSource("provideItemsForCopyCollection")
    void shouldCallCollectionsCopyProperly(Collection<?> source, List<?> copySource) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        Collections.addAll(list, source.toArray());
        assertThat(list.size()).isEqualTo(source.size());
        Collections.copy(list, copySource);
        assertThat(list.containsAll(copySource)).isTrue();
    }

    @Test
    void shouldAddElementsProperly() {
        final DIYArrayList<Long> longDIYArrayList = new DIYArrayList<>();
        assertThat(longDIYArrayList.size()).isEqualTo(0);
        assertThat(longDIYArrayList.isEmpty()).isTrue();

        longDIYArrayList.add(1L);
        longDIYArrayList.add(2L);
        longDIYArrayList.add(3L);
        assertThat(longDIYArrayList.indexOf(1L)).isEqualTo(0);
        assertThat(longDIYArrayList.indexOf(2L)).isEqualTo(1);
        assertThat(longDIYArrayList.indexOf(3L)).isEqualTo(2);
        assertThat(longDIYArrayList.size()).isEqualTo(3);
        assertThat(longDIYArrayList.contains(1L)).isTrue();
        assertThat(longDIYArrayList.contains(100L)).isFalse();
        assertThat(longDIYArrayList.isEmpty()).isFalse();

        longDIYArrayList.add(1, 4L);
        assertThat(longDIYArrayList.indexOf(4L)).isEqualTo(1);
        assertThat(longDIYArrayList.indexOf(2L)).isEqualTo(2);
        assertThat(longDIYArrayList.indexOf(3L)).isEqualTo(3);
        assertThat(longDIYArrayList.size()).isEqualTo(4);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> longDIYArrayList.add(-1, 5L));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> longDIYArrayList.add(8, 5L));
    }

    @Test
    void shouldRemoveElementsProperly() {
        final DIYArrayList<Long> longDIYArrayList = new DIYArrayList<>();
        longDIYArrayList.add(1L);
        longDIYArrayList.add(2L);
        longDIYArrayList.add(3L);

        final boolean removedContained = longDIYArrayList.remove(1L);
        assertThat(removedContained).isTrue();
        assertThat(longDIYArrayList.contains(1L)).isFalse();
        assertThat(longDIYArrayList.size()).isEqualTo(2);
        assertThat(longDIYArrayList.indexOf(2L)).isEqualTo(0);

        final boolean removedMissing = longDIYArrayList.remove(10L);
        assertThat(removedMissing).isFalse();

        final Long existingValue = longDIYArrayList.remove(0);
        assertThat(existingValue)
                .isNotNull()
                .isEqualTo(2L);
        assertThat(longDIYArrayList.contains(2L)).isFalse();
        assertThat(longDIYArrayList.size()).isEqualTo(1);
        assertThat(longDIYArrayList.indexOf(3L)).isEqualTo(0);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> longDIYArrayList.remove(10));
    }

    @ParameterizedTest
    @MethodSource("provideItemsCollection")
    void shouldClearElementsProperly(Collection<?> source) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        Collections.addAll(list, source.toArray());
        assertThat(list.size()).isEqualTo(source.size());

        list.clear();
        assertThat(list.size()).isEqualTo(0);
        assertThat(list.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideItemsForCopyCollection")
    void shouldCallAddAllProperly(Collection<?> source, Collection<?> newCollection) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        Collections.addAll(list, source.toArray());
        list.addAll(newCollection);
        assertThat(list.size()).isEqualTo(source.size() + newCollection.size());
        assertThat(list.containsAll(source)).isTrue();
        assertThat(list.containsAll(newCollection)).isTrue();

        assertThrows(NullPointerException.class, () -> list.addAll(null));
    }

    @Test
    void shouldThrowUnsupportedExceptionForNotImplementedMethods() {
        final DIYArrayList<Integer> intList = new DIYArrayList<>();
        assertThrows(UnsupportedOperationException.class, () -> intList.toArray(new Integer[0]));
        assertThrows(UnsupportedOperationException.class, () -> intList.addAll(1, Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> intList.removeAll(Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> intList.retainAll(Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> intList.subList(1, 2));
    }

    @ParameterizedTest
    @MethodSource("provideItemsCollection")
    void shouldListIteratorWorkProperly(Collection<?> source) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        list.addAll(source);

        int count = 0;
        final ListIterator<?> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            assertThat(listIterator.next()).isNotNull();
            assertThat(listIterator.nextIndex()).isEqualTo(count + 1);
            assertThat(listIterator.previousIndex()).isEqualTo(count);
            count++;
        }
        assertThat(count).isEqualTo(source.size());
    }

    @ParameterizedTest
    @MethodSource("provideItemsCollection")
    void shouldListIteratorReversedWorkProperly(Collection<?> source) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        list.addAll(source);
        int count = 0;
        final ListIterator<?> listReversedIterator = list.listIterator(source.size());
        while (listReversedIterator.hasPrevious()) {
            assertThat(listReversedIterator.previous()).isNotNull();
            count++;
        }
        assertThat(count).isEqualTo(source.size());
    }

    @ParameterizedTest
    @MethodSource("provideItemsCollection")
    void shouldRemoveInListIteratorWorkProperly(Collection<?> source) {
        final DIYArrayList<Object> list = new DIYArrayList<>();
        list.addAll(source);

        final ListIterator<?> iteratorForRemove = list.listIterator();
        while (iteratorForRemove.hasNext()) {
            iteratorForRemove.next();
            iteratorForRemove.remove();
        }
        assertThat(list.isEmpty()).isTrue();
    }


}