package edu.businesstravel.repository.utils;
/**
 * AuthInterface is an interface for handling user authentication in a generic way.
 * Implement this interface to create a custom authentication system for different types of users.
 *
 * @param <T>  The type representing the user.
 * @param <ID> The type representing the unique identifier of the user.
 *
 * Example Usage:
 * <pre>{@code
 *     public class MyAuthService implements AuthInterface<MyUser, Long> {
 *         // Implement the methods of AuthInterface here
 *     }
 * }</pre>
 *
 * @author Hassen MAOUA
 * @version 1.0
 */

public interface AuthInterface<T, ID> {

    /**
     * Authenticate a user based on username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated user or null if authentication fails.
     */
    T authenticate(String username, String password);

    /**
     * Check if a user is currently authenticated.
     *
     * @param user The user to check for authentication.
     * @return true if the user is authenticated, false otherwise.
     */
    boolean isAuthenticated(T user);

    /**
     * Log out a user, terminating their authentication session.
     *
     * @param user The user to log out.
     */
    void logout(T user);

    /**
     * Get the unique identifier for a user.
     *
     * @param email The email whose checking is needed.
     * @return true if the email is existed, false if not existed.
     */
    boolean isUserEmailExist(String email);

    /**
     * Change the password of a user.
     *
     * @param user The user whose password needs to be changed.
     * @param newPassword The new password for the user.
     */
    void changePassword(T user, String newPassword);
}
