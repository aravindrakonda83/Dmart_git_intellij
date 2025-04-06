package com.dmart.service;

import com.dmart.model.User;

import java.util.List;

public interface UserService {
    void register(User user);
    User login(String username, String password);

    List<User> findAllUsers();


}
