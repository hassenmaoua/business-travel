package edu.businesstravel.repository.utils;

import java.util.Optional;

public interface CrudInterface<T, ID> {
    <S extends T> S save(S entity);

    <S extends T> void saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();
}
