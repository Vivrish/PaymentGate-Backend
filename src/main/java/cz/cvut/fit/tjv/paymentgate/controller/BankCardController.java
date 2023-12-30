package cz.cvut.fit.tjv.paymentgate.controller;

import cz.cvut.fit.tjv.paymentgate.domain.BankCard;
import cz.cvut.fit.tjv.paymentgate.domain.User;
import cz.cvut.fit.tjv.paymentgate.dto.*;
import cz.cvut.fit.tjv.paymentgate.service.BankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Class is responsible for managing API of bank cards
 */
@RestController
@RequestMapping("/card")
public class BankCardController {

    /**
     * @dependecyInjection
     */
    private final BankCardService bankCardService;

    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @Operation(summary = "Get all bank cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank cards found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class))} )
    })
    @GetMapping("")
    public Iterable<AnonymousBankCardDTO> get() {
        ArrayList<AnonymousBankCardDTO> bankCards = new ArrayList<>();
        for (BankCard bankCard: bankCardService.readAll()) {
            bankCards.add(Mapper.toBankCardDto(bankCard));
        }
        return bankCards;
    }
    @Operation(summary = "Get a bank card by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @GetMapping("/{cardId}")
    public AnonymousBankCardDTO getById(@PathVariable Long cardId) {
        return Mapper.toBankCardDto(bankCardService.readById(cardId));
    }

    @Operation(summary = "Get a bank card by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Number not found",
                    content = @Content)
    })
    @GetMapping("/number/{number}")
    public AnonymousBankCardDTO getByNumber(@PathVariable int number) {
        return Mapper.toBankCardDto(bankCardService.getByNumber(number));
    }

    @Operation(summary = "Get a bank card holders its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @GetMapping("/{cardId}/holder")
    public Iterable<AnonymousUserDTO> getHolder(@PathVariable Long cardId) {
        ArrayList<AnonymousUserDTO> users = new ArrayList<>();
        for (User user: bankCardService.getHolders(cardId)) {
            users.add(Mapper.toUserDto(user));
        }
        return users;
    }
    @Operation(summary = "Get a bank card holders its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousUserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Number not found",
                    content = @Content)
    })
    @GetMapping("/number/{number}/holder")
    public Iterable<AnonymousUserDTO> getHolderByNumber(@PathVariable int number) {
        ArrayList<AnonymousUserDTO> users = new ArrayList<>();
        for (User user: bankCardService.getHolders(number)) {
            users.add(Mapper.toUserDto(user));
        }
        return users;
    }
    @Operation(summary = "Create a bank card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousBankCardDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Wrong format in one of the fields or currency does not exist or card number already exists",
                    content = @Content)
    })
    @PostMapping("/create")
    public AnonymousBankCardDTO create(@RequestBody FullBankCardDTO bankCard) {
        return Mapper.toBankCardDto(bankCardService.create(Mapper.toBankCardEntity(bankCard)));
    }
    @Operation(summary = "Update a bank card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong format in one of the fields or currency does not exist or card number already exists",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card id not found",
                    content = @Content)
    })
    @PutMapping("/{cardId}/update")
    public void update(@PathVariable Long cardId, @RequestBody FullBankCardDTO bankCard) {
        bankCardService.update(cardId, Mapper.toBankCardEntity(bankCard));
    }@Operation(summary = "Update a bank card by number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong format in one of the fields or currency does not exist or card number already exists",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card number not found",
                    content = @Content)
    })
    @PutMapping("/number/{number}/update")
    public void updateByNumber(@PathVariable int number, @RequestBody FullBankCardDTO bankCard) {
        try {
            bankCardService.updateByNumber(number, Mapper.toBankCardEntity(bankCard));
        }
        catch (Exception exception) {
            System.out.println("Exception caught: " + exception.getMessage());
        }
        bankCardService.updateByNumber(number, Mapper.toBankCardEntity(bankCard));
    }

    @Operation(summary = "Deactivate a bank card by number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card deactivated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card number not found",
                    content = @Content)
    })
    @PutMapping("/number/{number}/deactivate")
    public void deactivateByNumber(@PathVariable int number) {
        bankCardService.deactivate(number);
    }
    @Operation(summary = "Activate a bank card by number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card activated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card number not found",
                    content = @Content)
    })
    @PutMapping("/number/{number}/activate")
    public void activateByNumber(@PathVariable int number) {
        bankCardService.activate(number);
    }
    @Operation(summary = "Delete a bank card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card id not found",
                    content = @Content)
    })
    @DeleteMapping("/{cardId}/delete")
    public void delete(@PathVariable Long cardId) {
        bankCardService.deleteById(cardId);
    }

    @Operation(summary = "Delete a bank card by number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank card deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card number not found",
                    content = @Content)
    })
    @DeleteMapping("/number/{number}/delete")
    public void deleteByNumber(@PathVariable int number) {
        bankCardService.deleteByNumber(number);
    }


}
