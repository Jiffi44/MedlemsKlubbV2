package com.medlemsklubb.data;
import com.medlemsklubb.head.Item;
import java.util.*;
import java.util.stream.Collectors;

public class Invetory {
    private final List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
    }
    public Optional<Item> findById(String itemId) {
        for (Item item : items)
            if (item.itemId().equals(itemId)) {
                return Optional.of(item);
            }
        return Optional.empty();
    }
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }
    public boolean removeById(String id) {
        return items.removeIf(item -> item.itemId().equals(id));
    }


    }

