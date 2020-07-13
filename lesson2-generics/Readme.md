Упрощённая имплементация интерфейса {@link List} на базе массива. Имплементация не оптимизирована
и предназначена исключительно для проверки работы {@link Collections#copy(List, List)},
{@link Collections#sort(List, Comparator)} и {@link Collections#addAll(Collection, Object[])}.

Проверка реализации в тесте [DIYArrayListTest](/src/test/java/ru/otus/study/DIYArrayListTest.java)