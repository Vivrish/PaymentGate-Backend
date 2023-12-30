package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.exception.*;
import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import cz.cvut.fit.tjv.paymentgate.repository.UserRepository;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class is responsible for checking format and correctness of input and output entities
 */
public class Validator {

    /**
     * @param uniqCheck tells if it is needed to check repository for duplicate identifying fields
     */
    public static void validateUser(User user, UserRepository userRepository, boolean uniqCheck) throws WrongFormatException, LoginAlreadyExistsException{
        validateName(user.getName());
        validateName(user.getSurname());

        validateLogin(user.getLogin(), userRepository, uniqCheck);

        validatePassword(user.getPassword());
    }
    /**
     * @param uniqCheck tells if it is needed to check repository for duplicate identifying fields
     */
    public static void validateBankCard(BankCard bankCard, BankCardRepository bankCardRepository, boolean uniqCheck) {
        validateCardNumber(bankCard.getCardNumber(), bankCardRepository, uniqCheck);
        validateCurrency(bankCard.getCurrency());
        validateCvv(bankCard.getCvv());
    }

    public static void validateTransaction(Transaction transaction) {
        validateCurrency(transaction.getCurrency());
        validateCurrenciesCompatability(transaction.getSender(), transaction.getReceiver());
        validateBalance(transaction.getAmount(), transaction.getSender());
        }

    private static void validatePassword(String password) throws WrongFormatException {
        if (password.length() < 7) {
            throw new WrongFormatException("Password must contain at least 7 symbols");
        }
        if (password.length() > 50) {
            throw new WrongFormatException("Password must not be longer than 50 symbols");
        }
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).+$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new WrongFormatException("Password must contain at least one uppercase letter and one digit");
        }

    }
    private static void validateName(String name) throws WrongFormatException{
        if (name.length() < 2 || name.length() > 50) {
            throw new WrongFormatException("(Sur)Name must be 2-50 chars long");
        }
    }
    private static void validateLogin(String login, UserRepository userRepository, boolean uniqCheck) throws WrongFormatException, LoginAlreadyExistsException {
        if (login.length() < 2 || login.length() > 50) {
            throw new WrongFormatException("Login must be 2-50 chars long");
        }
        if (!uniqCheck) {
            return;
        }
        if (userRepository.existsByLogin(login)) {
            throw new LoginAlreadyExistsException();
        }
    }

    private static void validateCardNumber(int number, BankCardRepository bankCardRepository, boolean uniqCheck) throws CardNumberAlreadyExistsException {
        if (number < 1000 || number > 9999) {
            throw new WrongFormatException("Card number must be in range [1000-9999] Actual:" + number);
        }
        if (!uniqCheck) {
            return;
        }
        if (bankCardRepository.existsByNumber(number)) {
            throw new CardNumberAlreadyExistsException();
        }
    }
    private static void validateCvv(int cvv) {
        if (cvv < 100 || cvv > 999) {
            throw new WrongFormatException("Cvv must be in range [100-999]");
        }
    }
    private static void validateCurrency(String currency) {
        HashSet<String> currencies = new HashSet<>(Arrays.asList("CZK", "EUR", "USD"));
        if (!currencies.contains(currency)) {
            throw new CurrencyDoesNotExistException();
        }
    }

    private static void validateCurrenciesCompatability(User userOne, User userOther) {

        BankCard bankCardOne = userOne.getPreferred().orElseThrow(NoPreferredCardException::new);
        BankCard bankCardOther = userOther.getPreferred().orElseThrow(NoPreferredCardException::new);
        if (!bankCardOne.isActive() || !bankCardOther.isActive()) {
            throw new OperationOnDeactivatedCardException();
        }
        if (!Objects.equals(bankCardOther.getCurrency(), bankCardOne.getCurrency())) {
            throw new CurrenciesDoNotMatchException();
        }
    }

    private static void validateBalance(double transactionAmount, User sender) {
        BankCard card = sender.getPreferred().orElseThrow(NoPreferredCardException::new);
        if (card.getBalance() < transactionAmount) {
            throw new LowBalanceException();
        }
    }
}
