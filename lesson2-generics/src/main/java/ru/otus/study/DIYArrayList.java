package ru.otus.study;

import java.util.*;

/**
 * Упрощённая имплементация интерфейса {@link List} на базе массива. Имплементация не оптимизирована
 * и предназначена исключительно для проверки работы {@link Collections#copy(List, List)},
 * {@link Collections#sort(List, Comparator)} и {@link Collections#addAll(Collection, Object[])}.
 *
 * @param <E> type of elements to store
 */
public class DIYArrayList<E> implements List<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public DIYArrayList() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new DIYArrayIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length)
            elements = increase(size + 1);
        elements[size++] = e;
        return true;
    }

    private E[] increase(int newSize) {
        return Arrays.copyOf(elements, newSize);
    }

    @Override
    public boolean remove(Object o) {
        final int index = indexOf(o);
        if (index > -1) {
            final E[] data = elements;
            innerRemoveByIndex(index, data);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        final Collection<? extends E> addedCollection = Objects.requireNonNull(c, "Collection cannot be null");
        if (addedCollection.isEmpty())
            return false;
        final Object[] objects = addedCollection.toArray();
        final int newSize = size + addedCollection.size();
        final E[] data = Arrays.copyOf(elements, newSize + 1);
        System.arraycopy(objects, 0, data, size, objects.length);
        size = newSize;
        elements = data;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        final E[] data = elements;
        int oldSize = size;
        for (int i = 0; i < oldSize; i++, size--)
            data[i] = null;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        final E prevElement = elements[index];
        elements[index] = element;
        return prevElement;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForBounds(index);
        final int s = size;
        E[] data = this.elements;
        if (size == data.length)
            data = increase(s + 1);
        System.arraycopy(data, index, data, index + 1, s - index);
        data[index] = element;
        size = s + 1;
    }

    private void checkIndexForBounds(int index) {
        if (index < 0 || index > size)
            throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public E remove(int index) {
        checkIndexForBounds(index);
        E[] data = this.elements;
        final E element = data[index];
        innerRemoveByIndex(index, data);
        return element;
    }

    private void innerRemoveByIndex(int index, E[] data) {
        final int newSize = size - 1;
        if (newSize - index >= 0)
            System.arraycopy(data, index + 1, data, index, newSize - index);
        data[newSize] = null;
        size = newSize;
    }

    @Override
    public int indexOf(Object o) {
        final E[] data = elements;
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        final E[] data = elements;
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYArrayListIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new DIYArrayListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class DIYArrayIterator implements Iterator<E> {
        int position;
        int lastPosition = -1;

        DIYArrayIterator() {
        }

        @Override
        public boolean hasNext() {
            return position != size;
        }

        @Override
        public E next() {
            int pos = position;
            if (pos >= size)
                throw new NoSuchElementException();
            final E[] data = DIYArrayList.this.elements;
            if (pos >= data.length)
                throw new ConcurrentModificationException();
            position = pos + 1;
            lastPosition = pos;
            return data[lastPosition];
        }

        @Override
        public void remove() {
            if (lastPosition < 0)
                throw new IllegalStateException();
            try {
                DIYArrayList.this.remove(lastPosition);
                position = lastPosition;
                lastPosition = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

    }

    private class DIYArrayListIterator extends DIYArrayIterator implements ListIterator<E> {
        public DIYArrayListIterator(int position) {
            super();
            this.position = position;
        }

        @Override
        public boolean hasPrevious() {
            return position != 0;
        }

        @Override
        public E previous() {
            int pos = position - 1;
            if (pos < 0)
                throw new NoSuchElementException();
            final E[] data = DIYArrayList.this.elements;
            if (pos >= data.length)
                throw new ConcurrentModificationException();
            position = pos;
            lastPosition = pos;
            return data[lastPosition];
        }

        @Override
        public int nextIndex() {
            return position;
        }

        @Override
        public int previousIndex() {
            return position - 1;
        }

        public void set(E e) {
            if (lastPosition < 0)
                throw new IllegalStateException();
            try {
                DIYArrayList.this.set(lastPosition, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            try {
                int pos = position;
                DIYArrayList.this.add(pos, e);
                position = pos + 1;
                lastPosition = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
