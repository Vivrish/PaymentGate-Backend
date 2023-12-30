package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;

import java.util.Date;
/**
 * Interface dictates what every service of users must be capable of doing
 */
public interface UserService extends CrudService<User, Long>{

    void addBankCard(Long userId, Long bankCardId);

    Transaction addTransaction(Long senderId, Long receiverId, Transaction transaction);

    Iterable<Transaction> getIngoingTransactions(String login);
    Iterable<Transaction> getOutgoingTransactions(String login);

    User getByLogin(String login);

    Transaction addTransaction(String senderLogin, String receiverLogin, Transaction transaction);

    void deleteByLogin(String login);

    void updateByLogin(String login, User user);

    void addBankCard(String login, int bankCardNumber);

    Iterable<BankCard> getCards(String login);

    Iterable<BankCard> getCards(Long userId);

    void choosePreferred(Long userId, Long bankCardId);

    void choosePreferred(String login, int cardNumber);

    Iterable<BankCard> getActiveCards(String login);
}
