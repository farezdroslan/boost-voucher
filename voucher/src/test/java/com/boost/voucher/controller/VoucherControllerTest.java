package com.boost.voucher.controller;

import com.boost.voucher.entities.VoucherEntity;
import com.boost.voucher.model.VoucherRequest;
import com.boost.voucher.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class VoucherControllerTest {

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private VoucherController voucherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(voucherController).build();
        // Mock service behavior
        VoucherEntity mockVoucher = new VoucherEntity();
        mockVoucher.setCode("ABCDEFGH");
        mockVoucher.setRecipientName("John Doe");
        mockVoucher.setEmail("john.doe@example.com");
        mockVoucher.setSpecialOfferName("SummerSale");
        mockVoucher.setDiscountPercentage(20);
        mockVoucher.setExpirationDate(LocalDate.of(2025, 12, 31));

        when(
                voucherService
                        .generateVoucher(
                                anyString(),
                                anyInt(),
                                any(LocalDate.class),
                                anyString(),
                                anyString()))
                .thenReturn(mockVoucher);
    }

    // Test Case: Generate Voucher (Success)
    @Test
    void testGenerateVoucher_Success() throws Exception {


        mockMvc.perform(post("/api/voucher/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("""
                        {
                            "specialOfferName": "SummerSale",
                            "discountPercentage": 20,
                            "expirationDate": "2025-12-31",
                            "recipientName": "John Doe",
                            "email": "john.doe@example.com"
                        }
                        """))
                .andDo(print()) // Shows detailed request/response
                .andDo(result -> System.out.println("Response Body: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.code").exists());
//                .andExpect(jsonPath("$.recipientName").value("John Doe"))
//                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(voucherService, times(1)).generateVoucher(
                anyString(),
                anyInt(),
                any(LocalDate.class),
                anyString(),
                anyString()
        );
    }
}
