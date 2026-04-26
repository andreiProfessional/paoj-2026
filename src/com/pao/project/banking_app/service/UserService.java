package com.pao.project.banking_app.service;

import com.pao.project.banking_app.exception.UserNotFoundException;
import com.pao.project.banking_app.model.user.*;

import java.util.*;

public class UserService {

    private static UserService instance;

    private final List<User> users = new ArrayList<>();
    private final Map<String, User> usersById = new HashMap<>();

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (usersById.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with id '" + user.getId() + "' already exists.");
        }
        users.add(user);
        usersById.put(user.getId(), user);
    }

    public void removeUser(String id) {
        User user = findById(id);
        users.remove(user);
        usersById.remove(id);
    }

    public User findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }
        User user = usersById.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public List<User> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        String lower = name.toLowerCase();
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (getDisplayName(user).toLowerCase().contains(lower)) {
                result.add(user);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<User> listAll() {
        return Collections.unmodifiableList(users);
    }

    private String getDisplayName(User user) {
        if (user instanceof Person p) {
            return p.getFullName();
        }
        if (user instanceof AuthorizedIndividual a) {
            return a.getFullName();
        }
        if (user instanceof Company c) {
            return c.getCompanyName();
        }
        if (user instanceof Institution i) {
            return i.getInstitutionName();
        }
        return user.getId();
    }
}
