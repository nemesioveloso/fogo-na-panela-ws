package com.example.base.dto;

import com.example.base.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentDTO {

    @NotNull(message = "O método de pagamento é obrigatório.")
    private PaymentMethod paymentMethod;
}