package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.repository.TransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;



@Service
public class TransactionServiceImpl extends CrudServiceImpl<Transaction, Long> implements TransactionService {
    /**
     * @dependecyInjection
     */
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public JpaRepository<Transaction, Long> getRepository() {
        return transactionRepository;
    }


}



