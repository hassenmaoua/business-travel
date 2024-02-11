package edu.businesstravel.repository.category;

import edu.businesstravel.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Optional<Object> save(Object o);

    Optional<Category> findById(Long id);

    List<Category> findAll();

   void   deleteOne(Long id);
   void saveAll(ArrayList<Object> list);



    void update (Object o, Long id);

    boolean ifExist(Long id);
}

