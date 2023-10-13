package ru.kata.spring.boot_security.demo.service;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> index() {
        return userDAO.index();
    }

    @Transactional(readOnly = true)
    public User show(Long id) {
        return userDAO.show(id);
    }

    @Transactional(readOnly = true)
    public User show(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        return show(user.getId());
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Transactional
    public void update(@Valid User updatedUser) {
        User originUser = show(updatedUser.getId());
        originUser.setAge(updatedUser.getAge());
        originUser.setEmail(updatedUser.getEmail());
        originUser.setUsername(updatedUser.getUsername());
        if (!updatedUser.getPassword().equals(originUser.getPassword())) {
            originUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userDAO.update(originUser);
    }

    @Transactional
    public void delete(Long id) {
        userDAO.delete(id);
    }
}
