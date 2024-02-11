package edu.businesstravel.repository;

import edu.businesstravel.entities.Domaine;
import edu.businesstravel.entities.Entreprise;
import edu.businesstravel.repository.utils.CrudInterface;
import edu.businesstravel.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntrepriseRepository implements CrudInterface<Entreprise, Long> {
    private final Connection connection;

    public EntrepriseRepository() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        this.connection = connection;
    }

    @Override
    public <S extends Entreprise> S save(S entity) {
        String query;

        if (existsById(entity.getIdEntreprise())) {
            query = "UPDATE entreprises SET  nomEntreprise=?,raisonSociale=?, adresse=?,domaine=?, email=?,telephone=?,nbEmployee=? WHERE idEntreprise=?";
        } else {
            query = "INSERT INTO entreprises(nomEntreprise,raisonSociale, adresse, domaine,email,telephone,nbEmployee) VALUES(?,?,?,?,?,?,?)";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getNomEntreprise());
            statement.setString(2, entity.getRaisonSociale());
            statement.setString(3, entity.getAdresse());
            statement.setString(4, entity.getDomaine().name());

            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getTelephone());
            statement.setLong(7, entity.getNbEmployee());


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
                Entreprise entreprise = new Entreprise();

                // Set the values for each field based on the ResultSet
                entreprise.setIdEntreprise(resultSet.getLong("idEntreprise"));
                entreprise.setNomEntreprise(resultSet.getString("nomEntreprise"));
                entreprise.setRaisonSociale(resultSet.getString("raisonSociale"));
                entreprise.setAdresse(resultSet.getString("adresse"));
                entreprise.setEmail(resultSet.getString("email"));
                entreprise.setDomaine(Domaine.valueOf(resultSet.getString("domaine")));
                entreprise.setTelephone(resultSet.getString("telephone"));
                entreprise.setNbEmployee(resultSet.getLong("nbEmployee"));
                entreprise.setDateC(resultSet.getDate("dateC"));
                entreprise.setDateU(resultSet.getDate("dateU"));

                entrepriseList.add(entreprise);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entrepriseList;
    }


    //    @Override
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
        entreprise.setNomEntreprise(resultSet.getString("nomEntreprise"));
        entreprise.setRaisonSociale(resultSet.getString("raisonSociale"));
        entreprise.setAdresse(resultSet.getString("adresse"));
        entreprise.setDomaine(Domaine.valueOf(resultSet.getString("domaine")));
        entreprise.setEmail(resultSet.getString("email"));
        entreprise.setTelephone(resultSet.getString("telephone"));
        entreprise.setNbEmployee(resultSet.getLong("nbEmployee"));
        entreprise.setDateC(resultSet.getDate("dateC"));  // This line is causing the issue
        entreprise.setDateU(resultSet.getDate("dateU"));

        return entreprise;
    }

    public <S extends Entreprise> S edit(S entity) {
        if (!existsById(entity.getIdEntreprise())) {
            // Throw an exception or handle the case where the entity doesn't exist
            throw new IllegalArgumentException("Cannot edit non-existing Entreprise");
        }

        String query = "UPDATE entreprises SET nomEntreprise=?, raisonSociale=?, adresse=?, domaine=?, email=?, telephone=?, nbEmployee=? WHERE idEntreprise=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, entity.getNomEntreprise());
            statement.setString(2, entity.getRaisonSociale());
            statement.setString(3, entity.getAdresse());
            statement.setString(4, entity.getDomaine().name());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getTelephone());
            statement.setLong(7, entity.getNbEmployee());
            statement.setLong(8, entity.getIdEntreprise());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // The update was successful
                return entity;
            } else {
                // Handle the case where no rows were updated (perhaps the entity ID was not found)
                return null;
            }
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
            return null;
        }
    }

}
