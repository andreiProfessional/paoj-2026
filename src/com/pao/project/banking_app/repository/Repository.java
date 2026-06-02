package com.pao.project.banking_app.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD repository interface.
 *
 * @param <T>  entity type
 * @param <ID> primary key type
 */
public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(ID id);
}
