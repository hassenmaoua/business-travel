package edu.businesstravel.dao.repository;

import edu.businesstravel.dao.entities.Category;
import edu.businesstravel.dao.entities.Etat;
import edu.businesstravel.dao.entities.Event;
import edu.businesstravel.dao.repository.util.CrudInterface;
import edu.businesstravel.dao.repository.util.ICrud;
import edu.businesstravel.dao.tools.DatabaseConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static edu.businesstravel.dao.entities.Etat.Done;

public class EventRepository implements ICrud {
   // private  Connection connection;
    private   Connection connection= DatabaseConnection.getInstance().getConnection();
    public EventRepository(Connection connection) {
        this.connection = connection;
    }  public EventRepository() {
        this.connection = connection;
    }


    @Override
    public Optional<Object> save(Object o) {

        if (o instanceof Event) {
            Event event = (Event) o;

            try {
                String query = "INSERT INTO event (title, description, dateDebut, dateFin, region, adresse, status, idCategory) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, event.getTitle());
                    preparedStatement.setString(2, event.getDescription());
                    java.sql.Date dateDebutSql = java.sql.Date.valueOf(event.getDateDebut());
                    java.sql.Date dateFinSql = java.sql.Date.valueOf(event.getDateFin());
                    preparedStatement.setDate(3, dateDebutSql);
                    preparedStatement.setDate(4, dateFinSql);
                    preparedStatement.setString(5, event.getRegion());
                    preparedStatement.setString(6, event.getAdresse());
                    preparedStatement.setString(7, event.getStatus().name());
                    preparedStatement.setLong(8, event.getCategory()); // Use idCategory instead of getCategory().getIdCategory()

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            Long generatedId = generatedKeys.getLong(1);
                            event.setIdEvent(generatedId);
                            return Optional.of(event);
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
    public Object findById(Long id) {
        try {
            String query = "SELECT * FROM event WHERE idEvent = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Event event = mapResultSetToEvent(resultSet);
                        return event;
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
        List<Object> events = new ArrayList<>();

        try {
            String query = "SELECT * FROM event ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Event event = mapResultSetToEvent(resultSet);
                        events.add(event);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return events;


    }

    @Override
    public void deleteOne(Long id) {
        try {
            String query = "DELETE FROM event WHERE idEvent = ?";
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
        if (o instanceof Event) {
            Event updatedEvent = (Event) o;

            try {
                String query = "UPDATE event SET title = ?, description = ?, dateDebut = ?, dateFin = ?, " +
                        "region = ?, adresse = ?, status = ?, idCategory = ? WHERE idEvent = ?";
                System.out.println("query "+query);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, updatedEvent.getTitle());
                    preparedStatement.setString(2, updatedEvent.getDescription());
                    java.sql.Date dateDebutSql = java.sql.Date.valueOf(updatedEvent.getDateDebut());
                    java.sql.Date dateFinSql = java.sql.Date.valueOf(updatedEvent.getDateFin());
                    preparedStatement.setDate(3, dateDebutSql);
                    preparedStatement.setDate(4, dateFinSql);
                    preparedStatement.setString(5, updatedEvent.getRegion());
                    preparedStatement.setString(6, updatedEvent.getAdresse());
                    preparedStatement.setString(7, updatedEvent.getStatus().name());
                    preparedStatement.setLong(8, updatedEvent.getCategory()); // Use idCategory instead of getCategory().getIdCategory()
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
       if(findById(id) instanceof Event){
           return  true;
       }

        return false;
    }

    private Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setIdEvent(resultSet.getLong("idEvent"));
        event.setTitle(resultSet.getString("title"));
        event.setDescription(resultSet.getString("description"));
        event.setDateDebut(resultSet.getDate("dateDebut").toLocalDate());
        event.setDateFin(resultSet.getDate("dateFin").toLocalDate());
        event.setRegion(resultSet.getString("region"));
        event.setAdresse(resultSet.getString("adresse"));
        event.setStatus(Etat.valueOf(resultSet.getString("status")));
        event.setCategory(resultSet.getLong("idCategory"));

        return event;
    }

    public static void main(String[] args) {

    }
}
