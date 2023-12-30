package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.exception.*;
import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import cz.cvut.fit.tjv.paymentgate.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Unit test for validator
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ValidatorTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BankCardRepository bankCardRepository;

    @Test
    void validateUser() throws Exception {
        User testUser = new User("Bob", "Dillman", "bobdilman@gmail.com", "bob", "Password1");
        userAlreadyExists(testUser);
        testUser.setName("A");
        wrongFormat(testUser);
        testUser.setName("Bob");
        testUser.setPassword("simplepassword");
        wrongFormat(testUser);
        testUser.setPassword("Password1");
        correctUser(testUser);
    }


    @Test
    void validateBankCard() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        BankCard testCard = new BankCard(1234, 123, dateFormat.parse("January 1, 2023 12:00:00"), "CZK", 1000.0, true);
        cardAlreadyExists(testCard);
        testCard.setCvv(1234);
        wrongFormat(testCard);
        testCard.setCvv(123);
        testCard.setCurrency("IAN");
        wrongFormat(testCard);
        testCard.setCurrency("CZK");
        testCard.setCardNumber(12345);
        wrongFormat(testCard);
        testCard.setCardNumber(1234);
        correctCard(testCard);
    }

    @Test
    void validateTransaction() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        Transaction testTransaction = new Transaction(dateFormat.parse("January 1, 2023 12:00:00"), "CZK", 100.0);
        User sender = new User("Bob", "Dillman", "bobdilman@gmail.com", "bob", "Password1");
        User receiver = new User("Billy", "Bones", "billybones@gmail.com", "bil", "Password2");
        BankCard cardOne = new BankCard(1234, 123, dateFormat.parse("January 1, 2023 12:00:00"), "CZK", 5000.0, true);
        BankCard cardOther = new BankCard(4321, 321, dateFormat.parse("January 1, 2022 10:00:00"), "CZK", 1000.0, true);
        sender.setBankCards(List.of(cardOne));
        sender.setPreferredCardNumber(cardOne.getCardNumber());
        receiver.setBankCards(List.of(cardOther));
        receiver.setPreferredCardNumber(cardOther.getCardNumber());
        testTransaction.setSender(sender);
        testTransaction.setReceiver(receiver);
        correctTransaction(testTransaction);

        cardOne.setBalance(0.0);
        sender.setBankCards(List.of(cardOne));
        testTransaction.setSender(sender);
        wrongFormat(testTransaction);

    }


    private void userAlreadyExists(User testUser) throws Exception {
        when(userRepository.existsByLogin("bob")).thenReturn(true);
        boolean exceptionCaught = false;
        try {
            Validator.validateUser(testUser, userRepository, true);
        }
        catch (LoginAlreadyExistsException exception) {exceptionCaught = true;}
        if (!exceptionCaught) {
            throw new Exception();
        }
    }

    private void cardAlreadyExists(BankCard bankCard) throws Exception {
        when(bankCardRepository.existsByNumber(1234)).thenReturn(true);
        boolean exceptionCaught = false;
        try {
            Validator.validateBankCard(bankCard, bankCardRepository, true);
        }
        catch (CardNumberAlreadyExistsException exception) {exceptionCaught = true;}
        if (!exceptionCaught) {
            throw new Exception();
        }
    }

    private void wrongFormat(User testUser) throws Exception {
        when(userRepository.existsByLogin("bob")).thenReturn(false);
        boolean exceptionCaught = false;
        try {
            Validator.validateUser(testUser, userRepository, false);
        }
        catch (WrongFormatException exception) {
            exceptionCaught = true;
        }
        if (!exceptionCaught) {
            throw new Exception();
        }
    }

    private void wrongFormat(Transaction testTransaction) throws Exception {
        boolean exceptionCaught = false;
        try {
            Validator.validateTransaction(testTransaction);
        }
        catch (WrongFormatException | CurrenciesDoNotMatchException | LowBalanceException exception) {
            exceptionCaught = true;
        }
        if (!exceptionCaught) {
            throw new Exception();
        }
    }
    private void wrongFormat(BankCard bankCard) throws Exception {
        when(bankCardRepository.existsByNumber(12345)).thenReturn(false);
        boolean exceptionCaught = false;
        try {
            Validator.validateBankCard(bankCard, bankCardRepository, false);
        }
        catch (WrongFormatException | CurrencyDoesNotExistException exception) {
            exceptionCaught = true;
        }
        if (!exceptionCaught) {
            throw new Exception();
        }
    }

    private void correctUser(User testUser) throws Exception {
        when(userRepository.existsByLogin("bob")).thenReturn(false);
        boolean exceptionCaught = false;
        try {
            Validator.validateUser(testUser, userRepository, false);
        }
        catch (WrongFormatException exception) {
            exceptionCaught = true;
        }
        if (exceptionCaught) {
            throw new Exception();
        }
    }

    private void correctCard(BankCard bankCard) throws Exception {
        when(bankCardRepository.existsByNumber(12345)).thenReturn(false);
        boolean exceptionCaught = false;
        try {
            Validator.validateBankCard(bankCard, bankCardRepository, false);
        }
        catch (WrongFormatException exception) {
            exceptionCaught = true;
        }
        if (exceptionCaught) {
            throw new Exception();
        }
    }
    private void correctTransaction(Transaction transaction) throws Exception {

        boolean exceptionCaught = false;
        try {
            Validator.validateTransaction(transaction);
        }
        catch (WrongFormatException exception) {
            exceptionCaught = true;
        }
        if (exceptionCaught) {
            throw new Exception();
        }
    }
}