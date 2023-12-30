package cz.cvut.fit.tjv.paymentgate.repository;


import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT EXISTS(SELECT 1 FROM User WHERE login = ?1) AS exists")
    boolean existsByLogin(String login);

    @Query("select user from User user where user.login = ?1")
    Optional<User> findByLogin(String login);


    @Query("select bankCards from User user" +
            " join user.bankCards bankCards " +
            "where user.login = ?1 and bankCards.active = true")
    List<BankCard> getActiveCards(String login);
}
