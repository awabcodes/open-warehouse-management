package com.openwarehouse.openwarehousemanagement.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A InOrder.
 */
@Entity
@Table(name = "in_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "information", nullable = false)
    private String information;

    @NotNull
    @Column(name = "order_quantity", nullable = false)
    private Double orderQuantity;

    @Column(name = "delivered")
    private Boolean delivered;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @ManyToOne
    @JsonIgnoreProperties("inOrders")
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public InOrder title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public InOrder information(String information) {
        this.information = information;
        return this;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Double getOrderQuantity() {
        return orderQuantity;
    }

    public InOrder orderQuantity(Double orderQuantity) {
        this.orderQuantity = orderQuantity;
        return this;
    }

    public void setOrderQuantity(Double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Boolean isDelivered() {
        return delivered;
    }

    public InOrder delivered(Boolean delivered) {
        this.delivered = delivered;
        return this;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public InOrder orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public InOrder deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Item getItem() {
        return item;
    }

    public InOrder item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InOrder inOrder = (InOrder) o;
        if (inOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InOrder{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", information='" + getInformation() + "'" +
            ", orderQuantity=" + getOrderQuantity() +
            ", delivered='" + isDelivered() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            "}";
    }
}
