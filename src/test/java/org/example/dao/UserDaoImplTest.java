package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("users")
            .withUsername("postgres")
            .withPassword("postgres");

    private final UserDao userDao = new UserDaoImpl();

    @BeforeEach
    void cleanDatabase() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.createMutationQuery("delete from User").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    @DisplayName("Создание пользователя и чтение по id")
    void createAndFindById() {

        User toSave = new User(null, "Anna", "anna@icloud.com", 25, LocalDateTime.now());


        User created = userDao.create(toSave);
        Optional<User> found = userDao.findById(created.getId());


        assertNotNull(created.getId(), "После сохранения id должен быть не null");
        assertTrue(found.isPresent(), "Пользователь должен находиться по id");
        assertEquals("Anna", found.get().getName());
        assertEquals("anna@icloud.com", found.get().getEmail());
        assertEquals(25, found.get().getAge());
    }

    @Test
    @DisplayName("Обновление пользователя: изменяем email и возраст")
    void updateUser() {

        User created = userDao.create(new User(null, "Leila", "leila@icloud.com", 20, LocalDateTime.now()));


        created.setEmail("leila@gmail.com");
        created.setAge(30);
        User updated = userDao.update(created);


        Optional<User> fromDb = userDao.findById(created.getId());
        assertTrue(fromDb.isPresent(), "Пользователь должен существовать после обновления");
        assertEquals("leila@gmail.com", fromDb.get().getEmail());
        assertEquals(30, fromDb.get().getAge());
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void deleteUser() {

        User created = userDao.create(new User(null, "Mimi", "mimi@yandex.ru", 40, LocalDateTime.now()));
        Long id = created.getId();


        userDao.delete(id);


        Optional<User> found = userDao.findById(id);
        assertTrue(found.isEmpty(), "Пользователь должен быть удалён");
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void findAllUsers() {

        userDao.create(new User(null, "Kiki", "kiki@icloud.com", 18, LocalDateTime.now()));
        userDao.create(new User(null, "Mora", "mora@icloud.com", 19, LocalDateTime.now()));

        List<User> users = userDao.findAll();

        assertEquals(2, users.size(), "Должно вернуться два пользователя");
    }
}

