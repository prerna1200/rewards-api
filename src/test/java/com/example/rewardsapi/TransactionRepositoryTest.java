package com.example.rewardsapi;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
    }

    @Test
    void save_createsNewTransaction() {

        Transaction txn = new Transaction(
                "C1",
                new BigDecimal("120"),
                LocalDate.of(2026, 1, 15)
        );

        Transaction saved = repo.save(txn);

        assertNotNull(saved.getId());
        assertEquals("C1", saved.getCustomerId());
        assertEquals(new BigDecimal("120"), saved.getAmount());
    }

    @Test
    void findById_retrievesExistingTransaction() {

        Transaction txn = new Transaction(
                "C2",
                new BigDecimal("75"),
                LocalDate.of(2026, 2, 20)
        );

        Transaction saved = repo.save(txn);

        Optional<Transaction> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("C2", found.get().getCustomerId());
    }

    @Test
    void findByCustomerId_returnAllForCustomer() {

        repo.save(new Transaction("C1", new BigDecimal("100"), LocalDate.of(2026, 1, 10)));
        repo.save(new Transaction("C1", new BigDecimal("50"), LocalDate.of(2026, 1, 20)));
        repo.save(new Transaction("C2", new BigDecimal("200"), LocalDate.of(2026, 2, 15)));

        List<Transaction> c1List = repo.findByCustomerId("C1");
        List<Transaction> c2List = repo.findByCustomerId("C2");

        assertEquals(2, c1List.size());
        assertEquals(1, c2List.size());
    }

    @Test
    void findByCustomerId_emptyWhenNotFound() {

        repo.save(new Transaction("C1", new BigDecimal("100"), LocalDate.of(2026, 1, 10)));

        List<Transaction> result = repo.findByCustomerId("UNKNOWN");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllTransactions() {

        repo.save(new Transaction("C1", new BigDecimal("100"), LocalDate.of(2026, 1, 10)));
        repo.save(new Transaction("C2", new BigDecimal("50"), LocalDate.of(2026, 2, 20)));

        List<Transaction> all = repo.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void update_modifiesAmount() {

        Transaction txn = new Transaction("C1", new BigDecimal("100"), LocalDate.of(2026, 1, 10));
        Transaction saved = repo.save(txn);

        saved.setAmount(new BigDecimal("150"));
        Transaction updated = repo.save(saved);

        assertEquals(new BigDecimal("150"), updated.getAmount());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void delete_removesTransaction() {

        Transaction txn = new Transaction("C1", new BigDecimal("100"), LocalDate.of(2026, 1, 10));
        Transaction saved = repo.save(txn);

        repo.deleteById(saved.getId());

        Optional<Transaction> found = repo.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}
