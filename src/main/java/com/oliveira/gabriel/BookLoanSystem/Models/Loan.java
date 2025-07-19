package com.oliveira.gabriel.BookLoanSystem.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LoanItem> item;
    private Instant createAt;
    private Instant endAt;
    private Boolean finalized;

    @ManyToOne()
    private User borrower;

    public void removeItem(Book book){
        item.removeIf(i -> i.getBook().equals(book));
    }


}
