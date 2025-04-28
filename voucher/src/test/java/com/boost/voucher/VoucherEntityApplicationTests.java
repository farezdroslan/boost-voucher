package com.boost.voucher;

import com.boost.voucher.service.VoucherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class VoucherEntityApplicationTests {
	private VoucherService voucherService; // Autowire the service you want to test
	@Test
	void contextLoads() {
		// Verify that the Spring application context loads successfully
		assertNotNull(voucherService, "VoucherService bean must not be null");
	}

}
