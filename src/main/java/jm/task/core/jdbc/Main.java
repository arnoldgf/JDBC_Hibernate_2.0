package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {

    public static void main(String[] args) {
        Util.getConnection();
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Лупа", "Пупкин", (byte) 20);
        userService.saveUser("Гозя", "Рэпчик", (byte) 25);
        userService.saveUser("Вася", "Сливайся", (byte) 31);
        userService.saveUser("Дима", "Клиторенко", (byte) 38);

        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }
        userService.removeUserById(1);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
