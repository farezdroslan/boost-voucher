package com.boost.voucher.service;

import com.boost.voucher.entities.VoucherEntity;
import com.boost.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;
@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // Generate voucher code
    public VoucherEntity generateVoucher(
            String specialOfferName,
            Integer discountPercentage,
            LocalDate expirationDate,
            String recipientName,
            String email
    ) {
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        VoucherEntity voucher = new VoucherEntity(code, recipientName, email, specialOfferName, discountPercentage, expirationDate);
        voucherRepository.save(voucher);
        return voucher;
    }

    // Validate voucher code
    public Object validateVoucher(String code, String email) {
        Optional<VoucherEntity> voucher = voucherRepository.findByCodeAndEmail(code, email);
        if (voucher.isPresent()) {
            VoucherEntity v = voucher.get();
            if (v.getUsageDate() == null && v.getExpirationDate().isAfter(LocalDate.now())) {
                v.setUsageDate(LocalDate.now());
                voucherRepository.save(v);
                return (Map.of("Special Offer", v.getSpecialOfferName(), "Percentage", v.getDiscountPercentage()));
            }
        }
        return null;
    }

    // Retrieve all valid vouchers for given email
    public List<VoucherEntity> getValidVouchersByEmail(String email) {
        return voucherRepository.findByEmailAndUsageDateNull(email);
    }

}
