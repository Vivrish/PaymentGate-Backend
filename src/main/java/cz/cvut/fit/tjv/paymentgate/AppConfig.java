package cz.cvut.fit.tjv.paymentgate;

import cz.cvut.fit.tjv.paymentgate.repository.BankCardRepository;
import cz.cvut.fit.tjv.paymentgate.repository.TransactionRepository;
import cz.cvut.fit.tjv.paymentgate.repository.UserRepository;
import cz.cvut.fit.tjv.paymentgate.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    BankCardService bankCardService(BankCardRepository bankCardRepository) {
        return new BankCardServiceImpl(bankCardRepository);
    }
    @Bean
    TransactionService transactionService(TransactionRepository transactionRepository) {
        return new TransactionServiceImpl(transactionRepository);
    }
    @Bean
    UserService userService(UserRepository userRepository, BankCardRepository bankCardRepository, TransactionRepository transactionRepository) {
        return new UserServiceImpl(userRepository, bankCardRepository, transactionRepository);
    }


}
