package edu.businesstravel.services.companion;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;
import edu.businesstravel.repository.companion.CompanionRepository;
import edu.businesstravel.tools.DatabaseConnection;
import edu.businesstravel.tools.Helpers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CompanionService implements ICompanionService{
    private final CompanionRepository companionRepository;

    public CompanionService() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        companionRepository = new CompanionRepository(connection);
    }

    @Override
    public Companion initCompanion(User user, Evenement evenement){
        Companion c = new Companion();

        c.setEmployee(user);
        c.setDomaineActivite(user.getEntreprise().getDomaine().getNom());
        c.setNom(user.getNom() + " " + user.getPrenom());
        c.setEvenement(evenement);
        c.setAge(Helpers.calculateAge(user.getDateNaissance()));

        return c;
    }

    @Override
    public Companion add(Companion companion) {
        // Implement the logic to add a new companion
        return companionRepository.save(companion);
    }

    @Override
    public Companion update(Companion companion) {
        // Implement the logic to update an existing companion only if companion.getIdCompanion is not null
        if (companion.getIdCompanion() == 0) {
            throw new IllegalArgumentException("Companion ID cannot be null for update");
        }
        return companionRepository.save(companion);
    }

    @Override
    public Companion getById(Long id) {
        // Implement the logic to retrieve a companion by its ID
        return companionRepository.findById(id)
                .orElse(null); // You can adjust the handling based on your requirements
    }

    @Override
    public List<Companion> getAll() {
        // Implement the logic to retrieve all companions and convert to a List
        List<Companion> companionList = new ArrayList<>();
        companionRepository.findAll().forEach(companionList::add);
        return companionList;
    }

    @Override
    public void remove(Long companionId) {
        // Implement the logic to remove a companion by its ID
        companionRepository.deleteById(companionId);
    }

}
