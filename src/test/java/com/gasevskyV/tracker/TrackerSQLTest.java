package com.gasevskyV.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TrackerSQLTest {
    private static TrackerSQL sql =  new TrackerSQL();
    private static Item item = new Item("task", "desc");;

    @Before
    public void init() {
            assertThat(sql.structureCheck(), is(true));
            sql.clean();
            sql.add(item);
    }

    @After
    public void close() {
        sql.close();
    }

    @Test
    public void checkAddAndFindAll() {
        sql.findAll().stream().peek(x -> assertThat(x.getDesk(), is("desc"))).
                forEach(x -> assertThat(x.getTask(), is("task")));
    }

    @Test
    public void checkReplace() {
            Item newItem = new Item("new_task", "new_desc");
            assert (sql.replace("1", newItem));
            assert (!sql.replace("2", newItem));
            sql.findAll().stream().peek(x -> assertThat(x.getDesk(), is("new_desc"))).
                    forEach(x -> assertThat(x.getTask(), is("new_task")));
    }

    @Test
    public void checkDelete() {
            assert (sql.delete("1"));
            assert (!sql.delete("2"));
            assert (sql.findAll().size() == 0);
    }

    @Test
    public void checkFindByName() {
            sql.findByName("task").forEach(x->assertThat(x.getDesk(), is("desc")));
    }

    @Test
    public void checkFindById() {
            Item result = sql.findById("1");
            assertThat(result.getTask(), is("task"));
            assertThat(result.getDesk(), is("desc"));
    }

}