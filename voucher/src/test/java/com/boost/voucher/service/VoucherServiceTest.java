package com.boost.voucher.service;

import com.boost.voucher.entities.VoucherEntity;
import com.boost.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private VoucherService voucherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGeneralVoucher() {
        String specialOfferName = "SummerSale";
        Integer discountPercentage = 20;
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        String recipientName = "John Doe";
        String email = "john.doe@example.com";

        // Mock repository behavior
        when(voucherRepository.save(any(VoucherEntity.class))).thenAnswer(invocation -> {
        VoucherEntity voucher = invocation.getArgument(0);
        voucher.setId(1L); // Simulate database ID generation
        return voucher;
        });

        VoucherEntity v = voucherService.generateVoucher(
                specialOfferName,
                discountPercentage,
                expirationDate,
                recipientName,
                email
        );

        // Verify the result
        assertNotNull(v.getCode());
        assertEquals(recipientName, v.getRecipientName());
        assertEquals(email, v.getEmail());
        assertEquals(specialOfferName, v.getSpecialOfferName());
        assertEquals(discountPercentage, v.getDiscountPercentage());
        assertEquals(expirationDate, v.getExpirationDate());
        assertNull(v.getUsageDate());

        // Verify repository interaction
        verify(voucherRepository, times(1)).save(any(VoucherEntity.class));
    }

    // Test Case: Validate Voucher (Valid Case)
    @Test
    void testValidateVoucher_Valid() {
        String code = "ABCDEFGH";
        String email = "john.doe@example.com";
        Integer discountPercentage = 20;

        // Mock repository behavior
        VoucherEntity mockVoucher = new VoucherEntity();
        mockVoucher.setCode(code);
        mockVoucher.setEmail(email);
        mockVoucher.setDiscountPercentage(discountPercentage);
        mockVoucher.setExpirationDate(LocalDate.of(2025, 12, 31));

        when(voucherRepository.findByCodeAndEmail(code, email)).thenReturn(Optional.of(mockVoucher));

        // Call the method
        Object result = voucherService.validateVoucher(code, email);

        // Verify the result
        assertNotNull(result);
        assertEquals(discountPercentage, result);

        // Verify voucher usage date is set
        assertTrue(mockVoucher.getUsageDate().isAfter(LocalDate.now().minusDays(1)));

        // Verify repository interaction
        verify(voucherRepository, times(1)).findByCodeAndEmail(code, email);
        verify(voucherRepository, times(1)).save(mockVoucher);
    }

    // Test Case: Validate Voucher (Invalid Case)
    @Test
    void testValidateVoucher_Invalid() {
        String code = "INVALIDCODE";
        String email = "john.doe@example.com";

        // Mock repository behavior
        when(voucherRepository.findByCodeAndEmail(code, email)).thenReturn(Optional.empty());

        // Call the method
        Object result = voucherService.validateVoucher(code, email);

        // Verify the result
        assertNull(result);

        // Verify repository interaction
        verify(voucherRepository, times(1)).findByCodeAndEmail(code, email);
        verify(voucherRepository, never()).save(any(VoucherEntity.class));
    }


}
