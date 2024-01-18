package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Domaine;
import edu.businesstravel.dao.repository.util.CrudInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DomaineRepository implements CrudInterface<Domaine, Long> {
    private final Connection connection;

    public DomaineRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <S extends Domaine> S save(S entity) {
        String query;

        if (existsById(entity.getIdDomaine())) {
            query = "UPDATE domaines SET  nom=? WHERE idDomaine=?";
        } else {
            query = "INSERT INTO domaines(nom) VALUES(?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getNom());

            if (existsById(entity.getIdDomaine())) {
                statement.setLong(2, entity.getIdDomaine());
            }

            statement.executeUpdate();

            // Retrieve the generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                entity.setIdDomaine(generatedId);
            } else {
                throw new SQLException("Failed to get generated ID for the domaine");
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public <S extends Domaine> void saveAll(Iterable<S> entities) {
        try {
            for (Domaine entity : entities) {
                save(entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Domaine> findById(Long idDomaine) {
        String query = "SELECT * FROM domaines WHERE idDomaine = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idDomaine);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fetchDomaine(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long idDomaine) {
        String query = "SELECT COUNT(*) FROM domaines WHERE idDomaine = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idDomaine);
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
    public Iterable<Domaine> findAll() {
        List<Domaine> domaineList = new ArrayList<>();
        String query = "SELECT * FROM domaines";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Domaine domaine = fetchDomaine(resultSet);
                domaineList.add(domaine);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return domaineList;
    }


    @Override
    public Iterable<Domaine> findAllById(Iterable<Long> ids) {
        List<Domaine> domaineList = new ArrayList<>();
        String query = "SELECT * FROM domaines WHERE idDomaine IN (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Domaine domaine = fetchDomaine(resultSet);
                    domaineList.add(domaine);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return domaineList;
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM domaines";
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
    public void deleteById(Long idDomaine) {
        String query = "DELETE FROM domaines WHERE idDomaine = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idDomaine);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Domaine entity) {
        deleteById(entity.getIdDomaine());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        String query = "DELETE FROM domaines WHERE idDomaine IN (?)";
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
    public void deleteAll(Iterable<? extends Domaine> entities) {
        List<Long> ids = new ArrayList<>();
        for (Domaine entity : entities) {
            ids.add(entity.getIdDomaine());
        }
        deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM domaines";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Domaine fetchDomaine(ResultSet resultSet) throws SQLException {
        Domaine domaine = new Domaine();
        domaine.setIdDomaine(resultSet.getLong("idDomaine"));
        domaine.setNom(resultSet.getString("nom"));
        return domaine;
    }
}