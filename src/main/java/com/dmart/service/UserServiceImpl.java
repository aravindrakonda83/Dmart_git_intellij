package com.dmart.service;

import com.dmart.dao.UserDAO;
import com.dmart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void register(User user) {
        user.setRole("USER");
        user.setEnabled(true);
        userDAO.saveUser(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return userDAO.findAllUsers();
    }


}
