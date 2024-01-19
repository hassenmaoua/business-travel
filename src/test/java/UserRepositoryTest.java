
import edu.businesstravel.dao.entities.Role;
import edu.businesstravel.dao.entities.User;
import edu.businesstravel.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Use a mock database connection for testing
        Connection mockConnection = createMockConnection();
        userRepository = new UserRepository(mockConnection);
    }

    @Test
    public void saveAndFindByIdTest() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPswd("password123");
        user.setRole(Role.ADMIN);

        user = userRepository.save(user);

        // Retrieve the user by ID
        Optional<User> retrievedUser = userRepository.findById(user.getIdUser());

        assertTrue(retrievedUser.isPresent());

    }

    @Test
    public void findAllTest() {
        List<User> allUsers = (List<User>) userRepository.findAll();
        assertFalse(allUsers.isEmpty());
    }

    @Test
    public void existsByIdTest() {
        // Assuming there is a user in the database
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPswd("existingPassword");
        user.setRole(Role.COORDINATOR);

        user = userRepository.save(user);

        // Test existence by ID
        assertTrue(userRepository.existsById(user.getIdUser()));

        // Test non-existence by ID
        assertFalse(userRepository.existsById(-1L));
    }

    @Test
    public void findAllByIdTest() {
        // Assuming there are some users in the database
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPswd("password1");
        user1.setRole(Role.EMPLOYER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPswd("password2");
        user2.setRole(Role.ADMIN);
        userRepository.save(user2);

        // Retrieve users by their IDs
        List<User> foundUsers = (List<User>) userRepository.findAllById(Arrays.asList(user1.getIdUser(), user2.getIdUser()));

        assertEquals(2, foundUsers.size());
        assertEquals("user1@example.com", foundUsers.get(0).getEmail());
        assertEquals("user2@example.com", foundUsers.get(1).getEmail());
    }

    @Test
    public void updateTest() {
        // Assuming there is a user in the database
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPswd("existingPassword");
        user.setRole(Role.COORDINATOR);

        userRepository.save(user);

        // Update the user's information
        user.setEmail("updated@example.com");
        user.setPswd("updatedPassword");
        user.setRole(Role.EMPLOYER);

        userRepository.save(user);

        // Retrieve the updated user by ID
        Optional<User> updatedUser = userRepository.findById(user.getIdUser());

        assertTrue(updatedUser.isPresent());
        assertEquals("updated@example.com", updatedUser.get().getEmail());
        assertEquals("updatedPassword", updatedUser.get().getPswd());
        assertEquals(Role.COORDINATOR, updatedUser.get().getRole());
    }

    @Test
    public void deleteTest() {
        // Assuming there is a user in the database
        User user = new User();
        user.setEmail("to-be-deleted@example.com");
        user.setPswd("toBeDeletedPassword");
        user.setRole(Role.EMPLOYER);

        userRepository.save(user);

        // Delete the user
        userRepository.delete(user);

        // Attempt to retrieve the deleted user by ID
        Optional<User> deletedUser = userRepository.findById(user.getIdUser());

        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void deleteAllTest() {
        // Assuming there are some users in the database
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPswd("password1");
        user1.setRole(Role.EMPLOYER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPswd("password2");
        user2.setRole(Role.ADMIN);
        userRepository.save(user2);

        // Delete all users
        userRepository.deleteAll();

        // Attempt to retrieve users after deletion
        List<User> allUsers = (List<User>) userRepository.findAll();

        assertTrue(allUsers.isEmpty());
    }

    private Connection createMockConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/businesstravel", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error creating mock connection for testing", e);
        }
    }
}

