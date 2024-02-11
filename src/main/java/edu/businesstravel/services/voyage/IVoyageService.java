package edu.businesstravel.services.voyage;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Voyage;

import java.util.List;
import java.util.Set;

public interface IVoyageService {
    Voyage matchingCompanions(Voyage voyage);

    List<Companion> getCompanions(Long voyageId);

    Voyage add(Voyage voyage);

    Voyage update(Voyage voyage);

    Voyage getById(Long voyageId);

    List<Voyage> getAll();

    List<Voyage> getAllByEmployeeEntrepriseId(Long id);

    List<Voyage> getAllByEmployeeId(Long id);

    Set<String> findAvailableDestinations();

    Boolean isExistNonAffectedCompanions();
}
