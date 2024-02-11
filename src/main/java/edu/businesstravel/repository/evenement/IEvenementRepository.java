package edu.businesstravel.repository.evenement;

import edu.businesstravel.entities.Category;
import edu.businesstravel.entities.Evenement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IEvenementRepository {
    Optional<Object> save(Object o);

    Optional<Evenement> findById(Long id);

    List<Object> findAll();

   void   deleteOne(Long id);
   void saveAll(ArrayList<Object> list);



    void update (Object o, Long id);

    boolean ifExist(Long id);
}

