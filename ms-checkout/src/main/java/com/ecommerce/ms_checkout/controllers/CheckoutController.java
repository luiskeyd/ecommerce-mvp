package com.ecommerce.ms_checkout.controllers;

import com.ecommerce.ms_checkout.models.CheckoutResponse;
import com.ecommerce.ms_checkout.models.Order;
import com.ecommerce.ms_checkout.patterns.template.CheckoutProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Locale;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutProcessor checkoutProcessor;

    public CheckoutController(CheckoutProcessor checkoutProcessor) {
        this.checkoutProcessor = checkoutProcessor;
    }

    @PostMapping
    public ResponseEntity<?> finishBuy(@RequestBody CheckoutRequest request) {
        if (request.valor() == null || request.valor() <= 0) {
            return ResponseEntity.badRequest().body("O valor do pedido deve ser maior que zero.");
        }
        if (request.tipo() == null || request.tipo().isBlank()) {
            return ResponseEntity.badRequest().body("O tipo de pagamento é obrigatório.");
        }

        Order order = new Order();
        order.setTotal(request.valor());

        String paymentType = request.tipo() == null
                ? null
                : request.tipo().trim().toLowerCase(Locale.ROOT);

        try {
            CheckoutResponse response = checkoutProcessor.executeCheckout(order, paymentType);

            if ("FALHA".equals(response.status())) {
                return ResponseEntity.unprocessableEntity().body(response);
            }

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
record CheckoutRequest(Double valor, String tipo) {}
