package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Role;
import edu.businesstravel.dao.entities.User;
import edu.businesstravel.dao.entities.Voyage;
import edu.businesstravel.dao.repository.util.CrudInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoyageRepository implements CrudInterface<Voyage,Long> {
    private final Connection connection;

    public VoyageRepository(Connection connection){
        this.connection = connection;
    }
    @Override
    public <S extends Voyage> S save(S entity) {
        String query;

        if (existsById(entity.getIdVoyage())) {
            query = "UPDATE voyages SET nom=?, depart=?, destination=?, dateDepart=?, class=?, avion=? WHERE idVoyage=?";
        } else {
            query = "INSERT INTO voyages(nom, depart, destination,dateDepart,class,avion) VALUES(?,?,?,?,?,?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getNom());
            statement.setString(2, entity.getDepart());
            statement.setString(3, entity.getDestination());
            statement.setDate(4, entity.getDateDepart());
            statement.setString(5, entity.getClasse());
            statement.setString(6, entity.getAvion());



            if (existsById(entity.getIdVoyage())) {
                statement.setLong(7, entity.getIdVoyage());
            }

            statement.executeUpdate();

            // Retrieve the generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                entity.setIdVoyage(generatedId);
            } else {
                throw new SQLException("Failed to get generated ID for the voyage");
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public <S extends Voyage> void saveAll(Iterable<S> entities) {
        String query = "INSERT INTO voyages(nom, depart, destination,dateDepart,class,avion) VALUES(?,?,?,?,?,?)";
        try {
            for (Voyage entity : entities) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getNom());
                statement.setString(2, entity.getDepart());
                statement.setString(3, entity.getDestination());
                statement.setDate(4, entity.getDateDepart());
                statement.setString(5, entity.getClasse());
                statement.setString(6, entity.getAvion());
                statement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Optional<Voyage> findById(Long idVoyage) {
        String query = "SELECT * FROM voyages WHERE idVoyage = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idVoyage);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Voyage voyage = new Voyage();
                voyage.setIdVoyage(resultSet.getLong("idVoyage"));
                voyage.setNom(resultSet.getString("nom"));
                voyage.setDepart(resultSet.getString("depart"));
                voyage.setDestination(resultSet.getString("destination"));
                voyage.setDateDepart(resultSet.getDate("dateDepart"));
                voyage.setClasse(resultSet.getString("classe"));
                voyage.setAvion(resultSet.getString("avion"));
                return Optional.of(voyage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long idVoyage) {
        String query = "SELECT COUNT(*) FROM voyages WHERE idVoyage = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idVoyage);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Iterable<Voyage> findAll() {
        List<Voyage> voyageList = new ArrayList<>();
        String query = "SELECT * FROM voyages";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            FetchVoyage(voyageList, resultSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return voyageList;
    }

    @Override
    public Iterable<Voyage> findAllById(Iterable<Long> ids) {
        List<Voyage> voyageList = new ArrayList<>();
        String query = "SELECT * FROM voyages WHERE idVoyage IN (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                FetchVoyage(voyageList, resultSet);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return voyageList;
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM voyages";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void deleteById(Long idVoyage) {
        String query = "DELETE FROM voyages WHERE idVoyage = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idVoyage);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Voyage entity) {deleteById(entity.getIdVoyage());

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        String query = "DELETE FROM voyages WHERE idVoyage IN (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Voyage> entities) {
        List<Long> ids = new ArrayList<>();
        for (Voyage entity : entities) {
            ids.add(entity.getIdVoyage());
        }
        deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM voyages";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private void FetchVoyage(List<Voyage> voyageList, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Voyage voyage = new Voyage();
            voyage.setIdVoyage(resultSet.getLong("idVoyage"));
            voyage.setNom(resultSet.getString("nom"));
            voyage.setDepart(resultSet.getString("depart"));
            voyage.setDestination(resultSet.getString("destination"));
            voyage.setDateDepart(resultSet.getDate("dateDepart"));
            voyage.setClasse(resultSet.getString("classe"));
            voyage.setAvion(resultSet.getString("avion"));
            voyageList.add(voyage);
        }
    }
}

