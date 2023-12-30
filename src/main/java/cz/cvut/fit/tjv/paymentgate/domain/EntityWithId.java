package cz.cvut.fit.tjv.paymentgate.domain;

public interface EntityWithId<ID> {
    ID getId();
    void setId(ID id);
}
