/**
 * AuthentificationService is a service class for user authentication.
 * It provides a login method that authenticates users based on email and password.
 *
 * Example Usage:
 * <pre>{@code
 *     AuthentificationService authService = new AuthentificationService();
 *     User authenticatedUser = authService.login("user@example.com", "password123");
 * }</pre>
 *
 * @author Hassen MAOUA
 * @version 1.0
 */

package edu.businesstravel.services.authentification;

import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.repository.user.UserRepository;
import edu.businesstravel.tools.DatabaseConnection;

import java.sql.Connection;


public class AuthenticationService implements IAuthenticationService {
    private static User loggedInUser;
    private final UserRepository userRepository;

    public AuthenticationService() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        userRepository = new UserRepository(connection);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static Role getUserRole() { return loggedInUser.getRole();}

    public static void logout() {
        loggedInUser = null;
    }

    /**
     * Logs in a user based on email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The authenticated user if successful, null otherwise.
     */
    @Override
    public User login(String email, String password) {
        if (isNotEmpty(email) && isNotEmpty(password) && isEmailValid(email)) {
            // You can perform additional validation here if needed
            loggedInUser = userRepository.authenticate(email, password);
            return loggedInUser;

        } else {
            System.out.println("Email and password cannot be empty. Also, check the email format.");
            return null;
        }
    }



    /**
     * Checks if a string is not empty.
     *
     * @param str The string to check.
     * @return true if the string is not empty, false otherwise.
     */
    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Checks if the email format is valid.
     *
     * @param email The email to validate.
     * @return true if the email format is valid, false otherwise.
     */
    private boolean isEmailValid(String email) {
        // Simple email validation, you might want to use a more sophisticated approach
        // (e.g., regular expressions or a library) for a production environment
        return email != null && email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }
}
