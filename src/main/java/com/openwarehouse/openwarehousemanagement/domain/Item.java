package com.openwarehouse.openwarehousemanagement.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "available_quantity", nullable = false)
    private Double availableQuantity;

    @NotNull
    @Column(name = "minimum_quantity", nullable = false)
    private Double minimumQuantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "jhi_specification")
    private String specification;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OutOrder> outOrders = new HashSet<>();
    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InOrder> inOrders = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Item type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAvailableQuantity() {
        return availableQuantity;
    }

    public Item availableQuantity(Double availableQuantity) {
        this.availableQuantity = availableQuantity;
        return this;
    }

    public void setAvailableQuantity(Double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Double getMinimumQuantity() {
        return minimumQuantity;
    }

    public Item minimumQuantity(Double minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
        return this;
    }

    public void setMinimumQuantity(Double minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public Item price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public Item specification(String specification) {
        this.specification = specification;
        return this;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSupplier() {
        return supplier;
    }

    public Item supplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public Item description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OutOrder> getOutOrders() {
        return outOrders;
    }

    public Item outOrders(Set<OutOrder> outOrders) {
        this.outOrders = outOrders;
        return this;
    }

    public Item addOutOrders(OutOrder outOrder) {
        this.outOrders.add(outOrder);
        outOrder.setItem(this);
        return this;
    }

    public Item removeOutOrders(OutOrder outOrder) {
        this.outOrders.remove(outOrder);
        outOrder.setItem(null);
        return this;
    }

    public void setOutOrders(Set<OutOrder> outOrders) {
        this.outOrders = outOrders;
    }

    public Set<InOrder> getInOrders() {
        return inOrders;
    }

    public Item inOrders(Set<InOrder> inOrders) {
        this.inOrders = inOrders;
        return this;
    }

    public Item addInOrders(InOrder inOrder) {
        this.inOrders.add(inOrder);
        inOrder.setItem(this);
        return this;
    }

    public Item removeInOrders(InOrder inOrder) {
        this.inOrders.remove(inOrder);
        inOrder.setItem(null);
        return this;
    }

    public void setInOrders(Set<InOrder> inOrders) {
        this.inOrders = inOrders;
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
        Item item = (Item) o;
        if (item.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", availableQuantity=" + getAvailableQuantity() +
            ", minimumQuantity=" + getMinimumQuantity() +
            ", price=" + getPrice() +
            ", specification='" + getSpecification() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
