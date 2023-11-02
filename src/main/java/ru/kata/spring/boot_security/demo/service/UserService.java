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
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    @Autowired
    public UserService(UserDAO userDAO, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDAO = userDAO;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
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
    public void save(User user, String userRole, String adminRole) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (userRole != null && userRole.equals("USER")) {
            roles.add(roleService.getRoleByName("USER"));
        }
        if (adminRole != null && adminRole.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        user.setRoles(roles);

        userDAO.save(user);
    }

    @Transactional
    public void save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDAO.save(user);
    }

    @Transactional
    public void update(@Valid User updatedUser, String userRole, String adminRole) {
        Set<Role> roles = new HashSet<>();
        if (userRole != null && userRole.equals("USER")) {
            roles.add(roleService.getRoleByName("USER"));
        }
        if (adminRole != null && adminRole.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        updatedUser.setRoles(roles);

        User originUser = show(updatedUser.getId());
        originUser.setAge(updatedUser.getAge());
        originUser.setEmail(updatedUser.getEmail());
        originUser.setUsername(updatedUser.getUsername());
        originUser.setRoles(updatedUser.getRoles());
        if (!updatedUser.getPassword().equals(originUser.getPassword())) {
            originUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userDAO.update(originUser);
    }

    @Transactional
    public void update(@Valid User updatedUser) {
        User originUser = show(updatedUser.getId());
        originUser.setAge(updatedUser.getAge());
        originUser.setEmail(updatedUser.getEmail());
        originUser.setUsername(updatedUser.getUsername());
        originUser.setRoles(updatedUser.getRoles());
        if (!updatedUser.getPassword().equals(originUser.getPassword())) {
            originUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userDAO.update(originUser);
    }

    @Transactional
    public void delete(Long id) {
        userDAO.delete(id);
    }

    @Transactional
    public void getModelRoles(Long id, Model model) {
        Set<Role> roles = show(id).getRoles();
        for (Role role : roles) {
            if (role.equals(roleService.getRoleByName("ADMIN"))) {
                model.addAttribute("ADMIN", true);
            }
            if (role.equals(roleService.getRoleByName("USER"))) {
                model.addAttribute("USER", true);
            }
        }
    }
}
