package com.logistic.client.order.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_order_item")
public class OrderItem extends BaseEntity {
    @Id
    private UUID orderItemId;

    private UUID productId;

    @Embedded
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
    })
    private Money price;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "subtotal_amount"))
    })
    private Money subTotal;

    public OrderItem(UUID productId, Quantity quantity, Money price) {
        this.orderItemId = UUID.randomUUID();
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = price.multiply(quantity.getQuantity()); // 소계 계산
    }

    public void setOrder(Order order) { this.order = order; }
}
