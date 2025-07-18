package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoanRequest;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoanResponse;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Loan;
import com.oliveira.gabriel.BookLoanSystem.Models.LoanItem;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.LoanRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    public ResponseEntity<LoanResponse> makeLoan(LoanRequest request, JwtAuthenticationToken token){

        Loan loan = new Loan();

        loan.setBorrower(userRepository.findById(UUID.fromString(token.getName())).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        ));

        loan.setCreateAt(Instant.now());
        loan.setFinalized(false);

        loan.setItem(new HashSet<>());


        for (var id : request.getIds()) {

            var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id: " + id + " could not be found"));

            if (!book.getAvailable()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This book has already been loaned");
            }

            book.setAvailable(false);
            bookRepository.save(book);

            loan.getItem().add(new LoanItem(loan, book));
        }

        loanRepository.save(loan);

        return ResponseEntity.ok(new LoanResponse(loan, "make a loan"));
    }


    public ResponseEntity<LoanResponse> returnLoan(UUID id, JwtAuthenticationToken token){

        Loan loan = loanRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        User user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if(loan.getBorrower().getId() != user.getId()){
            if(user.getId() != userRepository.findByUsername("admin").get().getId()){
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }

        loan.setEndAt(Instant.now());
        loan.setFinalized(true);

        for(var item : loan.getItem()){
            item.getBook().setAvailable(true);
            item.getBook().setLoanJoin(null);
            bookRepository.save(item.getBook());
        }

        return ResponseEntity.ok(new LoanResponse(loan, "end a loan"));

    }

    public ResponseEntity<Page<LoanResponse>> findAll(Pageable pageable){
        return ResponseEntity.ok(loanRepository.findAll(pageable).map(l -> new LoanResponse(l, "View a loan")));
    }

}

