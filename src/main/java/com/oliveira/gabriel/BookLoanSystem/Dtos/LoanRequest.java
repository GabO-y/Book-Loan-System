package com.oliveira.gabriel.BookLoanSystem.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanRequest {
    Set<UUID> ids;
}
