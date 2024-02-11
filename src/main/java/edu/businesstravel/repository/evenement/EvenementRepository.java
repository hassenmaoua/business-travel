package edu.businesstravel.repository.evenement;

import edu.businesstravel.entities.Category;
import edu.businesstravel.entities.Etat;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.repository.category.CategoryRepository;
import edu.businesstravel.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenementRepository implements IEvenementRepository {
   // private  Connection connection;
    private final Connection connection;
    private final CategoryRepository categoryRepository;

    public EvenementRepository() {
        connection = DatabaseConnection.getInstance().getConnection();
        categoryRepository = new CategoryRepository();

    }




    @Override
    public Optional<Object> save(Object o) {

        if (o instanceof Evenement) {
            Evenement evenement = (Evenement) o;

            try {
                String query = "INSERT INTO evenements (titre, description, dateDebut, dateFin, region, adresse, etat, idCategory) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, evenement.getTitre());
                    preparedStatement.setString(2, evenement.getDescription());
                    preparedStatement.setDate(3, evenement.getDateDebut());
                    preparedStatement.setDate(4, evenement.getDateFin());
                    preparedStatement.setString(5, evenement.getRegion());
                    preparedStatement.setString(6, evenement.getAdresse());
                    preparedStatement.setString(7, evenement.getEtat().name());
                    preparedStatement.setLong(8, evenement.getCategorie().getIdCategory()); // Use idCategory instead of getCategory().getIdCategory()

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            Long generatedId = generatedKeys.getLong(1);
                            evenement.setIdEvenement(generatedId);
                            return Optional.of(evenement);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return Optional.empty();
    }
    @Override
    public Optional<Evenement> findById(Long id) {
        try {
            String query = "SELECT * FROM evenements WHERE idEvenement = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Evenement evenement = mapResultSetToEvenement(resultSet);
                        return Optional.of(evenement);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Object> findAll() {
        List<Object> evenements = new ArrayList<>();

        try {
            String query = "SELECT * FROM evenements ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Evenement evenement = mapResultSetToEvenement(resultSet);
                        evenements.add(evenement);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return evenements;


    }

    @Override
    public void deleteOne(Long id) {
        try {
            String query = "DELETE FROM evenements WHERE idEvenement = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.println("supprime avec sucess");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveAll(ArrayList<Object> list) {
        list.forEach(this::save);
    }

    @Override
    public void update(Object o, Long id) {
        if (o instanceof Evenement) {
            Evenement updatedEvenement = (Evenement) o;

            try {
                String query = "UPDATE evenements SET titre = ?, description = ?, dateDebut = ?, dateFin = ?, " +
                        "region = ?, adresse = ?, etat = ?, idCategory = ? WHERE idEvenement = ?";
                System.out.println("query "+query);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, updatedEvenement.getTitre());
                    preparedStatement.setString(2, updatedEvenement.getDescription());
                    preparedStatement.setDate(3, updatedEvenement.getDateDebut());
                    preparedStatement.setDate(4, updatedEvenement.getDateFin());
                    preparedStatement.setString(5, updatedEvenement.getRegion());
                    preparedStatement.setString(6, updatedEvenement.getAdresse());
                    preparedStatement.setString(7, updatedEvenement.getEtat().name());
                    preparedStatement.setLong(8, updatedEvenement.getCategorie().getIdCategory()); // Use idCategory instead of getCategory().getIdCategory()
                    preparedStatement.setLong(9, id);

                    preparedStatement.executeUpdate();
                    System.out.println("Update successful");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid object type for update");
        }
    }

    @Override
    public boolean ifExist(Long id) {
        return findById(id).isPresent();
    }

    private Evenement mapResultSetToEvenement(ResultSet resultSet) throws SQLException {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(resultSet.getLong("idEvenement"));
        evenement.setTitre(resultSet.getString("titre"));
        evenement.setDescription(resultSet.getString("description"));
        evenement.setDateDebut(resultSet.getDate("dateDebut"));
        evenement.setDateFin(resultSet.getDate("dateFin"));
        evenement.setRegion(resultSet.getString("region"));
        evenement.setAdresse(resultSet.getString("adresse"));
        evenement.setEtat(Etat.valueOf(resultSet.getString("etat")));

        Optional<Category> optionalCategory = categoryRepository.findById(resultSet.getLong("idCategory"));
        optionalCategory.ifPresent(evenement::setCategorie);


        return evenement;
    }

    public static void main(String[] args) {

    }
}
