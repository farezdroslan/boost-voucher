package com.boost.voucher.controller;

import com.boost.voucher.entities.VoucherEntity;
import com.boost.voucher.model.VoucherRequest;
import com.boost.voucher.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
@Validated
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Object> generateVoucher (@RequestBody @Valid VoucherRequest request) {

        if (request.getExpirationDate() == null || !request.getExpirationDate().isAfter(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error","Invalid expiration date",
                            "message","The expiration date must be a future date."
                    ));
        }

        VoucherEntity voucher = voucherService.generateVoucher(
                request.getSpecialOfferName(),
                request.getDiscountPercentage(),
                request.getExpirationDate(),
                request.getRecipientName(),
                request.getEmail()
        );

        return ResponseEntity.ok(voucher);
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateVoucher(@RequestParam String code, @RequestParam String email) {
        // Input validation
        if (code == null || code.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Invalid code",
                            "message", "The voucher code must be provided and cannot be empty."
                    ));
        }

        if (email == null || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Invalid email",
                            "message", "The email must be provided and must be a valid email address."
                    ));
        }

        // Validate voucher using the service
        Object discount = voucherService.validateVoucher(code, email);

        if (discount == null) {
            // Voucher is invalid, expired, or already used
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Invalid voucher",
                            "message", "The voucher code is invalid, expired, or has already been used."
                    ));
        }

        // Return the discount percentage if the voucher is valid
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/valid")
    public ResponseEntity<Object> getValidVouchersByEmail(@RequestParam(required = false) String email) {
        // Input validation
        if (email == null || email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Invalid email",
                            "message", "The email must be provided and must be a valid email address."
                    ));
        }

        // Retrieve valid vouchers
        List<VoucherEntity> vouchers = voucherService.getValidVouchersByEmail(email);
        if (vouchers.isEmpty()) {
            return ResponseEntity.noContent().build(); // No valid vouchers found
        }
        return ResponseEntity.ok(vouchers);
    }
}
