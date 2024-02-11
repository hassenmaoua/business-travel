package edu.businesstravel.repository.companion;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.repository.evenement.EvenementRepository;
import edu.businesstravel.repository.user.UserRepository;
import edu.businesstravel.repository.voyage.VoyageRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanionRepository implements CompanionCrud<Companion, Long> {
    private final Connection connection;
    private final VoyageRepository voyageRepository;
    private final UserRepository userRepository;
    private final EvenementRepository evenementRepository;

    public CompanionRepository(Connection connection) {
        this.connection = connection;
        this.voyageRepository = new VoyageRepository(connection);
        this.userRepository = new UserRepository(connection);
        this.evenementRepository = new EvenementRepository();
    }


    @Override
    public <S extends Companion> S save(S entity) {
        String query;

        Boolean isExist = existsById(entity.getIdCompanion());

        if (isExist) {
            query = "UPDATE companions SET evenementId=?, employeeId=?, voyageId=?, nom=?, domaineActivite=?, age=?, " +
                    "besoinsSpeciaux=?, contactUrgenceNom=?, contactUrgenceTel=?, " +
                    "restrictionsAlimentaires=?, numPassport=?, notesSupplementaires=?, dateC=?, dateU=? " +
                    "WHERE idCompanion=?";
        } else {
            query = "INSERT INTO companions(evenementId, employeeId, voyageId, nom, domaineActivite, age, " +
                    "besoinsSpeciaux, contactUrgenceNom, contactUrgenceTel, restrictionsAlimentaires, " +
                    "numPassport, notesSupplementaires, dateC, dateU) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, entity.getEvenement().getIdEvenement());
            statement.setLong(2, entity.getEmployee().getIdUser());
            if (entity.getVoyage() != null) {
                statement.setLong(3, entity.getVoyage().getIdVoyage());
            } else {
                statement.setNull(3, Types.INTEGER); // Assuming voyageId is of INTEGER type, adjust if needed
            }
            statement.setString(4, entity.getNom());
            statement.setString(5, entity.getDomaineActivite());
            statement.setInt(6, entity.getAge());
            statement.setString(7, entity.getBesoinsSpeciaux());

            statement.setString(8, entity.getContactUrgenceNom());
            statement.setString(9, entity.getContactUrgenceTel());
            statement.setString(10, entity.getRestrictionsAlimentaires());
            statement.setString(11, entity.getNumPassport());
            statement.setString(12, entity.getNotesSupplementaires());
            statement.setDate(13, entity.getDateC());
            statement.setDate(14, entity.getDateU());

            if (existsById(entity.getIdCompanion())) {
                statement.setLong(15, entity.getIdCompanion());
            }

            statement.executeUpdate();

            if (!isExist) {// Retrieve the generated ID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    entity.setIdCompanion(generatedId);
                } else {
                    throw new SQLException("Failed to get generated ID for the companion");
                }
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public <S extends Companion> void saveAll(Iterable<S> entities) {
        try {
            for (Companion entity : entities) {
                save(entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Companion> findById(Long idCompanion) {
        String query = "SELECT * FROM companions WHERE idCompanion = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idCompanion);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fetchCompanion(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long idCompanion) {
        String query = "SELECT COUNT(*) FROM companions WHERE idCompanion = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idCompanion);
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
    public Iterable<Companion> findAll() {
        List<Companion> companionList = new ArrayList<>();
        String query = "SELECT * FROM companions";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Companion companion = fetchCompanion(resultSet);
                companionList.add(companion);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return companionList;
    }


    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM companions";
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
    public void deleteById(Long idCompanion) {
        String query = "DELETE FROM companions WHERE idCompanion = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idCompanion);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Companion entity) {
        deleteById(entity.getIdCompanion());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        String query = "DELETE FROM companions WHERE idCompanion IN (?)";
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
    public void deleteAll(Iterable<? extends Companion> entities) {
        List<Long> ids = new ArrayList<>();
        for (Companion entity : entities) {
            ids.add(entity.getIdCompanion());
        }
        deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM companions";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Companion> findByVoyageId(Long voyageId) {
        List<Companion> companions = new ArrayList<>();
        String query = "SELECT * FROM companions WHERE voyageId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, voyageId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Companion companion = fetchCompanion(resultSet);
                companions.add(companion);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return companions;
    }

    @Override
    public List<Companion> findUnassignedCompanions() {
        List<Companion> unassignedCompanions = new ArrayList<>();
        String query = "SELECT * FROM companions WHERE voyageId IS NULL";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Companion companion = fetchCompanion(resultSet);
                unassignedCompanions.add(companion);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return unassignedCompanions;
    }

    private Companion fetchCompanion(ResultSet resultSet) throws SQLException {
        Companion companion = new Companion();

        companion.setIdCompanion(resultSet.getLong("idCompanion"));
        companion.setNom(resultSet.getString("nom"));
        companion.setDomaineActivite(resultSet.getString("domaineActivite"));
        companion.setAge(resultSet.getInt("age"));
        companion.setBesoinsSpeciaux(resultSet.getString("besoinsSpeciaux"));
        companion.setContactUrgenceNom(resultSet.getString("contactUrgenceNom"));
        companion.setContactUrgenceTel(resultSet.getString("contactUrgenceTel"));
        companion.setRestrictionsAlimentaires(resultSet.getString("restrictionsAlimentaires"));
        companion.setNumPassport(resultSet.getString("numPassport"));
        companion.setNotesSupplementaires(resultSet.getString("notesSupplementaires"));
        companion.setDateC(resultSet.getDate("dateC"));
        companion.setDateU(resultSet.getDate("dateU"));


        Optional<Voyage> optionalVoyage = voyageRepository.findById(resultSet.getLong("voyageId"));
        optionalVoyage.ifPresent(companion::setVoyage);

        Optional<User> optionalUser = userRepository.findById(resultSet.getLong("employeeId"));
        optionalUser.ifPresent(companion::setEmployee);

        Optional<Evenement> optionalEvenement = evenementRepository.findById(resultSet.getLong("evenementId"));
        optionalEvenement.ifPresent(companion::setEvenement);

        return companion;
    }
}

