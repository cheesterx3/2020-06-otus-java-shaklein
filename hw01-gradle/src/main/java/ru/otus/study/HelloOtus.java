package ru.otus.study;

import com.google.common.collect.ImmutableList;

import java.util.stream.Collectors;

public class HelloOtus {
    public static void main(String[] args) {
        final ImmutableList<Integer> list = ImmutableList.copyOf(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        System.out.println(list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
    }
}
