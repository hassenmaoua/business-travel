package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Entreprise;
import edu.businesstravel.dao.entities.Role;
import edu.businesstravel.dao.entities.User;
import edu.businesstravel.dao.repository.util.CrudInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudInterface<User, Long> {
    private final Connection connection;
    private final EntrepriseRepository entrepriseRepository;

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.entrepriseRepository = new EntrepriseRepository(connection);
    }

    @Override
    public <S extends User> S save(S entity) {
        String query;

        if (existsById(entity.getIdUser())) {
            query = "UPDATE users SET email=?, pswd=?, role=?, nom=?, prenom=?, adresse=?, dateNaissance=?, telephone=?, entrepriseId=? WHERE idUser=?";
        } else {
            query = "INSERT INTO users(email, pswd, role, nom, prenom, adresse, dateNaissance, telephone, entrepriseId) VALUES(?,?,?,?,?,?,?,?,?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPswd());
            statement.setString(3, entity.getRole().name());
            statement.setString(4, entity.getNom());
            statement.setString(5, entity.getPrenom());
            statement.setString(6, entity.getAdresse());
            statement.setDate(7, entity.getDateNaissance());
            statement.setString(8, entity.getTelephone());

            if (entity.getEntreprise() != null) {
                statement.setLong(9, entity.getEntreprise().getIdEntreprise());
            } else {
                statement.setNull(9, Types.BIGINT);
            }

            if (existsById(entity.getIdUser())) {
                statement.setLong(10, entity.getIdUser());
            }

            statement.executeUpdate();

            // Retrieve the generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                entity.setIdUser(generatedId);
            } else {
                throw new SQLException("Failed to get generated ID for the user");
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public <S extends User> void saveAll(Iterable<S> entities) {
        try {
            for (User entity : entities) {
                save(entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long idUser) {
        String query = "SELECT * FROM users WHERE idUser = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idUser);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fetchUser(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long idUser) {
        String query = "SELECT COUNT(*) FROM users WHERE idUser = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idUser);
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
    public Iterable<User> findAll() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = fetchUser(resultSet);
                userList.add(user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }


    @Override
    public Iterable<User> findAllById(Iterable<Long> ids) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE idUser IN (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    User user = fetchUser(resultSet);
                    userList.add(user);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM users";
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
    public void deleteById(Long idUser) {
        String query = "DELETE FROM users WHERE idUser = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idUser);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(User entity) {
        deleteById(entity.getIdUser());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        String query = "DELETE FROM users WHERE idUser IN (?)";
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
    public void deleteAll(Iterable<? extends User> entities) {
        List<Long> ids = new ArrayList<>();
        for (User entity : entities) {
            ids.add(entity.getIdUser());
        }
        deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM users";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private User fetchUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setIdUser(resultSet.getLong("idUser"));
        user.setEmail(resultSet.getString("email"));
        user.setPswd(resultSet.getString("pswd"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        user.setNom(resultSet.getString("nom"));
        user.setPrenom(resultSet.getString("prenom"));
        user.setAdresse(resultSet.getString("adresse"));
        user.setDateNaissance(resultSet.getDate("dateNaissance"));
        user.setTelephone(resultSet.getString("telephone"));

        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(resultSet.getLong("entrepriseId"));
        optionalEntreprise.ifPresent(user::setEntreprise);

        return user;
    }
}