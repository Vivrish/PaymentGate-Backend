package cz.cvut.fit.tjv.paymentgate.dto;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.domain.User;

import java.util.ArrayList;
import java.util.List;
/**
 * Class is responsible for converting DTOs to entities and back
 */
public class Mapper {
    public static AnonymousUserDTO toUserDto(User userEntity) {
        return new AnonymousUserDTO(userEntity.getName(), userEntity.getSurname(), userEntity.getLogin(), userEntity.getPreferredCardNumber());
    }

    public static User toUserEntity(FullUserDto inputUserDto) {
        return new User(inputUserDto.getName(), inputUserDto.getSurname(), inputUserDto.getEmail(), inputUserDto.getLogin(), inputUserDto.getPassword());
    }

    public static AnonymousUserWithTransactionsDTO toUserWithTransactionsDto(User userEntity) {
        AnonymousUserWithTransactionsDTO user = new AnonymousUserWithTransactionsDTO();
        user.setName(userEntity.getName());
        user.setLogin(userEntity.getLogin());
        List<AnonymousTransactionDTO> anonymousTransactionDTOS = new ArrayList<>();
        for (Transaction transactionEntity: userEntity.getTransactionsSent()) {
            anonymousTransactionDTOS.add(toTransactionDto(transactionEntity));
        }
        for (Transaction transactionEntity: userEntity.getTransactionsReceived()) {
            anonymousTransactionDTOS.add(toTransactionDto(transactionEntity));
        }
        user.setTransactions(anonymousTransactionDTOS);
        return user;
    }

    public static AnonymousTransactionDTO toTransactionDto(Transaction transactionEntity) {
        return new AnonymousTransactionDTO(
                transactionEntity.getDate(),
                transactionEntity.getCurrency(),
                transactionEntity.getAmount(),
                toUserDto(transactionEntity.getSender()),
                toUserDto(transactionEntity.getReceiver()),
                transactionEntity.getSuccessful());
    }

    public static Transaction toTransactionEntity(AnonymousTransactionDTO anonymousTransactionDTO) {
        return new Transaction(
                anonymousTransactionDTO.getDate(),
                anonymousTransactionDTO.getCurrency(),
                anonymousTransactionDTO.getAmount()
        );
    }

    public static AnonymousBankCardDTO toBankCardDto(BankCard bankCard) {
        AnonymousBankCardDTO anonymousBankCardDTO = new AnonymousBankCardDTO(
                bankCard.getCardNumber(),
                bankCard.getCurrency(),
                bankCard.getBalance(),
                bankCard.isActive()
        );
        if (bankCard.getHolders() == null) {
            return anonymousBankCardDTO;
        }
        for (User holder: bankCard.getHolders()) {
            anonymousBankCardDTO.addHolder(toUserDto(holder));
        }
        return anonymousBankCardDTO;
    }

    public static BankCard toBankCardEntity(FullBankCardDTO inputBankCardDTO) {
        return new BankCard(
                inputBankCardDTO.getCardNumber(),
                inputBankCardDTO.getCvv(),
                inputBankCardDTO.getExpirationDate(),
                inputBankCardDTO.getCurrency(),
                inputBankCardDTO.getBalance(),
                inputBankCardDTO.getActive()
        );
    }




}
