package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserRepository.
 * 
 * Tests all repository methods with @DataJpaTest and 100% code coverage.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser1 = new User();
        testUser1.setUsername("testuser1");
        testUser1.setPassword("password123");
        testUser1.setEmail("test1@example.com");

        testUser2 = new User();
        testUser2.setUsername("testuser2");
        testUser2.setPassword("password456");
        testUser2.setEmail("test2@example.com");
    }

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {
        User savedUser = userRepository.save(testUser1);

        assertNotNull(savedUser.getId());
        assertEquals(testUser1.getUsername(), savedUser.getUsername());
        assertEquals(testUser1.getEmail(), savedUser.getEmail());
        assertEquals(testUser1.getPassword(), savedUser.getPassword());
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindUserById() {
        User savedUser = userRepository.save(testUser1);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void shouldReturnEmptyWhenUserNotFoundById() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("testuser1")));
        assertTrue(users.stream().anyMatch(user -> user.getUsername().equals("testuser2")));
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        userRepository.save(testUser1);

        Optional<User> foundUser = userRepository.findByUsername("testuser1");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser1", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        userRepository.save(testUser1);

        Optional<User> foundUser = userRepository.findByEmail("test1@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test1@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should check if user exists by username")
    void shouldCheckIfUserExistsByUsername() {
        userRepository.save(testUser1);

        assertTrue(userRepository.existsByUsername("testuser1"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        userRepository.save(testUser1);

        assertTrue(userRepository.existsByEmail("test1@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        User savedUser = userRepository.save(testUser1);
        savedUser.setUsername("updateduser");
        savedUser.setEmail("updated@example.com");

        User updatedUser = userRepository.save(savedUser);

        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        User savedUser = userRepository.save(testUser1);

        userRepository.deleteById(savedUser.getId());

        assertFalse(userRepository.existsById(savedUser.getId()));
    }

    @Test
    @DisplayName("Should check if user exists by ID")
    void shouldCheckIfUserExistsById() {
        User savedUser = userRepository.save(testUser1);

        assertTrue(userRepository.existsById(savedUser.getId()));
        assertFalse(userRepository.existsById(999L));
    }

    @Test
    @DisplayName("Should count users")
    void shouldCountUsers() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        long count = userRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should delete all users")
    void shouldDeleteAllUsers() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        userRepository.deleteAll();

        assertEquals(0, userRepository.count());
    }
}
