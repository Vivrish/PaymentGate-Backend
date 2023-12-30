package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.EntityWithId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface dictates what every service should be capable of doing
 * @param <T> Type of the entity (e.g. User, BankCard ...)
 * @param <ID> Type of the entity's id
 */
public interface CrudService<T extends EntityWithId<ID>, ID> {
    T create(T obj);
    void update(ID id, T obj) throws IllegalArgumentException;
    T readById(ID id) throws IllegalArgumentException;
    Iterable<T> readAll();
    void deleteById(ID id) throws IllegalArgumentException;

    JpaRepository<T, ID> getRepository();

}
