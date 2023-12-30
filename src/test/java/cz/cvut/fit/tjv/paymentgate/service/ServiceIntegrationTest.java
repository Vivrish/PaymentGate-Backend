package cz.cvut.fit.tjv.paymentgate.service;


import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import cz.cvut.fit.tjv.paymentgate.repository.TransactionRepository;
import cz.cvut.fit.tjv.paymentgate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * Parent class for all integration tests. Is used for initializing test data
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class ServiceIntegrationTest {
    @Autowired
    protected UserService userService;
    @Autowired
    protected BankCardService bankCardService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected TransactionRepository transactionRepository;
    @Autowired
    protected BankCardRepository bankCardRepository;

    protected User bob;
    protected User billy;
    protected BankCard cardOne;
    protected BankCard cardOther;


    protected final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
    @BeforeEach
    protected void init() throws ParseException {
        transactionRepository.deleteAll();
        bankCardRepository.deleteAll();
        userRepository.deleteAll();
        bob = new User("Bob", "Dillman", "bobdilman@gmail.com", "bob", "Password1");
        billy = new User("Billy", "Bones", "billybones@gmail.com", "bil", "Password2");
        userService.create(bob);
        userService.create(billy);
        cardOne = new BankCard(1234, 123, dateFormat.parse("January 1, 2023 12:00:00"), "CZK", 1000.0, true);
        cardOther = new BankCard(4321, 321, dateFormat.parse("January 1, 2022 10:00:00"), "CZK", 5000.0, true);
        bankCardService.create(cardOne);
        bankCardService.create(cardOther);

        userService.addBankCard(bob.getLogin(), cardOne.getCardNumber());
        userService.addBankCard(billy.getLogin(), cardOther.getCardNumber());
        userService.choosePreferred(bob.getLogin(), cardOne.getCardNumber());
        userService.choosePreferred(billy.getLogin(), cardOther.getCardNumber());

    }
}
