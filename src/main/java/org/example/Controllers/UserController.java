package org.example.Controllers;

import org.example.Models.User;
import org.example.Services.UserService;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }


    public boolean deleteUser(int id) {
        return userService.deleteUser(id);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userService.changePassword(userId, newPassword);
    }
}
