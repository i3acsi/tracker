package com.gasevskyV.tracker;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test.
 *
 * @author Vasiliy Gasevskiy (gasevskyv@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class TrackerTest {
    private final ITracker tracker = Tracker.getInstance();

    @After
    public void backOutput() {
        tracker.clean();
    }

    /**
     * Test adding a new element.
     * If this element appears in the array, the test is passed.
     */
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        Item item = new Item("test1", "testDescription");
        tracker.add(item);
        assertThat(tracker.findAll().get(0), is(item));
    }

    /**
     * Test of cell replacement.
     * If new element appears in the array instead of previous and method returns true, the test is passed.
     */
    @Test
    public void whenReplaceNameThenReturnNewName() {
        Item previous = new Item("test1", "testDescription");
        tracker.add(previous);
        String id = previous.getId();
        Item next = new Item("test2", "testDescription2");
        next.setId(id);
        tracker.replace(id, next);
        assertThat(tracker.findById(previous.getId()).getTask(), is("test2"));
    }

    /**
     * Test to find items by name.
     * If elements with right name are existing in the array, the resulting array will contain elements with this name.
     */
    @Test
    public void whenFindByNameThenReturnArrayOfItems() {
        Item[] ex = {
                tracker.add(new Item("test1", "testDescription")),
                tracker.add(new Item("test1", "testDescription"))
        };
        Item item3 = new Item("test2", "testDescription");
        tracker.add(item3);
        assertThat(tracker.findByName("test1"), is(Arrays.asList(ex)));
    }

    /**
     * Test to find items by name.
     * If there no elements with right name in the array, the resulting array will have zero length.
     */
    @Test
    public void whenFindByNameThenNoItems() {
        Item item1 = new Item("test1", "testDescription");
        tracker.add(item1);
        assertThat(tracker.findByName("test").size(), is(0));
    }

    /**
     * Test of deleting an item.
     * When deleting an item, the resulting array have no such element and method returns true.
     */
    @Test
    public void whenDeleteReturnTrue() {
        Item item1 = new Item("test1", "testDescription");
        Item item2 = new Item("test1", "testDescription");
        Item item3 = new Item("test2", "testDescription");
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        assertThat(tracker.delete(tracker.findByName("test1").get(0).getId()), is(true));
        assertThat(tracker.findAll().get(0), is(item2));
        assertThat(tracker.findAll().get(1), is(item3));
        assertThat(tracker.findAll().size(), is(2));
    }

    /**
     * Test of deleting non existing item.
     * When deleting non existing item, then returns false.
     */
    @Test
    public void whenDeleteReturnFalse() {
        Item item1 = new Item("test1", "testDescription");
        Item item2 = new Item("test1", "testDescription");
        tracker.add(item1);
        tracker.add(item2);
        String id = item2.getId();
        tracker.delete(id);
        assertThat(tracker.delete(id), is(false));
    }

    /**
     * Test of searching non null elements.
     * Resulting array have only initialized items.
     */
    @Test
    public void whenFindAllThenReturnArrayOfItemsWithoutNull() {
        Item item1 = new Item("test1", "testDescription");
        Item item2 = new Item("test1", "testDescription");
        Item item3 = new Item("test2", "testDescription");
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        assertThat(tracker.findAll().size(), is(3));
        assertThat(tracker.findAll().get(0), is(item1));
        assertThat(tracker.findAll().get(1), is(item2));
        assertThat(tracker.findAll().get(2), is(item3));
    }

    /**
     * Test of searching elements by Id.
     * The resulting item will have the required ID.
     */
    @Test
    public void whenFindByIdThenOnExistReturnItem() {
        Item item1 = new Item("test1", "testDescription");
        Item item2 = new Item("test1", "testDescription");
        Item item3 = new Item("test2", "testDescription");
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        assertThat(tracker.findById(item1.getId()), is(item1));
    }

    /**
     * Test of searching non existing item.
     * If there's no such elements, the result will be null.
     */
    @Test
    public void whenFindByIdThenIfNotExistReturnNull() {
        Item item1 = new Item("test1", "testDescription");
        Item item2 = new Item("test1", "testDescription");
        Item item3 = new Item("test2", "testDescription");
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        String id = item2 .getId();
        tracker.delete(id);
        assertThat((tracker.findById(id) == null), is(true));
    }
}