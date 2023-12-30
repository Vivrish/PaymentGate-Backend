package cz.cvut.fit.tjv.paymentgate.repository;

import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
