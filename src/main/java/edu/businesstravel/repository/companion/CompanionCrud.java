package edu.businesstravel.repository.companion;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.repository.utils.CrudInterface;

import java.util.List;

public interface CompanionCrud<T, ID> extends CrudInterface<T, ID> {
    List<Companion> findByVoyageId(Long voyageId);

    List<Companion> findUnassignedCompanions();
}
