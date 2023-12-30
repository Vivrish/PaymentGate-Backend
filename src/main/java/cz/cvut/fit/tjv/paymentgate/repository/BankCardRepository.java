package cz.cvut.fit.tjv.paymentgate.repository;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface BankCardRepository extends JpaRepository<BankCard, Long> {


    @Query("select card from BankCard card where card.cardNumber = ?1")
    Optional<BankCard> findByNumber(int bankCardNumber);

    @Query("SELECT EXISTS(SELECT 1 FROM BankCard WHERE cardNumber = ?1) AS exists")
    boolean existsByNumber(int number);
}
