package edu.businesstravel.services.authentification;

import edu.businesstravel.entities.User;

public interface IAuthenticationService {
    User login(String email, String password);
}
