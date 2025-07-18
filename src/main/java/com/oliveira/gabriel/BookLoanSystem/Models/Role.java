package com.oliveira.gabriel.BookLoanSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "tb_roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @Getter
    public enum Values{

        BASIC(1L),
        ADMIN(2L);

        final Long id;

        Values(Long id){
            this.id = id;
        }

    }

}
