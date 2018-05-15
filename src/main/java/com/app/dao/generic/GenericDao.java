package com.app.dao.generic;


import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    void add(T t);
    void update(T t);
    void delete(Long id);
    Optional<T> findOneById(Long id);
    List<T> findAll();
}
