package edu.businesstravel.services.companion;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;

import java.util.List;

public interface ICompanionService {
    Companion initCompanion(User user, Evenement evenement);

    Companion add(Companion companion);

    Companion update(Companion companion);

    Companion getById(Long id);

    List<Companion> getAll();

    void remove(Long companionId);
}
