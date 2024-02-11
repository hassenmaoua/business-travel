package edu.businesstravel.services.user;

import edu.businesstravel.entities.User;

import java.util.List;

public interface IUserService {
    User add(User user);
    User update(User user);

    User getById(Long id);

    List<User> getAll();
    void remove(Long userId);

    List<User> getByEntrepriseId(Long id);

    boolean isEmailExist(String email);

    boolean isPhoneExist(String phone);
}
