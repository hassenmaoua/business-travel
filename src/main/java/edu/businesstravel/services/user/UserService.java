/**
 * UserService class provides services related to User entities.
 * It includes methods for adding, updating, retrieving, and removing users.
 * Additionally, it performs validation checks for user data and sends email notifications.
 *
 * @author Hassen MAOUA
 * @version 1.0
 */
package edu.businesstravel.services.user;


import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.repository.user.UserRepository;
import edu.businesstravel.tools.DatabaseConnection;
import edu.businesstravel.tools.EmailSender;
import edu.businesstravel.tools.PasswordGenerator;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserService implements IUserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService instance with a connection to the user repository.
     */
    public UserService() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        this.userRepository = new UserRepository(connection);
    }


    /**
     * Adds a new user, performs validation, and sends an email notification.
     *
     * @param user The user to be added.
     * @return The added user.
     */
    @Override
    public User add(User user) {
        validateUser(user);

        validateEmailExist(user);
        validatePhoneExist(user);

        user.setPswd(PasswordGenerator.generateRandomPassword());

        user = userRepository.save(user);

        sendEmail(user);
        return user;
    }

    /**
     * Updates an existing user, performs validation, and returns the updated user.
     *
     * @param user The user to be updated.
     * @return The updated user.
     */
    @Override
    public User update(User user) {
        validateUser(user);

        // Check if the user exists
        if (!userRepository.existsById(user.getIdUser())) {
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }

        user = userRepository.save(user);
        return user;
    }


    @Override
    public User getById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> getAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    @Override
    public List<User> getByEntrepriseId(Long id) {
        return userRepository.getAllByEntrepriseId(id);
    }

    @Override
    public void remove(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        userRepository.delete(user);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.isUserEmailExist(email);
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return false;
    }

    /**
     * Validates user data by performing common checks for both add and update methods.
     *
     * @param user The user to be validated.
     */
    private void validateUser(User user) {
        // Common validation logic for both add and update methods

        // Check if the user object is null
        if (user == null) {
            throw new IllegalArgumentException("L'objet Utilisateur ne peut pas être nul");
        }

        // Check if the 'nom' (last name) is provided and has a minimum length of 3 characters
        if (user.getNom() == null || user.getNom().length() < 3 || user.getNom().length() > 30) {
            throw new IllegalArgumentException("Le nom doit être fourni et avoir une longueur minimale de 3 caractères et longueur maximale de 30 caractères");
        }

        // Check if the 'prenom' (first name) is provided and has a minimum length of 3 characters
        if (user.getPrenom() == null || user.getPrenom().length() < 3 || user.getPrenom().length() > 30) {
            throw new IllegalArgumentException("Le prénom doit être fourni et avoir une longueur minimale de 3 caractères et longueur maximale de 30 caractères");
        }

        // Check if the 'email' is provided and follows a valid format
        if (user.getEmail() == null || isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Adresse e-mail invalide");
        }

        // Check if the 'pswd' (password) is provided and meets the minimum length requirement
//        if (user.getPswd() == null || isValidPassword(user.getPswd())) {
//            throw new IllegalArgumentException("Le mot de passe doit être fourni et avoir une longueur minimale de 8 caractères");
//        }

        if (user.getTelephone() == null || !isValidPhone(user.getTelephone())) {
            throw new IllegalArgumentException("Le numéro de téléphone doit commencer par 2, 4, 5, 7 ou 9 et contenir 8 chiffres.");
        }

        // Check if the 'role' is valid (ADMIN, COORDINATOR, EMPLOYER)
        if (user.getRole() == null || !(user.getRole().equals(Role.EMPLOYER) || user.getRole().equals(Role.COORDINATOR) || user.getRole().equals(Role.ADMIN))) {
            throw new IllegalArgumentException("Rôle invalide");
        }

        // If the role is COORDINATOR or EMPLOYER, check that 'entrepriseId' is not null
        if ((user.getRole().equals(Role.COORDINATOR) || user.getRole().equals(Role.EMPLOYER)) && user.getEntreprise() == null) {
            throw new IllegalArgumentException("L'identifiant de l'entreprise est requis pour le rôle de Coordinateur ou Employeur");
        }
    }

    private void validateEmailExist(User user) {
        if (isEmailExist(user.getEmail())) {
            throw new IllegalArgumentException("Adresse e-mail déjà utilisée");
        }
    }

    private void validatePhoneExist(User user) {
        if (isPhoneExist(user.getTelephone())) {
            throw new IllegalArgumentException("Le numéro de téléphone déjà utilisé");
        }
    }

    /**
     * Sends an email notification to the user.
     *
     * @param user The user for whom the email is sent.
     */
    private void sendEmail(User user) {
        String company = user.getEntreprise() == null ? "Administration Team" : user.getEntreprise().getNomEntreprise();
        EmailSender.UserData userData = new EmailSender.UserData(user.getNom(), user.getEmail(), user.getPswd(), user.getRole().name(), company);
        EmailSender.sendEmail(user.getEmail(), userData);
    }

    // Helper method to validate email format using a simple regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        return email.length() < 3 || email.length() > 50 || !email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$";

        return password.length() < 6 || password.length() > 50 || !password.matches(passwordRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[24579]\\d{7}$";

        return phone.matches(phoneRegex);
    }


}
