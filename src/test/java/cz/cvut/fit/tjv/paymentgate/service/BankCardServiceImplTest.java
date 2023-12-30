package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BankCardServiceImplTest extends ServiceIntegrationTest {

    @Test
    void getHolders() {
        Iterable<User> users = bankCardService.getHolders(cardOne.getCardNumber());
        assertNotNull(users);
        ArrayList<User> userList = new ArrayList<>();
        users.forEach(userList::add);
        assertEquals(userList.size(), 1);
        assertEquals(userList.get(0).getLogin(), bob.getLogin());
    }
}