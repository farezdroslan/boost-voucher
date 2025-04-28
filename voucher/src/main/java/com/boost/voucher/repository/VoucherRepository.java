package com.boost.voucher.repository;

import com.boost.voucher.entities.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findByCodeAndEmail(String code, String email);
    List<VoucherEntity> findByEmailAndUsageDateNull(String email);
}
