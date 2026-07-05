package interfaces;

import java.util.List;

public interface IRepository<T> {
    boolean add(T entity);
    boolean remove(int id);
    boolean update(int id, T entity);
    List<T> findAll();
    T findById(int id);
}

