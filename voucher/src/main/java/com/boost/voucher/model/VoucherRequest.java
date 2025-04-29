package com.boost.voucher.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class VoucherRequest {
    @NotBlank(message = "Special offer name cannot be blank")
    private String specialOfferName;

    @Min(value = 1, message = "Discount percentage must be greater than 0")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Integer discountPercentage;

    private LocalDate expirationDate;

    @NotBlank(message = "Recipient name cannot be blank")
    private String recipientName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    // Constructors
    public VoucherRequest() {}

    public VoucherRequest(String specialOfferName, Integer discountPercentage, LocalDate expirationDate, String recipientName, String email) {
        this.specialOfferName = specialOfferName;
        this.discountPercentage = discountPercentage;
        this.expirationDate = expirationDate;
        this.recipientName = recipientName;
        this.email = email;
    }

    // Getters and Setters
    public String getSpecialOfferName() {
        return specialOfferName;
    }

    public void setSpecialOfferName(String specialOfferName) {
        this.specialOfferName = specialOfferName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VoucherRequest{" +
                "specialOfferName='" + specialOfferName + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", expirationDate=" + expirationDate +
                ", recipientName='" + recipientName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
