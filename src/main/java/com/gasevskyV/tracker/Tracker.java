package com.gasevskyV.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Класс Tracker - является оберткой над массивом. Представляет хранилище заявок.
 *
 * @author Vasiliy Gasevskiy (gasevskyv@gmail.com)
 * @version $Id$
 * @since 0.1
 */
enum Tracker implements ITracker {
    INSTANCE;

    public static ITracker getInstance() {
        return INSTANCE;
    }

    /**
     * Массив для хранение заявок.
     */
    private List<Item> items = new ArrayList<>();

    /**
     * Метод реализаущий добавление заявки в хранилище
     *
     * @param item новая заявка
     */
    public Item add(Item item) {
        String id = this.generateId();
        item.setId(id);
        items.add(item);
        return item;
    }

    private static AtomicLong idCounter = new AtomicLong();

    /**
     * Метод генерирует уникальный ключ для заявки.
     * Так как у заявки нет уникальности полей, имени и описание. Для идентификации нам нужен уникальный ключ.
     *
     * @return Уникальный ключ.
     */
    private String generateId() {
        return String.valueOf(idCounter.getAndIncrement());
    }

    /**
     * Метод возвращает копию массива this.items без null элементов;
     *
     * @return Item[].
     */
    public List<Item> findAll() {
        return items;
    }

    /**
     * Метод проверяет в цикле все элементы массива, сравнивая id с аргументом String id и
     * возвращает найденный Item. Если Item не найден - возвращает null.
     *
     * @return Item.
     */
    public Item findById(String id) {
        return items.stream().filter(x -> id.equals(x.getId())).findAny().orElse(null);
    }

    /**
     * Метод должен заменить ячейку в массиве. Для этого необходимо найти
     * ячейку в массиве по id. Метод должен вернуть boolean результат - удалось ли провести операцию.
     *
     * @return boolean.
     */
    public boolean replace(String id, Item item) {
        int index = this.index(id);
        if (index >= 0) {
            item.setId(id);
            items.set(index, item);
        }
        return index >= 0;
    }


    /**
     * Метод должен удалить ячейку в массиве. Для этого необходимо найти
     * ячейку в массиве по id. Метод должен вернуть boolean результат - удалось ли провести операцию.
     *
     * @return boolean.
     */
    public boolean delete(String id) {
        int index = this.index(id);
        if (index >= 0) {
            items.remove(index);
        }
        return index >= 0;
    }

    /**
     * Метод проверяет в цикле все элементы массива, сравнивая name с аргументом метода String key.
     * Элементы, у которых совпадает name, копирует в результирующий массив и возвращает его;
     *
     * @return Item[].
     */
    public List<Item> findByName(String key) {
        return items.stream().filter(x -> key.equals(x.getTask())).collect(Collectors.toCollection(ArrayList::new));
    }

    public void clean() {
        items.clear();
    }

    private int index(String id) {
        int index = -1, i = 0;
        for (Item item : items) {
            if (id.equals(item.getId())) {
                index = i;
                break;
            }
            i++;
        }
        return index;
    }
}

