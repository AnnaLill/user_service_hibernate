package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.dto.CreateUserDto;
import org.example.dto.UpdateUserDto;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this(new UserDaoImpl());
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(CreateUserDto dto) {
        dto.validate();
        User user = new User(null, dto.getName(), dto.getEmail(), dto.getAge(), null);
        return userDao.create(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User update(Long id, UpdateUserDto dto) {
        dto.validate();
        User user = userDao.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        if (userDao.findById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userDao.delete(id);
    }
}
