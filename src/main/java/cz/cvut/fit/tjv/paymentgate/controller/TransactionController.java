package cz.cvut.fit.tjv.paymentgate.controller;


import cz.cvut.fit.tjv.paymentgate.domain.Transaction;
import cz.cvut.fit.tjv.paymentgate.dto.AnonymousTransactionDTO;
import cz.cvut.fit.tjv.paymentgate.dto.AnonymousUserDTO;
import cz.cvut.fit.tjv.paymentgate.dto.Mapper;
import cz.cvut.fit.tjv.paymentgate.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Class is responsible for managing API for transactions
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousTransactionDTO.class))} )
    })
    @GetMapping("")
    public Iterable<AnonymousTransactionDTO> getAll() {
        ArrayList<AnonymousTransactionDTO> transactions = new ArrayList<>();
        for (Transaction transaction: transactionService.readAll()) {
            transactions.add(Mapper.toTransactionDto(transaction));
        }
        return transactions;
    }

    @Operation(summary = "Get a transaction by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnonymousTransactionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @GetMapping("/{transactionId}")
    public AnonymousTransactionDTO get(@PathVariable Long transactionId) {
        return Mapper.toTransactionDto(transactionService.readById(transactionId));
    }

    @Operation(summary = "Update a transaction by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong format of one of the submitted fields",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @PutMapping("{transactionId}/update")
    public void update(@PathVariable Long transactionId, @RequestBody AnonymousTransactionDTO transaction) {
        transactionService.update(transactionId, Mapper.toTransactionEntity(transaction));
    }
    @Operation(summary = "Delete a transaction by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    @DeleteMapping("{transactionId}/delete")
    public void delete(@PathVariable Long transactionId) {
        transactionService.deleteById(transactionId);
    }
}
