package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService, RoleDAO roleDAO) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initializedDataBase() {
        roleService.save(new Role("ADMIN"));
        roleService.save(new Role("USER"));
        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        Set<Role> allRoles = new HashSet<>();
        adminRole.add(roleService.getRoleByName("ADMIN"));
        userRole.add(roleService.getRoleByName("USER"));
        allRoles.add(roleService.getRoleByName("ADMIN"));
        allRoles.add(roleService.getRoleByName("USER"));
        userService.save(new User("Robert", 23, "1filatov1@mail.ru", "Robert", adminRole));
        userService.save(new User("Roman", 22, "Kashapov@bk.ru", "Roman", userRole));
        userService.save(new User("Nikolay", 23, "Chudinov@list.ru", "Nikolay", allRoles));
    }
}
