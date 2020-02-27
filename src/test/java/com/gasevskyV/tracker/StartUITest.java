package com.gasevskyV.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test.
 *
 * @author Vasiliy Gasevskiy (gasevskyv@gmail.com)
 * @version $Id$
 * @since 0.1
 */

public class StartUITest {
    private final String ln = System.lineSeparator();
    private final String menu = ("0. Add new Item." + ln
            + "1. Find all items." + ln
            + "2. Edit Item." + ln
            + "3. Delete Item." + ln
            + "4. Find Item by ID." + ln
            + "5. Find Items by Name." + ln
            + "6. Comment." + ln
            + "7. Exit." + ln);
    private final ITracker tracker = Tracker.getInstance();
    private final Item[] item = {tracker.add(new Item("test name", "desc")),
            tracker.add(new Item("test name", "desc2")),
            tracker.add(new Item("test name 3", "desc3"))};
    private final String[] id = {item[0].getId(), item[1].getId(), item[2].getId()};
    private final PrintStream stdout = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final Consumer<String> output = new Consumer<String>() {
        private final PrintStream stdout = new PrintStream(out);

        @Override
        public void accept(String s) {
            stdout.println(s);
        }
    };

    @Before
    public void loadOutput() {
        System.out.println("execute before method");
        System.setOut(new PrintStream(this.out));
    }

    @After
    public void backOutput() {
        tracker.clean();
        System.setOut(this.stdout);
        System.out.println("execute after method");
    }

    /**
     * Тест поиска заявки по ID.
     */
    @Test
    public void whenFindByIDThenItemShown() {
        Input input = new StubInput(new String[]{"4", id[0], "y"});
        new StartUI(input, tracker, output).init();
        assertThat(this.out.toString(),
                is(
                        new StringBuilder()
                                .append(menu)
                                .append("----------Поиск заявки по ID----------" + ln)
                                .append("Найдена заявка " + item[0].show() + ln)
                                .toString()
                )
        );

    }

    /**
     * Тест поиска заявок по названию задачи.
     */
    @Test
    public void whenFindTaskThenItemsShown() {
        Input input = new StubInput(new String[]{"5", "test name", "y"});
        new StartUI(input, tracker, output).init();
        assertThat(this.out.toString(),
                is(
                        new StringBuilder()
                                .append(menu)
                                .append("----------Поиск заявок по названию----------" + ln)
                                .append("Найденные заявки: " + ln)
                                .append(item[0].show() + ln)
                                .append(item[1].show() + ln)
                                .toString()
                )
        );
    }

    /**
     * Тест отображения всех заявок.
     */
    @Test
    public void whenShowThenAllItemsShown() {
        Input input = new StubInput(new String[]{"1", "y"});
        new StartUI(input, tracker, output).init();
        assertThat(
                this.out.toString(),
                is(
                        new StringBuilder()
                                .append(menu)
                                .append("----------Все существующие заявки---------" + ln)
                                .append(item[0].show() + ln)
                                .append(item[1].show() + ln)
                                .append(item[2].show() + ln)
                                .append("------------------------------------------" + ln)
                                .toString()
                )
        );
    }

    /**
     * Тест добавления нового элемнета.
     */
    @Test
    public void whenUserAddItemThenTrackerHasNewItemWithSameName() {
        Input input = new StubInput(new String[]{"0", "test name0", "desc", "y"});
        new StartUI(input, tracker, output).init();
        assertThat(tracker.findAll().size(), is(4));
        assertThat(tracker.findAll().get(3).getTask(), is("test name0"));
    }


    /**
     * Тест редактирования заявки.
     */
    @Test
    public void whenUpdateThenTrackerHasUpdatedValue() {
        Input input = new StubInput(new String[]{"2", id[0], "test replace", "заменили заявку", "y"});
        new StartUI(input, tracker, output).init();
        assertThat(tracker.findById(id[0]).getTask(), is("test replace"));
    }

    /**
     * Тест удаления заявки.
     */
    @Test
    public void whenDeleteThenTrackerHasNoValue() {
        Input input = new StubInput(new String[]{"3", id[2], "y"});
        new StartUI(input, tracker, output).init();
        assertThat(tracker.findAll().size(), is(2));
    }

    /**
     * Тест комментария к заявке.
     */
    @Test
    public void whenCommentThenItemHasComment() {
        Input input = new StubInput(new String[]{"6", id[0], "comment", "y"});
        new StartUI(input, tracker, output).init();
        assertThat(tracker.findById(id[0]).getComments(), is("comment"));
    }
}
