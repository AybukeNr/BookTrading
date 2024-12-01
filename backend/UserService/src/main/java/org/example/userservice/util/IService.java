package org.example.userservice.util;

import java.util.List;
import java.util.Optional;

public interface IService<T,ID> {
    T save(T entity);
    Iterable<T> saveAll(Iterable<T> entityList);
    T update(T entity);
    void delete(T entity);
    void deleteById(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAllByIds(List<ID> ids);

}