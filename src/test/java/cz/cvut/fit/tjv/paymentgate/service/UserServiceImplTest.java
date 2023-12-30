package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest extends ServiceIntegrationTest{

    @Test
    void create() {
        userService.create(new User("Bob", "Dillman", "bobdilman@gmail.com", "bobCreate", "Password1"));
        User returnedUser = userService.getByLogin("bobCreate");
        assertEquals("Bob", returnedUser.getName());
        assertEquals("Dillman", returnedUser.getSurname());
        assertEquals("bobdilman@gmail.com", returnedUser.getEmail());
        assertEquals("bobCreate", returnedUser.getLogin());
        assertEquals("Password1", returnedUser.getPassword());
    }

    @Test
    void addTransaction() throws ParseException {
        Transaction transaction = new Transaction(dateFormat.parse("January 13, 2020 10:00:00"), "CZK", 500.0);
        userService.addTransaction(bob.getLogin(), billy.getLogin(), transaction);
        Iterable<Transaction> bobTransactions = userService.getOutgoingTransactions(bob.getLogin());
        Iterable<Transaction> billyTransactions = userService.getIngoingTransactions(billy.getLogin());
        ArrayList<Transaction> bobArray = new ArrayList<>();
        ArrayList<Transaction> billyArray = new ArrayList<>();
        bobTransactions.forEach(bobArray::add);
        billyTransactions.forEach(billyArray::add);

        assertEquals(bobArray.size(), 1);
        assertEquals(billyArray.size(), 1);
        assertEquals(bobArray.get(0).getSender().getLogin(), bob.getLogin());
        assertEquals(bobArray.get(0).getReceiver().getLogin(), billy.getLogin());
        assertEquals(billyArray.get(0).getSender().getLogin(), bob.getLogin());
        assertEquals(billyArray.get(0).getReceiver().getLogin(), billy.getLogin());
        assertEquals(transaction.getReceiver().getLogin(), billy.getLogin());
        assertEquals(transaction.getSender().getLogin(), bob.getLogin());
        assertEquals(transaction.getCurrency(), "CZK");
        assertEquals(transaction.getAmount(), 500.0);
    }

    @Test
    void deleteByLogin() {
        userService.deleteByLogin(bob.getLogin());
        assertFalse(userRepository.existsByLogin(bob.getLogin()));
    }

    @Test
    void updateByLogin() {
        User newBob = new User("Dill", "Bobman", "dillbobman@gmail.com", "dill", "Password1");
        userService.updateByLogin(bob.getLogin(), newBob);
        assertTrue(userRepository.existsByLogin("dill"));
        assertEquals(userService.getByLogin("dill").getLogin(), newBob.getLogin());
        assertEquals(userService.getByLogin("dill").getName(), newBob.getName());
        assertEquals(userService.getByLogin("dill").getPreferredCardNumber(), newBob.getPreferredCardNumber());
        assertEquals(userService.getByLogin("dill").getSurname(), newBob.getSurname());
        assertEquals(userService.getByLogin("dill").getEmail(), newBob.getEmail());
        assertEquals(userService.getByLogin("dill").getPassword(), newBob.getPassword());
    }

    @Test
    void choosePreferred() {

        userService.addBankCard(bob.getLogin(), cardOther.getCardNumber());
        userService.choosePreferred(bob.getLogin(), cardOther.getCardNumber());
        User testUser = userService.getByLogin(bob.getLogin());
        assertFalse(testUser.getPreferred().isEmpty());
        assertEquals(testUser.getPreferred().get(), cardOther);
    }
}