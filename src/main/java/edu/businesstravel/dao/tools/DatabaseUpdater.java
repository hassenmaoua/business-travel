package edu.businesstravel.dao.tools;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;

public class DatabaseUpdater {
    private final static Connection connection = DatabaseConnection.getInstance().getConnection();

    public static void main(String[] args) {
        // Create 'updates' and 'triggers' tables if not exists
        createUpdatesTableIfNotExists(connection);
        createTriggersTableIfNotExists(connection);

        try {
            // Load the XML configuration file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/core/database-updates.xml"));

            // Get the list of update nodes
            NodeList updateNodes = document.getElementsByTagName("update");

            // Iterate through each update node
            for (int i = 0; i < updateNodes.getLength(); i++) {
                Element updateElement = (Element) updateNodes.item(i);

                // Extract information from the XML
                String scriptId = updateElement.getElementsByTagName("id").item(0).getTextContent();
                String scriptPath = updateElement.getElementsByTagName("path").item(0).getTextContent();
                String comment = updateElement.getElementsByTagName("comment").item(0).getTextContent();

                // Check if the script has been executed
                if (!isScriptExecuted(scriptId)) {
                    // Log the update information
                    System.out.println("Executing script: " + scriptId + " - " + comment);

                    // Execute the SQL script and update the 'updates' table
                    executeScriptAndUpdateTable("src/core/" + scriptPath, scriptId, comment);
                } else {
                    // Log that the script has already been executed
                    System.out.println("Script already executed: " + scriptId);
                }
            }

            // After all updates are done, execute triggers
            executeTriggers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createUpdatesTableIfNotExists(Connection connection) {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS updates (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "script_id VARCHAR(255) NOT NULL," +
                    "comment VARCHAR(255) NOT NULL," +
                    "status INT NOT NULL" +
                    ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTriggersTableIfNotExists(Connection connection) {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS triggers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "trigger_id VARCHAR(255) NOT NULL," +
                    "comment VARCHAR(255) NOT NULL," +
                    "status INT NOT NULL" +
                    ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeScriptAndUpdateTable(String scriptPath, String scriptId, String comment) {
        try {
            String scriptContent = FileUtils.readFileToString(new File(scriptPath));

            // Split the script into separate statements based on the delimiter
            String[] statements = scriptContent.split(";");

            // Set autocommit to false for transactional behavior
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                for (String statementText : statements) {
                    // Skip empty statements
                    if (statementText.trim().isEmpty()) {
                        continue;
                    }

                    // Execute each statement
                    statement.execute(statementText.trim());
                }

                // If execution is successful, insert into 'updates' with status 1
                updateUpdatesTable(scriptId, comment, 1);

                // Commit the transaction
                connection.commit();

            } catch (SQLException e) {
                // If there is an exception, insert into 'updates' with status 0
                updateUpdatesTable(scriptId, comment, 0);

                // Rollback the transaction
                connection.rollback();
            } finally {
                // Restore autocommit to true
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isScriptExecuted(String scriptId) {
        try {
            String query = "SELECT COUNT(*) FROM updates WHERE script_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, scriptId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void updateUpdatesTable(String scriptId, String comment, int status) {
        try {
            String insertQuery = "INSERT INTO updates (script_id, comment, status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, scriptId);
                statement.setString(2, comment);
                statement.setInt(3, status);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeTriggers() {
        try {
            // Load the XML configuration file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/core/database-updates.xml"));

            // Get the list of trigger nodes
            NodeList triggerNodes = document.getElementsByTagName("trigger");

            // Iterate through each trigger node
            for (int i = 0; i < triggerNodes.getLength(); i++) {
                Element triggerElement = (Element) triggerNodes.item(i);

                // Extract information from the XML
                String triggerId = triggerElement.getElementsByTagName("id").item(0).getTextContent();
                String triggerPath = triggerElement.getElementsByTagName("path").item(0).getTextContent();
                String comment = triggerElement.getElementsByTagName("comment").item(0).getTextContent();

                // Check if the trigger has been executed
                if (!isTriggerExecuted(triggerId)) {
                    // Log the trigger information
                    System.out.println("Executing trigger: " + triggerId + " - " + comment);

                    // Execute the SQL trigger script and update the 'triggers' table
                    executeTriggerAndUpdateTable("src/core/" + triggerPath, triggerId, comment);
                } else {
                    // Log that the trigger has already been executed
                    System.out.println("Trigger already executed: " + triggerId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isTriggerExecuted(String triggerId) {
        try {
            String query = "SELECT COUNT(*) FROM triggers WHERE trigger_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, triggerId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void executeTriggerAndUpdateTable(String triggerPath, String triggerId, String comment) {
        try {
            String triggerContent = FileUtils.readFileToString(new File(triggerPath));

            // Split the trigger content into separate statements based on the delimiter
            String[] statements = triggerContent.split(";");

            // Set autocommit to false for transactional behavior
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                for (String statementText : statements) {
                    // Skip empty statements
                    if (statementText.trim().isEmpty()) {
                        continue;
                    }

                    // Execute each statement
                    statement.execute(statementText.trim());
                }

                // If execution is successful, insert into 'triggers' with status 1
                updateTriggersTable(triggerId, comment, 1);

                // Commit the transaction
                connection.commit();

            } catch (SQLException e) {
                // If there is an exception, insert into 'triggers' with status 0
                updateTriggersTable(triggerId, comment, 0);

                System.out.println(e.getMessage());

                // Rollback the transaction
                connection.rollback();
            } finally {
                // Restore autocommit to true
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateTriggersTable(String triggerId, String comment, int status) {
        try {
            String insertQuery = "INSERT INTO triggers (trigger_id, comment, status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, triggerId);
                statement.setString(2, comment);
                statement.setInt(3, status);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
