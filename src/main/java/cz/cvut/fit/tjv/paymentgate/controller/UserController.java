package cz.cvut.fit.tjv.paymentgate.controller;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.dto.*;
import cz.cvut.fit.tjv.paymentgate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class is responsible for managing API for users and their interactions with bank cards and transactions
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class)) })})
    @GetMapping("")
    public Iterable<AnonymousUserDTO> getAll() {
        ArrayList<AnonymousUserDTO> users = new ArrayList<>();
        for (User user: userService.readAll()) {
            users.add(Mapper.toUserDto(user));
        }
        return users;
    }

    @Operation(summary = "Get user by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Login not found",
                    content = @Content) })
    @GetMapping("/login/{login}")
    public AnonymousUserDTO getByLogin(@PathVariable String login) {
        System.out.println("Request get user " + login);
        return Mapper.toUserDto(userService.getByLogin(login));
    }

    @Operation(summary = "Get a user specified by id and their transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user and their transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserWithTransactionsDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User id not found",
                    content = @Content) })
    @GetMapping("/{userId}/transactions")
    public AnonymousUserWithTransactionsDTO getTransactions(@PathVariable Long userId) {
        return Mapper.toUserWithTransactionsDto(userService.readById(userId));
    }
    @Operation(summary = "Get a user specified by id and their transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user and their transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserWithTransactionsDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User login not found",
                    content = @Content) })
    @GetMapping("/login/{login}/transactions")
    public AnonymousUserWithTransactionsDTO getTransactionsByLogin(@PathVariable String login) {
        return Mapper.toUserWithTransactionsDto(userService.getByLogin(login));
    }

    @Operation(summary = "Get bank cards of a user specified by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user and their bank cards",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User login not found",
                    content = @Content) })
    @GetMapping("/login/{login}/cards")
    public Iterable<AnonymousBankCardDTO> getBankCardsByLogin(@PathVariable String login) {
        ArrayList<AnonymousBankCardDTO> bankCards = new ArrayList<>();
        for (BankCard bankCard: userService.getCards(login)) {
            bankCards.add(Mapper.toBankCardDto(bankCard));
        }
        return bankCards;
    }
    @Operation(summary = "Get bank cards of a user specified by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user and their transaction",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User id not found",
                    content = @Content) })
    @GetMapping("/{userId}/cards")
    public Iterable<AnonymousBankCardDTO> getBankCards(@PathVariable Long userId) {
        ArrayList<AnonymousBankCardDTO> bankCards = new ArrayList<>();
        for (BankCard bankCard: userService.getCards(userId)) {
            bankCards.add(Mapper.toBankCardDto(bankCard));
        }
        return bankCards;
    }

    @Operation(summary = "Get active bank cards of a user specified by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user and their active bank cards",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User login not found",
                    content = @Content) })
    @GetMapping("/login/{login}/cards/active")
    public Iterable<AnonymousBankCardDTO> getActiveCards(@PathVariable String login) {
        ArrayList<AnonymousBankCardDTO> cards = new ArrayList<>();
        for (BankCard card: userService.getActiveCards(login)) {
            cards.add(Mapper.toBankCardDto(card));
        }
        return cards;
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Wrong format of one of the submitted fields or login already exists or password is too simple",
                    content = @Content) })
    @PostMapping("/create")
    public AnonymousUserDTO create(@RequestBody FullUserDto user) {
        System.out.println("Request to create user " + user.getLogin());
        return Mapper.toUserDto(userService.create(Mapper.toUserEntity(user)));
    }
    @Operation(summary = "Make a transaction using ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction is made",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Currency not found or sender/receiver doesn't have a preferred card or wrong format in one of the fields",
                    content = @Content),
            @ApiResponse(responseCode = "402", description = "Sender balance is too low",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Currencies don't match or sender/receiver bank card is deactivated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sender or receiver not found",
                    content = @Content)
            })
    @PostMapping("/{senderId}/create/transaction/{receiverId}")
    public AnonymousTransactionDTO createTransaction(@PathVariable Long senderId, @PathVariable Long receiverId, @RequestBody AnonymousTransactionDTO transaction) {
        return Mapper.toTransactionDto(userService.addTransaction(senderId, receiverId, Mapper.toTransactionEntity(transaction)));
    }
    @Operation(summary = "Make a transaction using logins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction is made",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Currency not found or sender/receiver doesn't have a preferred card or wrong format in one of the fields",
                    content = @Content),
            @ApiResponse(responseCode = "402", description = "Sender balance is too low",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Currencies don't match or sender/receiver bank card is deactivated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sender or receiver not found",
                    content = @Content)
    })
    @PostMapping ("/login/{senderLogin}/create/transaction/{receiverLogin}")
    public AnonymousTransactionDTO createTransactionByLogin(@PathVariable String senderLogin, @PathVariable String receiverLogin, @RequestBody AnonymousTransactionDTO transaction) {
        return Mapper.toTransactionDto(userService.addTransaction(senderLogin, receiverLogin, Mapper.toTransactionEntity(transaction)));
    }

    @Operation(summary = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @DeleteMapping( "/{userId}/delete")
    public void delete(@PathVariable("userId") Long id) {
        userService.deleteById(id);
    }

    @Operation(summary = "Delete a user by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Login not found",
                    content = @Content)
    })
    @DeleteMapping("/login/{login}/delete")
    public void deleteByLogin(@PathVariable String login) {
        userService.deleteByLogin(login);
    }
    @Operation(summary = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong format of one of the submitted fields or login already exists or password is too simple",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @PutMapping( "/{userId}/update")
    public void update(@PathVariable Long userId, @RequestBody FullUserDto user) {
        userService.update(userId, Mapper.toUserEntity(user));
    }
    @Operation(summary = "Update a user by login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong format of one of the submitted fields or login already exists or password is too simple",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Login not found",
                    content = @Content)
    })
    @PutMapping("/login/{login}/update")
    public void updateByLogin(@PathVariable String login, @RequestBody FullUserDto user) {
        System.out.println("Request to update user " + user.getLogin());
        userService.updateByLogin(login, Mapper.toUserEntity(user));
    }
    @Operation(summary = "Add existing bank card to a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card is added",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "One of ids not found",
                    content = @Content)
    })
    @PutMapping("/{userId}/add/card/{bankCardId}")
    public void addBankCard(@PathVariable Long bankCardId, @PathVariable Long userId) {
        userService.addBankCard(userId, bankCardId);
    }

    @Operation(summary = "Add existing bank card to a user by login and number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card is added",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id or login not found",
                    content = @Content)
    })
    @PutMapping("/login/{login}/add/card/{bankCardNumber}")
    public void addBankCardByLogin(@PathVariable String login, @PathVariable int bankCardNumber) {
        userService.addBankCard(login, bankCardNumber);
    }
    @Operation(summary = "Choose one of user's bank cards as preferred by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card is added",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "One of ids not found",
                    content = @Content)
    })
    @PutMapping("/{userId}/prefer/{bankCardId}")
    public void choosePreferred(@PathVariable Long userId, @PathVariable Long bankCardId) {
        userService.choosePreferred(userId, bankCardId);
    }
    @Operation(summary = "Choose one of user's bank cards as preferred by login and number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card is added",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Number or login not found",
                    content = @Content)
    })
    @PutMapping("/login/{login}/prefer/{cardNumber}")
    public void choosePreferredByLogin(@PathVariable String login, @PathVariable int cardNumber) {
        userService.choosePreferred(login, cardNumber);
    }

}
