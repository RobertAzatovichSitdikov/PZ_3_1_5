package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    private RoleDAO roleDAO;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleDAO roleDAO) {
        this.roleRepository = roleRepository;
        this.roleDAO = roleDAO;
    }

    @Transactional
    public Set<Role> getAllRoles() {
        return roleRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Transactional
    public Role findByRoleId(Long id) {
        return roleRepository.getById(id);
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public Role getRoleByName(String name) {
        return roleDAO.getRoleByName(name);
    }
}
