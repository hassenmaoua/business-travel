package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Domaine;
import edu.businesstravel.dao.entities.Entreprise;
import edu.businesstravel.dao.repository.util.CrudInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntrepriseRepository implements CrudInterface<Entreprise, Long> {
    private final Connection connection;
    private final DomaineRepository domaineRepository;

    public EntrepriseRepository(Connection connection) {
        this.connection = connection;
        this.domaineRepository = new DomaineRepository(connection);
    }

    @Override
    public <S extends Entreprise> S save(S entity) {
        String query;

        if (existsById(entity.getIdEntreprise())) {
            query = "UPDATE entreprises SET  raisonSociale=?, adresse=?, domaineId=? WHERE idEntreprise=?";
        } else {
            query = "INSERT INTO entreprises(raisonSociale, adresse, domaineId) VALUES(?,?,?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getRaisonSociale());
            statement.setString(2, entity.getAdresse());

            if (existsById(entity.getIdEntreprise())) {
                statement.setLong(3, entity.getIdEntreprise());
            }

            statement.executeUpdate();

            // Retrieve the generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                entity.setIdEntreprise(generatedId);
            } else {
                throw new SQLException("Failed to get generated ID for the Entreprise");
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public <S extends Entreprise> void saveAll(Iterable<S> entities) {
        try {
            for (Entreprise entity : entities) {
                save(entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Entreprise> findById(Long idEntreprise) {
        String query = "SELECT * FROM entreprises WHERE idEntreprise = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idEntreprise);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fetchEntreprise(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long idEntreprise) {
        String query = "SELECT COUNT(*) FROM entreprises WHERE idEntreprise = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idEntreprise);
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
    public Iterable<Entreprise> findAll() {
        List<Entreprise> entrepriseList = new ArrayList<>();
        String query = "SELECT * FROM entreprises";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Entreprise entreprise = fetchEntreprise(resultSet);
                entrepriseList.add(entreprise);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entrepriseList;
    }


    @Override
    public Iterable<Entreprise> findAllById(Iterable<Long> ids) {
        List<Entreprise> entrepriseList = new ArrayList<>();
        String query = "SELECT * FROM entreprises WHERE idEntreprise IN (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Entreprise entreprise = fetchEntreprise(resultSet);
                    entrepriseList.add(entreprise);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entrepriseList;
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM entreprises";
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
    public void deleteById(Long idEntreprise) {
        String query = "DELETE FROM entreprises WHERE idEntreprise = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idEntreprise);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Entreprise entity) {
        deleteById(entity.getIdEntreprise());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        String query = "DELETE FROM entreprises WHERE idEntreprise IN (?)";
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
    public void deleteAll(Iterable<? extends Entreprise> entities) {
        List<Long> ids = new ArrayList<>();
        for (Entreprise entity : entities) {
            ids.add(entity.getIdEntreprise());
        }
        deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM entreprises";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Entreprise fetchEntreprise(ResultSet resultSet) throws SQLException {
        Entreprise entreprise = new Entreprise();
        entreprise.setIdEntreprise(resultSet.getLong("idEntreprise"));
        entreprise.setRaisonSociale(resultSet.getString("raisonSociale"));
        entreprise.setAdresse(resultSet.getString("adresse"));

        Optional<Domaine> optionalDomaine = domaineRepository.findById(resultSet.getLong("domaineId"));
        optionalDomaine.ifPresent(entreprise::setDomaine);

        return entreprise;
    }
}
