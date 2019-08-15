package service.impl;

import dao.interfaces.UserDao;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void addUser(String email, String password, String role) {
        userDao.addUser(new User(email, bCryptPasswordEncoder.encode(password), role));
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public boolean isUserExist(String email) {
        return userDao.getUserByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, String email, String password, String role) {
        userDao.updateUser(new User(userId, email, bCryptPasswordEncoder.encode(password), role));
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userDao.removeUser(user);
    }
}
