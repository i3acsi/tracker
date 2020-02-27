package com.gasevskyV.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;
import java.util.function.Consumer;


public class TrackerSQL implements ITracker, AutoCloseable {

    private Connection connection;
    private Properties properties = new Properties();

    public TrackerSQL(Connection connection) {
        getProperties("app.properties");
        structureCheck();
        this.connection = connection;

    }

    public TrackerSQL() {
        getProperties("app.properties");
        structureCheck();
    }

    public boolean structureCheck() {
        /*Для считывания файла используется ClassLoader
        Предусмотреть возможность, что структуры в базе еще нет. И вам нужно ее создать при старте.
        Все ресурсы необходимо закрывай через try-with-resources
        В классе трекер появляется новое поле private Connection connection. Его нужно закрывать через AutoCloseable.
        Например
        try (Tracker tracker = new Tracker(config)) {
             // todo actions.
        }
         */
        boolean exists = false;

        String url = properties.getProperty("url").replaceFirst("tracker", "");
        getConnection(url);
        try (PreparedStatement st = connection.prepareStatement("SELECT datname FROM pg_database;");
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                if ("tracker".equals(rs.getString("datname"))) {
                    exists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!exists) {
            try (PreparedStatement st = connection.prepareStatement("CREATE DATABASE tracker;")) {
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        getConnection(properties.getProperty("url"));

        try (PreparedStatement st = connection.prepareStatement(
                "CREATE TABLE if NOT EXISTS trackItem (\n"
                        + "id SERIAL PRIMARY KEY NOT NULL,\n"
                        + "task VARCHAR (100) NOT NULL,\n"
                        + "description VARCHAR (300) NOT NULL,\n"
                        + "date_creation TIMESTAMP NOT NULL,\n"
                        + "comment VARCHAR (300)\n"
                        + ");")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return this.connection != null;
    }

    private void getProperties(String name) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(name)) {
            this.properties.load(is);
            Class.forName(properties.getProperty("driver-class-name"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void getConnection(String url) {
        String username = String.valueOf(this.properties.getProperty("username"));
        String password = String.valueOf(this.properties.getProperty("password"));
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO trackItem (task, description, date_creation) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getTask());
            ps.setString(2, item.getDesk());
            ps.setTimestamp(3, new Timestamp(item.getDateCreation()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getString("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        int result = 0;
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE trackItem SET task = ?, description = ?, date_creation = ? WHERE id = ?;"
        )) {
            ps.setString(1, item.getTask());
            ps.setString(2, item.getDesk());
            ps.setTimestamp(3, new Timestamp(item.getDateCreation()));
            ps.setInt(4, Integer.valueOf(id));
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public boolean delete(String id) {
        int result = 0;
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM trackItem WHERE id = ?"
        )) {
            ps.setInt(1, Integer.valueOf(id));
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT  * FROM trackItem");
             ResultSet set = ps.executeQuery()) {
            while (set.next()) {
                Item item = new Item(set.getString("task"),
                        set.getString("description"));
                item.setId(set.getString("id"));
                item.setDateCreation(set.getTimestamp("date_creation").getTime());
                item.setComments(set.getString("comment"));
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT  * FROM trackItem WHERE task =?")) {
            ps.setString(1, key);
            try (ResultSet set = ps.executeQuery()) {
                while (set.next()) {
                    Item item = new Item(set.getString("task"),
                            set.getString("description"));
                    item.setId(set.getString("id"));
                    item.setDateCreation(set.getTimestamp("date_creation").getTime());
                    item.setComments(set.getString("comment"));
                    result.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Item findById(String id) {
        Item result = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT  * FROM trackItem WHERE id =?")) {
            ps.setInt(1, Integer.valueOf(id));
            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    result = new Item(set.getString("task"),
                            set.getString("description"));
                    result.setId(set.getString("id"));
                    result.setDateCreation(set.getTimestamp("date_creation").getTime());
                    result.setComments(set.getString("comment"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void clean() {
        try (PreparedStatement ps = connection.prepareStatement(
                "TRUNCATE TABLE trackItem RESTART IDENTITY;")) {
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Main extends StartUI {
    /**
     * Конструктор с инициалицацией полей.
     *
     * @param input   ввод данных.
     * @param tracker хранилище заявок.
     * @param output
     */
    public Main(Input input, ITracker tracker, Consumer<String> output) {
        super(input, tracker, output);
    }

    public static void main(String[] args) {
        TrackerSQL trackerSQL = new TrackerSQL();
        new Main(new ValidateInput(
                new ConsoleInput()),
                trackerSQL,
                System.out::println).
                init();
    }
}