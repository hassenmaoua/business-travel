package edu.businesstravel.repository.category;

import edu.businesstravel.entities.Category;
import edu.businesstravel.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository implements ICategoryRepository {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public CategoryRepository() {
        this.connection = connection;
    }

    public static void main(String[] args) {
        CategoryRepository categoryRepository = new CategoryRepository();

        //  categoryRepository.save(c);
        System.out.println("find by id" + categoryRepository.findById(2L));
        System.out.println(categoryRepository.findAll());


    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Object> save(Object o) {
        Category category = (Category) o;

        String requete = "INSERT INTO categories (nom)   VALUES ('" + category.getName() + "')";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(requete);
            System.out.println("ajout success");
            return Optional.of(category);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Category> findById(Long id) {
        String requete = "SELECT * FROM categories WHERE idCategorie =" + id;

        try {
            Statement st = connection.createStatement();
            ResultSet re = st.executeQuery(requete);
            Category category = new Category();
            if (re.next()) {

                category.setIdCategory(re.getLong(1));
                category.setName(re.getString(2));


            }

            return Optional.of(category);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();

    }

    @Override
    public List<Category> findAll() {
        String query = "SELECT * FROM categories";
        ArrayList<Category> list = new ArrayList<>();

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Category category = new Category();
                        category.setIdCategory(resultSet.getLong("idCategorie"));
                        category.setName(resultSet.getString("nom"));
                        list.add(category);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public void deleteOne(Long id) {
        String requete = "DELETE FROM categories WHERE idCategorie =" + id;
        try {
            Statement st = connection.createStatement();
            int set = st.executeUpdate(requete);
            if (set > 0) {
                System.out.println("nombre de ligne supprime " + set);
            } else System.out.println("aucun ligne supprime ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @Override

    public void saveAll(ArrayList<Object> list) {
        try {
            String query = "INSERT INTO categories (idCategorie, nom) VALUES (?, ?)";

            list.forEach(object -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setLong(1, ((Category) object).getIdCategory());
                    preparedStatement.setString(2, ((Category) object).getName());

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Addition successful");
                    } else {
                        System.out.println("Failed to add data");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Object o, Long id) {
        if (o instanceof Category) {
            Category updatedCategory = (Category) o;

            try {
                String query = "UPDATE categories SET nom = ? WHERE idCategorie = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, updatedCategory.getName());
                    preparedStatement.setLong(2, id);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Update successful");
                    } else {
                        System.out.println("No category found with the given id");
                    }
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
        try {
            String query = "SELECT COUNT(*) FROM categories WHERE idCategorie = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0; // If count is greater than 0, the category exists.
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false if there is an error or the category doesn't exist.
    }
}

