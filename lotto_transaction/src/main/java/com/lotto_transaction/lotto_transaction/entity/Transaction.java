package com.lotto_transaction.lotto_transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int card_number;
    private BigDecimal amount;
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @JoinColumn(name = "category_id", nullable = false)
    private Long categoryId;
}