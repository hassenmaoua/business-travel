package edu.businesstravel.repository.user;

import edu.businesstravel.repository.utils.CrudInterface;

import java.util.List;

public interface UserCrud<T, ID> extends CrudInterface<T, ID> {
    List<T> getAllByEntrepriseId(Long id);
}
