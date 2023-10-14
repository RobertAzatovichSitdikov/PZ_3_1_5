package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Role getRoleByName(String name) {
        Role role = null;
        try {
            role = getEntityManager()
                    .createQuery("SELECT r FROM Role r WHERE r.name=:role", Role.class)
                    .setParameter("role", name)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Роли с таким именем не существует");
        }
        return role;
    }

}
