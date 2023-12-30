package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.Transaction;

/**
 * Interface dictates what every service of transactions must be capable of doing
 */
public interface TransactionService extends CrudService<Transaction, Long> {
}
