package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;

/**
 * Interface dictates what every service of bankcards must be capable of doing
 */
public interface BankCardService extends CrudService<BankCard, Long> {
    BankCard getByNumber(int number);

    Iterable<User> getHolders(Long cardId);

    Iterable<User> getHolders(int number);

    void updateByNumber(int number, BankCard bankCard);

    void deleteByNumber(int number);

    void deactivate(int number);

    void activate(int number);
}
