package edu.businesstravel.dao.repository.util;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudInterface<T, ID> {
    <S extends T> S save(S entity) throws SQLException;

    <S extends T> void saveAll(Iterable<S> entities);

    Optional<T> findById(ID id) throws SQLException;

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();

    void updateById(T entity,ID id) throws SQLException;
}
