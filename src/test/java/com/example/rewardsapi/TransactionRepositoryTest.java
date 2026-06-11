package com.example.rewardsapi;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
    }

    @Test
    void save_createsNewTransaction() {
        Transaction txn = new Transaction("C1", 120, LocalDate.of(2026, 1, 15));
        Transaction saved = repo.save(txn);

        assertNotNull(saved.getId());
        assertEquals("C1", saved.getCustomerId());
        assertEquals(120, saved.getAmount());
    }

    @Test
    void findById_retrievesExistingTransaction() {
        Transaction txn = new Transaction("C2", 75, LocalDate.of(2026, 2, 20));
        Transaction saved = repo.save(txn);

        Optional<Transaction> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("C2", found.get().getCustomerId());
    }

    @Test
    void findByCustomerId_returnAllForCustomer() {
        repo.save(new Transaction("C1", 100, LocalDate.of(2026, 1, 10)));
        repo.save(new Transaction("C1", 50, LocalDate.of(2026, 1, 20)));
        repo.save(new Transaction("C2", 200, LocalDate.of(2026, 2, 15)));

        List<Transaction> c1List = repo.findByCustomerId("C1");
        List<Transaction> c2List = repo.findByCustomerId("C2");

        assertEquals(2, c1List.size());
        assertEquals(1, c2List.size());
    }

    @Test
    void findByCustomerId_emptyWhenNotFound() {
        repo.save(new Transaction("C1", 100, LocalDate.of(2026, 1, 10)));

        List<Transaction> result = repo.findByCustomerId("UNKNOWN");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllTransactions() {
        repo.save(new Transaction("C1", 100, LocalDate.of(2026, 1, 10)));
        repo.save(new Transaction("C2", 50, LocalDate.of(2026, 2, 20)));

        List<Transaction> all = repo.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void update_modifiesAmount() {
        Transaction txn = new Transaction("C1", 100, LocalDate.of(2026, 1, 10));
        Transaction saved = repo.save(txn);

        saved.setAmount(150);
        Transaction updated = repo.save(saved);

        assertEquals(150, updated.getAmount());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void delete_removesTransaction() {
        Transaction txn = new Transaction("C1", 100, LocalDate.of(2026, 1, 10));
        Transaction saved = repo.save(txn);
        Long id = saved.getId();

        repo.deleteById(id);

        Optional<Transaction> found = repo.findById(id);
        assertFalse(found.isPresent());
    }
}