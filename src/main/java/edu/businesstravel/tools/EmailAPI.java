/**
 * EmailSender class provides functionality to send emails using the Mailjet API.
 * It includes a method to send emails with dynamic content and supports placeholder replacement.
 * The class also contains an inner class, UserData, to represent user-specific data for email content.
 *
 * @author Hassen MAOUA
 * @version 1.0
 */
package edu.businesstravel.tools;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.User;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.services.companion.CompanionService;
import edu.businesstravel.services.voyage.VoyageService;
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


public class EmailAPI {
    private static final String API_KEY = "5afb56514e6cb626ed0348abf53f63da";
    private static final String API_SECRET = "7af2227f41ec4c9e8f758403fd418fa3";
    private static final String SENDER_EMAIL = "yassine.raouafi@esprit.tn";


    public static void main(String[] args) {
        // Example of how to use the sendEmail method
        CompanionService companionService = new CompanionService();
        VoyageService voyageService = new VoyageService();

        Companion companion = companionService.getById(17L);
        Voyage voyage = voyageService.getById(35L);

        String email = companion.getEmployee().getEmail();

        sendEmail(email, companion, voyage);
    }


    public static void sendEmail(String recipientEmail, Companion companion, Voyage voyage) {
        try {
            // Read email content from an external file using absolute path
            String projectPath = System.getProperty("user.dir");
            String filePath = projectPath + "/src/main/java/edu/businesstravel/tools/voyage_template.html";
            String bodyTemplate = readFromFile(filePath);

            // Map to store dynamic values
            Map<String, String> dynamicValues = new HashMap<>();
            dynamicValues.put("user_name", companion.getEmployee().getNom());
            dynamicValues.put("voyage_name", voyage.getNom());
            dynamicValues.put("destination", voyage.getDestination());
            dynamicValues.put("datedepart", voyage.getDateDepart().toString());
            dynamicValues.put("evenement", companion.getEvenement().getTitre());
            dynamicValues.put("company_name", companion.getEmployee().getEntreprise().getNomEntreprise());

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
            messageNode.put("Subject", "Affectation du Voyage");
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
}