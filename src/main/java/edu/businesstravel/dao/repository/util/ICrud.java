package edu.businesstravel.dao.repository.util;

import edu.businesstravel.dao.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ICrud {
    Optional<Object> save(Object o);

    Object findById(Long id);

    List<Object> findAll();

   void   deleteOne(Long id);
   void saveAll(ArrayList<Object> list);



    void update (Object o, Long id);

    boolean ifExist(Long id);
}

