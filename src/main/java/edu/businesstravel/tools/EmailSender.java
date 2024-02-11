/**
 * EmailSender class provides functionality to send emails using the Mailjet API.
 * It includes a method to send emails with dynamic content and supports placeholder replacement.
 * The class also contains an inner class, UserData, to represent user-specific data for email content.
 *
 * @author Hassen MAOUA
 * @version 1.0
 */
package edu.businesstravel.tools;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class EmailSender {
    private static final String API_KEY = "0f5e6d26d372113cfbcc6a2fc8cc2dc6";
    private static final String API_SECRET = "4291cd9ce25a196e97c1300dadead3a0";
    private static final String SENDER_EMAIL = "hassen.maoua@esprit.tn";

    /**
     * Main method demonstrating how to use the sendEmail method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Example of how to use the sendEmail method
        UserData userData = new UserData("John Doe", "john.doe@example.com", "your_password123", "Employee", "Your Company");
        sendEmail( "cavoma5817@bitofee.com", userData);
    }

    /**
     * Sends an email using Mailjet API with dynamic content and placeholder replacement.
     *
     * @param recipientEmail The email address of the recipient.
     * @param userData       The UserData object containing dynamic user-specific information.
     */
    public static void sendEmail(String recipientEmail, UserData userData) {
        try {
            // Read email content from an external file using absolute path
            String projectPath = System.getProperty("user.dir");
            String filePath = projectPath + "/src/main/java/edu/businesstravel/tools/body_template.html";
            String bodyTemplate = readFromFile(filePath);

            // Map to store dynamic values
            Map<String, String> dynamicValues = new HashMap<>();
            dynamicValues.put("user_name", userData.getUserName());
            dynamicValues.put("user_email", userData.getUserEmail());
            dynamicValues.put("user_password", userData.getUserPassword());
            dynamicValues.put("user_role", userData.getUserRole());
            dynamicValues.put("company_name", userData.getCompanyName());

            // Replace placeholders with actual values
            String body = replacePlaceholders(bodyTemplate, dynamicValues);

            // Mailjet API endpoint
            String endpoint = "https://api.mailjet.com/v3.1/send";

            // Create HttpClient
            HttpClient client = HttpClientBuilder.create().build();

            // Create and configure the HTTP request
            HttpPost request = new HttpPost(endpoint);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((API_KEY + ":" + API_SECRET).getBytes()));

            // Escape double quotes in the HTML content
            body = body.replace("\"", "\\\"");

            // Prepare the email data
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonData = objectMapper.createObjectNode();

            ObjectNode messageNode = objectMapper.createObjectNode();
            messageNode.put("From", objectMapper.createObjectNode().put("Email", SENDER_EMAIL));
            ArrayNode toNode = objectMapper.createArrayNode();
            toNode.add(objectMapper.createObjectNode().put("Email", recipientEmail));
            messageNode.set("To", toNode);
            messageNode.put("Subject", "Account Created Successfully");
            messageNode.put("HTMLPart", body);

            ArrayNode messagesNode = objectMapper.createArrayNode();
            messagesNode.add(messageNode);

            jsonData.set("Messages", messagesNode);

            String data = jsonData.toString();

            request.setEntity(new StringEntity(data, "UTF-8"));

            // Send the email
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("Email sent successfully! Response: " + response.getStatusLine().getStatusCode());
            } else {
                System.out.println("Error sending email. Response: " + response.getStatusLine().getStatusCode());
                System.out.println("Response content: " + readResponseContent(response));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readResponseContent(HttpResponse response) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static String replacePlaceholders(String template, Map<String, String> dynamicValues) {
        for (Map.Entry<String, String> entry : dynamicValues.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            template = template.replace(placeholder, entry.getValue());
        }
        return template;
    }

    /**
     * Inner class to represent user data for email content.
     */
    public static class UserData {
        private String userName;
        private String userEmail;
        private String userPassword;
        private String userRole;
        private String companyName;

        /**
         * Constructs a UserData object with user-specific information.
         *
         * @param userName     The user's name.
         * @param userEmail    The user's email address.
         * @param userPassword The user's password.
         * @param userRole     The user's role.
         * @param companyName  The user's company name.
         */
        public UserData(String userName, String userEmail, String userPassword, String userRole, String companyName) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.userRole = userRole;
            this.companyName = companyName;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public String getUserRole() {
            return userRole;
        }

        public String getCompanyName() {
            return companyName;
        }
    }
}