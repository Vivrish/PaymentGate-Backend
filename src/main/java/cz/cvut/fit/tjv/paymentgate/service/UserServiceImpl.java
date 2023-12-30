package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.exception.*;
import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import cz.cvut.fit.tjv.paymentgate.repository.TransactionRepository;
import cz.cvut.fit.tjv.paymentgate.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl extends CrudServiceImpl<User, Long> implements UserService {
    /**
     * @dependecyInjection
     */
    private final UserRepository userRepository;
    /**
     * @dependecyInjection
     */
    private final BankCardRepository bankCardRepository;
    /**
     * @dependecyInjection
     */
    private final TransactionRepository transactionRepository;

    public UserServiceImpl(UserRepository userRepository, BankCardRepository bankCardRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.bankCardRepository = bankCardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public User create(User user) {
        Validator.validateUser(user, userRepository, true);
        return userRepository.save(user);
    }

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }


    @Override
    public Iterable<Transaction> getIngoingTransactions(String login) {
        User user = getUserOrThrow(login);
        return user.getTransactionsReceived();
    }

    @Override
    public Iterable<Transaction> getOutgoingTransactions(String login) {
        User user = getUserOrThrow(login);
        return user.getTransactionsSent();
    }

    @Override
    public User getByLogin(String login) {
        return getUserOrThrow(login);
    }

    @Override
    public Transaction addTransaction(String senderLogin, String receiverLogin, Transaction transaction) {
        User sender = getUserOrThrow(senderLogin);
        User receiver = getUserOrThrow(receiverLogin);
        return addTransaction(sender, receiver, transaction);
    }
    @Override
    public Transaction addTransaction(Long senderId, Long receiverId, Transaction transaction) {
        User sender = getUserOrThrow(senderId);
        User receiver = getUserOrThrow(receiverId);
        return addTransaction(sender, receiver, transaction);
    }

    @Override
    public void deleteByLogin(String login) {
        User user = getUserOrThrow(login);
        userRepository.deleteById(user.getId());
    }

    @Override
    public void updateByLogin(String login, User newUser) {
        if (!userRepository.existsByLogin(login)) {
            throw new LoginDoesNotExistException();
        }
        Validator.validateUser(newUser, userRepository, false);
        deleteByLogin(login);
        userRepository.save(newUser);
    }

    @Override
    public void addBankCard(String login, int bankCardNumber) {
        User user = getUserOrThrow(login);
        BankCard bankCard = bankCardRepository.findByNumber(bankCardNumber).orElseThrow(CardNumberDoesNotExistException::new);
        addBankCard(user, bankCard);
    }
    @Override
    public void addBankCard(Long userId, Long bankCardId) {
        User user = getUserOrThrow(userId);
        BankCard bankCard = bankCardRepository.findById(bankCardId).orElseThrow(IdDoesNotExistException::new);
        addBankCard(user, bankCard);
    }

    @Override
    public Iterable<BankCard> getCards(String login) {
        User user = getUserOrThrow(login);
        return user.getBankCards();
    }

    @Override
    public Iterable<BankCard> getCards(Long userId) {
        User user = getUserOrThrow(userId);
        return user.getBankCards();
    }

    @Override
    public void choosePreferred(Long userId, Long bankCardId) {
        User user = getUserOrThrow(userId);
        BankCard bankCard = bankCardRepository.findById(bankCardId).orElseThrow(IdDoesNotExistException::new);
        choosePreferred(user, bankCard);
    }

    @Override
    public void choosePreferred(String login, int cardNumber) {
        User user = getUserOrThrow(login);
        BankCard bankCard = bankCardRepository.findByNumber(cardNumber).orElseThrow(CardNumberDoesNotExistException::new);
        choosePreferred(user, bankCard);
    }

    @Override
    public Iterable<BankCard> getActiveCards(String login) {
        return userRepository.getActiveCards(login);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(IdDoesNotExistException::new);
    }

    private User getUserOrThrow(String login) {
        return userRepository.findByLogin(login).orElseThrow(LoginDoesNotExistException::new);
    }


    private Transaction addTransaction(User sender, User receiver, Transaction transaction) {
        BankCard senderBankCard;
        BankCard receiverBankCard;

         // If the validator raises any custom exceptions, transaction is aborted and successful is set to false
        try {
            sender.addOutgoingTransaction(transaction);
            receiver.addIngoingTransaction(transaction);
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            Validator.validateTransaction(transaction);
            senderBankCard = sender.getPreferred().orElseThrow(NoPreferredCardException::new);
            senderBankCard.setBalance(senderBankCard.getBalance() - transaction.getAmount());
            receiverBankCard = receiver.getPreferred().orElseThrow(NoPreferredCardException::new);
            receiverBankCard.setBalance(receiverBankCard.getBalance() + transaction.getAmount());
        }
        catch (CurrencyDoesNotExistException
               | CurrenciesDoNotMatchException
               | LowBalanceException
               | OperationOnDeactivatedCardException
               | NoPreferredCardException
               | LoginDoesNotExistException exception
        ) {
            transaction.setSuccessful(false);
            transaction.setAllToDefault();
            return transactionRepository.save(transaction);
        }
        bankCardRepository.save(senderBankCard);
        bankCardRepository.save(receiverBankCard);
        userRepository.save(sender);
        userRepository.save(receiver);
        return transactionRepository.save(transaction);
    }

    private void choosePreferred(User user, BankCard bankCard) {
        // You can't prefer a card you don't own
        for (BankCard userCard: user.getBankCards()) {
            if (userCard.getCardNumber() == bankCard.getCardNumber()) {
                user.setPreferredCardNumber(userCard.getCardNumber());
                userRepository.save(user);
                return;
            }
        }
        throw new CardNumberDoesNotExistException();
    }

    private void addBankCard(User user, BankCard bankCard) {
        user.addBankCard(bankCard);
        bankCard.addHolder(user);
        userRepository.save(user);
        bankCardRepository.save(bankCard);
    }


}
