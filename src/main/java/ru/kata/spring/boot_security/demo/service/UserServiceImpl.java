package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepo;
import ru.kata.spring.boot_security.demo.repositories.UsersRepo;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private UsersRepo usersRepo;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return usersRepo.findAll();
    }
    @Override
    public User findOne(int id) {
        Optional<User> foundUser = usersRepo.findById(id);
        return foundUser.orElse(null);
    }
    @Override
    @Transactional
    public void save(User user) {
        if (getPersonByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepo.save(user);
        }
    }
    @Override
    @Transactional
    public void update(int id, User updatedUser) {
        if (updatedUser.getPassword().equals(findOne(id).getPassword())) {
            updatedUser.setId(id);
            usersRepo.save(updatedUser);
            return;
        }
        updatedUser.setId(id);
        updatedUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        usersRepo.save(updatedUser);
    }
    @Override
    @Transactional
    public void delete(int id) {
        usersRepo.deleteById(id);
    }
    public User getPersonByUsername(String username) {
        Optional<User> user = usersRepo.findByUsername(username);
        return user.orElse(null);
    }
}
//ПОПРАВИТЬ getPersonByUsername