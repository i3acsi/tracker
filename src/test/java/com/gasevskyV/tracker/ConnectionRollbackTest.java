package com.gasevskyV.tracker;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ConnectionRollbackTest {


    public Connection init() {
        try (InputStream in = TrackerSQL.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);

        }
    }

    @Test
    public void whenCreateNewItemThanTrue() throws SQLException {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            tracker.clean();
            tracker.add(new Item("task", "desc"));
            assertThat(tracker.findByName("task").size(), is(1));
        }
    }

    @Test
    public void whenAddTwoItemsThanGetAllIsTwo() throws SQLException {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item temp = tracker.add(new Item("task", "desc"));
            assertThat(tracker.findById(temp.getId()), is(temp));
        }
    }

    @Test
    public void whenReplaceThanTrue() throws SQLException {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item item = tracker.add(new Item("task", "desc"));
            Item newItem = new Item("newTask", "descripted");
            tracker.replace(item.getId(), newItem);
            assertThat(tracker.findById(item.getId()).getTask(), is(newItem.getTask()));
        }
    }

    @Test
    public void whenAddAndDeleteThanTrue() throws SQLException {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item item = tracker.add(new Item("task", "desc"));
            assertTrue(tracker.delete(item.getId()));
        }
    }

    @Test
    public void whenDeleteNotExistedThanFalse() throws SQLException {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            assert (!tracker.delete("200"));
        }
    }

}