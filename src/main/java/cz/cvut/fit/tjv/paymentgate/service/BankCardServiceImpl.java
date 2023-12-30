package cz.cvut.fit.tjv.paymentgate.service;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.exception.CardNumberDoesNotExistException;
import cz.cvut.fit.tjv.paymentgate.exception.IdDoesNotExistException;
import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BankCardServiceImpl extends CrudServiceImpl<BankCard, Long> implements BankCardService {
    /**
     * @dependecyInjection
     */
    private final BankCardRepository bankCardRepository;


    public BankCardServiceImpl(BankCardRepository bankCardRepository) {
        this.bankCardRepository = bankCardRepository;
    }

    @Override
    public JpaRepository<BankCard, Long> getRepository() {
        return bankCardRepository;
    }

    @Override
    public BankCard getByNumber(int number) {
        return bankCardRepository.findByNumber(number).orElseThrow(CardNumberDoesNotExistException::new);
    }

    @Override
    public Iterable<User> getHolders(Long cardId) {
        BankCard bankCard = bankCardRepository.findById(cardId).orElseThrow(IdDoesNotExistException::new);
        return bankCard.getHolders();
    }

    @Override
    public Iterable<User> getHolders(int number) {
        BankCard bankCard = bankCardRepository.findByNumber(number).orElseThrow(CardNumberDoesNotExistException::new);
        return bankCard.getHolders();
    }

    @Override
    public BankCard create(BankCard bankCard) {
        Validator.validateBankCard(bankCard, bankCardRepository, true);
        return bankCardRepository.save(bankCard);
    }

    @Override
    public void updateByNumber(int number, BankCard newBankCard) {
        if (!bankCardRepository.existsByNumber(number)) {
            throw new CardNumberDoesNotExistException();
        }
        Validator.validateBankCard(newBankCard, bankCardRepository, false);
        deleteByNumber(number);
        bankCardRepository.save(newBankCard);
    }

    @Override
    public void deleteByNumber(int number) {
        BankCard bankCard = bankCardRepository.findByNumber(number).orElseThrow(CardNumberDoesNotExistException::new);
        bankCardRepository.deleteById(bankCard.getId());
    }

    @Override
    public void deactivate(int number) {
        BankCard bankCard = bankCardRepository.findByNumber(number).orElseThrow(CardNumberDoesNotExistException::new);
        bankCard.setActive(false);
        bankCardRepository.save(bankCard);
    }

    @Override
    public void activate(int number) {
        BankCard bankCard = bankCardRepository.findByNumber(number).orElseThrow(CardNumberDoesNotExistException::new);
        bankCard.setActive(true);
        bankCardRepository.save(bankCard);
    }
}
