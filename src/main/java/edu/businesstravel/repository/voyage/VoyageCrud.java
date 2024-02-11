package edu.businesstravel.repository.voyage;

import edu.businesstravel.repository.utils.CrudInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface VoyageCrud<T, ID> extends CrudInterface<T, ID> {
    List<T> getAllByEmployeeEntrepriseId(Long entrepriseId);

    List<T> getAllByEmployeeId(Long id);

    Set<String> findAvailableDestinations();

    Boolean isExistNonAffectedCompanions();
    Map<String, Integer> getCompanionCountByRegion();

}
