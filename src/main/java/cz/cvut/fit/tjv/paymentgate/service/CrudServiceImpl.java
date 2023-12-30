package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.EntityWithId;
import cz.cvut.fit.tjv.paymentgate.exception.IdDoesNotExistException;

public abstract class CrudServiceImpl<T extends EntityWithId<ID>, ID> implements CrudService<T, ID> {

    @Override
    public T create(T obj) {
        return getRepository().save(obj);
    }

    @Override
    public void update(ID id, T obj) {
        if (!getRepository().existsById(id)) {
            throw new IdDoesNotExistException();
        }
        getRepository().deleteById(id);
        obj.setId(id);
        getRepository().save(obj);
    }

    @Override
    public T readById(ID id) {
        return getRepository().findById(id).orElseThrow(IdDoesNotExistException::new);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void deleteById(ID id) {
        if (!getRepository().existsById(id)) {
            return;
        }
        getRepository().deleteById(id);
    }

}
