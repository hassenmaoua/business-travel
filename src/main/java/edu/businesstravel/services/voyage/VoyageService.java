package edu.businesstravel.services.voyage;

import edu.businesstravel.entities.*;
import edu.businesstravel.repository.companion.CompanionRepository;
import edu.businesstravel.repository.voyage.VoyageRepository;
import edu.businesstravel.tools.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VoyageService implements IVoyageService {
    private static VoyageRepository voyageRepository;
    private static CompanionRepository companionRepository;

    public VoyageService() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        voyageRepository = new VoyageRepository(connection);
        companionRepository = new CompanionRepository(connection);
    }

    @Override
    public Voyage matchingCompanions(Voyage voyage) {
        // Step 1: Look for only Companions that have Companion.voyage = null
        List<Companion> eligibleCompanions = companionRepository.findUnassignedCompanions();

        if (eligibleCompanions.isEmpty()) {
            System.out.println("Il nya pas de matching.");
            return null;
        }

        // Step 2: Filter Companions based on Evenement.region equals to Voyage.destination
        eligibleCompanions = filterCompanionsByRegion(eligibleCompanions, voyage.getDestination());

        // Step 3: Filter Companions based on the same employer.companie.domaine
        User employer = eligibleCompanions.get(0).getEmployee();
        Entreprise entreprise = eligibleCompanions.get(0).getEmployee().getEntreprise();
        if (employer != null && entreprise != null) {
            Domaine domaine = eligibleCompanions.get(0).getEmployee().getEntreprise().getDomaine();
            eligibleCompanions = filterCompanionsByDomaine(eligibleCompanions, domaine);
        } else {
            // Handle the case where the necessary properties are null
            System.out.println("Unable to filter by domaine due to null properties.");
            return null;
        }

        // Step 4: Filter Companions based on Evenement.dateDebut within 3 days
        eligibleCompanions = filterCompanionsByDate(eligibleCompanions);

        // Step 5: Set voyage.dateDepart automatic before 1 day from the earliest companion.evenement.dateDebut
        setVoyageDateDepart(voyage, eligibleCompanions);

        // Assign eligible companions to the voyage
        for (Companion companion : eligibleCompanions) {
            companion.setVoyage(voyage);
            companionRepository.save(companion);
        }
        return  voyageRepository.save(voyage);
    }

    @Override
    public List<Companion> getCompanions(Long voyageId) {
        // Implement the logic to retrieve companions for a specific voyage
        return companionRepository.findByVoyageId(voyageId);
    }

    @Override
    public Voyage add(Voyage voyage) {
        // Implement the logic to add a new voyage
        return voyageRepository.save(voyage);
    }

    @Override
    public Voyage update(Voyage voyage) {
        // Implement the logic to update an existing voyage only if voyage.getIdVoyage is not null
        if (voyage.getIdVoyage() == 0) {
            throw new IllegalArgumentException("Voyage ID cannot be null for update");
        }
        return voyageRepository.save(voyage);
    }

    @Override
    public Voyage getById(Long voyageId) {
        // Implement the logic to retrieve a voyage by its ID
        return voyageRepository.findById(voyageId)
                .orElse(null); // You can adjust the handling based on your requirements
    }

    @Override
    public List<Voyage> getAll() {
        // Implement the logic to retrieve all voyages and convert to a List
        List<Voyage> voyageList = new ArrayList<>();
        voyageRepository.findAll().forEach(voyageList::add);
        return voyageList;
    }

    @Override
    public List<Voyage> getAllByEmployeeEntrepriseId(Long id) {
        return voyageRepository.getAllByEmployeeEntrepriseId(id);
    }

    @Override
    public List<Voyage> getAllByEmployeeId(Long id) {
        return voyageRepository.getAllByEmployeeId(id);
    }

    @Override
    public Set<String> findAvailableDestinations() {
        return voyageRepository.findAvailableDestinations();
    }

    @Override
    public Boolean isExistNonAffectedCompanions() {
        return voyageRepository.isExistNonAffectedCompanions();
    }


    // Helper methods

    private List<Companion> filterCompanionsByRegion(List<Companion> companions, String destination) {
        List<Companion> filteredCompanions = new ArrayList<>();
        for (Companion companion : companions) {
            if (companion.getEvenement().getRegion().equalsIgnoreCase(destination)) {
                filteredCompanions.add(companion);
            }
        }
        return filteredCompanions;
    }

    private List<Companion> filterCompanionsByDomaine(List<Companion> companions, Domaine domaine) {
        List<Companion> filteredCompanions = new ArrayList<>();
        for (Companion companion : companions) {
            if (companion.getEmployee().getEntreprise().getDomaine() == domaine) {
                filteredCompanions.add(companion);
            }
        }
        return filteredCompanions;
    }

    private List<Companion> filterCompanionsByDate(List<Companion> companions) {
        List<Companion> filteredCompanions = new ArrayList<>();

        if (companions.size() == 0) {
            return filteredCompanions;
        }

        // Iterate through companions to find the minimum and maximum event start dates
        Date minDate = companions.get(0).getEvenement().getDateDebut();
        Date maxDate = companions.get(0).getEvenement().getDateDebut();

        for (Companion companion : companions) {
            Date eventDate = companion.getEvenement().getDateDebut();
            if (eventDate.before(minDate)) {
                minDate = eventDate;
            }
            if (eventDate.after(maxDate)) {
                maxDate = eventDate;
            }
        }

        // Set the desired time difference (3 days in milliseconds)
        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000;

        // Iterate through companions again and add those within the time difference to the filtered list
        for (Companion companion : companions) {
            Date eventDate = companion.getEvenement().getDateDebut();
            long timeDifference = maxDate.getTime() - eventDate.getTime();

            if (timeDifference <= threeDaysInMillis) {
                filteredCompanions.add(companion);
            }
        }

        return filteredCompanions;
    }


    private void setVoyageDateDepart(Voyage voyage, List<Companion> companions) {
        if (!companions.isEmpty()) {
            // Find the earliest companion.evenement.dateDebut
            Date earliestDateDebut = companions.get(0).getEvenement().getDateDebut();
            for (Companion companion : companions) {
                Date evenementDateDebut = companion.getEvenement().getDateDebut();
                if (evenementDateDebut.before(earliestDateDebut)) {
                    earliestDateDebut = evenementDateDebut;
                }
            }

            // Set voyage.dateDepart automatic before 1 day from the earliest companion.evenement.dateDebut
            long oneDayInMillis = 24 * 60 * 60 * 1000;
            voyage.setDateDepart(new Date(earliestDateDebut.getTime() - oneDayInMillis));
        }
    }
}
