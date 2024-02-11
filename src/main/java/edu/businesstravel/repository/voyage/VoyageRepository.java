package edu.businesstravel.repository.voyage;

import edu.businesstravel.entities.Voyage;
import edu.businesstravel.entities.VoyageClasse;

import java.sql.*;
import java.util.*;

public class VoyageRepository implements VoyageCrud<Voyage, Long> {
    private final Connection connection;

    public VoyageRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <S extends Voyage> S save(S entity) {
        String query;

        boolean isExist = existsById(entity.getIdVoyage());

        if (isExist) {
            query = "UPDATE voyages SET nom=?, depart=?, destination=?, dateDepart=?, classe=?, avion=?, dateC=?, dateU=? WHERE idVoyage=?";
        } else {
            query = "INSERT INTO voyages(nom, depart, destination, dateDepart, classe, avion, dateC, dateU) VALUES(?,?,?,?,?,?,?,?)";
        }

        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getNom());
            statement.setString(2, entity.getDepart());
            statement.setString(3, entity.getDestination());
            statement.setDate(4, entity.getDateDepart());
            statement.setString(5, entity.getClasse().name());
            statement.setString(6, entity.getAvion());
            statement.setDate(7, entity.getDateC());
            statement.setDate(8, entity.getDateU());

            if (isExist) {
                statement.setLong(9, entity.getIdVoyage());
            }

            statement.executeUpdate();

            if (!isExist) {   // Retrieve the generated ID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    entity.setIdVoyage(generatedId);
                } else {
                    throw new SQLException("Failed to get generated ID for the voyage");
                }
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public <S extends Voyage> void saveAll(Iterable<S> entities) {
        try {
            for (Voyage entity : entities) {
                save(entity);
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
                return Optional.of(fetchVoyage(resultSet));
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

            while (resultSet.next()) {
                Voyage voyage = fetchVoyage(resultSet);
                voyageList.add(voyage);
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
    public void delete(Voyage entity) {
        deleteById(entity.getIdVoyage());
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

    @Override
    public List<Voyage> getAllByEmployeeEntrepriseId(Long entrepriseId) {
        List<Voyage> voyageList = new ArrayList<>();
        String query = "SELECT v.* FROM voyages v " +
                "JOIN companions c ON c.voyageId = v.idVoyage " +
                "JOIN users u ON u.idUser = c.employeeId " +
                "WHERE u.entrepriseId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, entrepriseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Voyage voyage = fetchVoyage(resultSet);
                voyageList.add(voyage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return voyageList;
    }

    @Override
    public List<Voyage> getAllByEmployeeId(Long employeeId) {
        List<Voyage> voyageList = new ArrayList<>();
        String query = "SELECT v.* FROM voyages v " +
                "JOIN companions c ON c.voyageId = v.idVoyage " +
                "WHERE c.employeeId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Voyage voyage = fetchVoyage(resultSet);
                voyageList.add(voyage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return voyageList;
    }

    @Override
    public Set<String> findAvailableDestinations() {
        Set<String> destinations = new HashSet<>(); // Use HashSet for uniqueness

        String query = "SELECT e.region, COUNT(DISTINCT c.idCompanion) AS companionCount " +
                "FROM companions c " +
                "JOIN evenements e ON c.evenementId = e.idEvenement " +
                "WHERE c.voyageId IS NULL " +
                "GROUP BY e.region " +
                "ORDER BY companionCount DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Fetch resultSet into destinations

                if (resultSet.getInt("companionCount") >= 2) {
                    String region = resultSet.getString("region");
                    destinations.add(region);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return destinations;
    }


    @Override
    public Boolean isExistNonAffectedCompanions() {
        String query;
        query = "SELECT e.region, COUNT(DISTINCT c.idCompanion) AS companionCount " +
                "FROM companions c " +
                "JOIN evenements e ON c.evenementId = e.idEvenement " +
                "WHERE c.voyageId IS NULL " +
                "GROUP BY e.region " +
                "ORDER BY companionCount DESC LIMIT 1";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int regionCount = resultSet.getInt("companionCount");
                return regionCount >= 2;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false in case of any exception or no result
    }

    @Override
    public Map<String, Integer> getCompanionCountByRegion() {
        Map<String, Integer> countCompanionsByRegion = new HashMap<>();
        String query =
                "SELECT e.region, COUNT(DISTINCT c.idCompanion) AS companionCount " +
                        "FROM companions c " +
                        "JOIN evenements e ON c.evenementId = e.idEvenement " +
                        "WHERE c.voyageId IS NULL " +
                        "GROUP BY e.region " +
                        "ORDER BY companionCount DESC";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt(2) >= 2) {
                    System.out.println(resultSet.getInt(2));
                    countCompanionsByRegion.put(resultSet.getString(1), resultSet.getInt(2));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return countCompanionsByRegion;
    }


    private Voyage fetchVoyage(ResultSet resultSet) throws SQLException {
        Voyage voyage = new Voyage();

        voyage.setIdVoyage(resultSet.getLong("idVoyage"));
        voyage.setNom(resultSet.getString("nom"));
        voyage.setDepart(resultSet.getString("depart"));
        voyage.setDestination(resultSet.getString("destination"));
        voyage.setDateDepart(resultSet.getDate("dateDepart"));
        voyage.setClasse(VoyageClasse.valueOf(resultSet.getString("classe")));
        voyage.setAvion(resultSet.getString("avion"));
        voyage.setDateC(resultSet.getDate("dateC"));
        voyage.setDateU(resultSet.getDate("dateU"));

        return voyage;
    }
}
