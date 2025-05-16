package service;


import org.example.Manager.LibraryManager;
import org.example.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionServiceImplTest {

    private LibraryManager libraryManager;
    private TransactionServiceImpl transactionService;

    @Test
    void calculateTrustFee_shouldReturnCorrectValue() {
        Mockito.when(libraryManager.getBookCondition(323L)).thenReturn("NEW");
        Double result = transactionService.calculateTrustFee(323L);
        assertEquals(110.0, result);
    }

    @Test
    void calculateTrustFee_shouldReturnFeeForNew() {
        Long bookId = 323L;
        Mockito.when(libraryManager.getBookCondition(bookId)).thenReturn("NEW");

        Double result = transactionService.calculateTrustFee(bookId);

        assertEquals(110.0, result); // 50 + 60
    }

    @Test
    void calculateTrustFee_shouldReturnFeeForGood() {
        Long bookId = 324L;
        Mockito.when(libraryManager.getBookCondition(bookId)).thenReturn("GOOD");

        Double result = transactionService.calculateTrustFee(bookId);

        assertEquals(90.0, result); // 30 + 60
    }

    @Test
    void calculateTrustFee_shouldReturnDefaultFeeForInvalidCondition() {
        Long bookId = 325L;
        Mockito.when(libraryManager.getBookCondition(bookId)).thenReturn("OLD");

        Double result = transactionService.calculateTrustFee(bookId);

        assertEquals(90.0, result); // default 30 + 60
    }

    @Test
    void calculateTrustFee_shouldReturnFeeForAcceptableWithExtraSpace() {
        Long bookId = 326L;
        Mockito.when(libraryManager.getBookCondition(bookId)).thenReturn(" ACCEPTABLE");

        Double result = transactionService.calculateTrustFee(bookId);

        assertEquals(80.0, result); // 20 + 60
    }
}
