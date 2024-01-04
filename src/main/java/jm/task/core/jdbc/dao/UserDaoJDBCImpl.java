package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255)," +
                "last_name VARCHAR(255)," +
                "age TINYINT)";
        try (Statement createTableStatement = connection.createStatement()) {
            createTableStatement.executeUpdate(createTable);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users";
        try (Statement createTableStatement = connection.createStatement()) {
            createTableStatement.executeUpdate(dropTable);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertUser = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
        try (PreparedStatement insertUserStatement = connection.prepareStatement(insertUser)) {
            insertUserStatement.setString(1, name);
            insertUserStatement.setString(2, lastName);
            insertUserStatement.setByte(3, age);
            insertUserStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String deleteUser = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUser)) {
            deleteUserStatement.setLong(1, id);
            deleteUserStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectAll = "SELECT * FROM users";
        try (Statement selectAllStatement = connection.createStatement();
             ResultSet resultSet = selectAllStatement.executeQuery(selectAll)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return userList;
    }

    public void cleanUsersTable() {
        String truncateTable = "TRUNCATE TABLE users";
        try (Statement createTableStatement = connection.createStatement()) {
            createTableStatement.execute(truncateTable);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
