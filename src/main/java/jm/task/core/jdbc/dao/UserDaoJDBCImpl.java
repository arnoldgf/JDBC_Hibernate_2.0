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
            System.out.println(e.getMessage());
            //throw new RuntimeException("Ошибка при создании таблицы 'users': " + e.getMessage(), e);
        }
    }

    public void dropUsersTable() {
        String sqlDropTable = "drop table users";
        try(Statement statement = connection.createStatement())    {

            statement.executeUpdate(sqlDropTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //throw new RuntimeException("Таблица вероятно уже отсутствует и не может быть удалена!", e);
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
                System.out.println("Пользователь " + name + " " + lastName + " спешно создан!");
            } else {
                System.out.println("Не удалось создать пользователя " + name + " " + lastName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //throw new RuntimeException("Ошибка создания пользователя", e);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //throw new RuntimeException("Ошибка удаления пользователя", e);
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
            System.out.println(e.getMessage());
            //throw new RuntimeException("Ошибка удаления пользователя", e);
        }
        return userList != null ? userList : new ArrayList<>();
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //throw new RuntimeException("Ошибка очистки таблицы пользователей", e);
        }
    }
}
