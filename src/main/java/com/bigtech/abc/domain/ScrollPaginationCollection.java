package com.bigtech.abc.domain;

public class ScrollPaginationCollection {
    private final List<T> itemsWithNextCusor;
    private final int countPerScroll;

    public static <T> ScrollPaginationCollection<T> of(List<T> itemsWithNextCusor, int size) {
        return new ScrollPaginationCollection<>(itemsWithNextCusor, size);
    }

    public boolean isLastScroll() {
        return this.itemsWithNextCusor.size() <= countPerScroll;
    }

    public List<T> getCurrentScrollItems() {
        if (isLastScroll()) {
            return this.itemsWithNextCursor;
        }
        return this.itemsWithNextCursor.subList(0, countPerScroll);
    }

    public T getNextCursor() {
        return itemsWithNextCursor.get(countPerScroll - 1);
    }
}
