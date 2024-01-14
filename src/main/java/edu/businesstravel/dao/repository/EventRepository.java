package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Event;
import edu.businesstravel.dao.repository.util.CrudInterface;

import java.sql.Connection;
import java.util.Optional;

public class EventRepository implements CrudInterface<Event ,Long> {
    private final Connection connection;

    public EventRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public <S extends Event> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Event> void saveAll(Iterable<S> entities) {

    }

    @Override
    public Optional<Event> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Event> findAll() {
        return null;
    }

    @Override
    public Iterable<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
