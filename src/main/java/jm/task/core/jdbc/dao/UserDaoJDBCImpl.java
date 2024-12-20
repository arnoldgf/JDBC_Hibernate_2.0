package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    lastName VARCHAR(50) NOT NULL,
                    age TINYINT NOT NULL
                );
                """;
        try(Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlCreateTable);
            System.out.println("Таблица 'users' создана или уже существует!");
        } catch (SQLException e) {
            System.err.println("Исключение при создании таблицы: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        String sqlDropTable = "drop table users";
        try(Statement statement = connection.createStatement())    {

            statement.executeUpdate(sqlDropTable);
        } catch (SQLException e) {
            System.err.println("Исключение при удалении таблицы: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlAddUser = "insert into users (name, lastName, age) values (?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sqlAddUser)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            int status = preparedStatement.executeUpdate();
            if (status > 0) {
                System.out.println("Пользователь " + name + " " + lastName + " успешно создан!");
            } else {
                System.out.println("Не удалось создать пользователя " + name + " " + lastName);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка создания пользователя: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String sqlDeleteUserId = "DELETE FROM users WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUserId)) {

            preparedStatement.setLong(1, id);
            int status = preparedStatement.executeUpdate();
            if (status > 0) {
                System.out.println("Пользователь с ID " + id + " был успешно удалён!");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден!");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления пользователя" + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        String sqlGetUsers = "select * from users";
        List<User> userList = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetUsers)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления пользователя" + e.getMessage());
        }
        return userList;
    }

    public void cleanUsersTable() {
        String sqlDeleteUsers = "delete from users";
        try (Statement statement = connection.createStatement()) {

            int status = statement.executeUpdate(sqlDeleteUsers);
            if (status > 0) {
                System.out.println("Таблица пользователей очищена!");
            } else {
                System.out.println("Не удалось очистить таблицу пользователей.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка очистки таблицы пользователей" + e.getMessage());
        }
    }
}
