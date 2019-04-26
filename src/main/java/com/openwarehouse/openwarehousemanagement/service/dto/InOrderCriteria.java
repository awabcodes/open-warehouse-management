package com.openwarehouse.openwarehousemanagement.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the InOrder entity. This class is used in InOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /in-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InOrderCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter information;

    private DoubleFilter orderQuantity;

    private BooleanFilter delivered;

    private LocalDateFilter orderDate;

    private LocalDateFilter deliveryDate;

    private LongFilter itemId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getInformation() {
        return information;
    }

    public void setInformation(StringFilter information) {
        this.information = information;
    }

    public DoubleFilter getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(DoubleFilter orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BooleanFilter getDelivered() {
        return delivered;
    }

    public void setDelivered(BooleanFilter delivered) {
        this.delivered = delivered;
    }

    public LocalDateFilter getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateFilter orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateFilter getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InOrderCriteria that = (InOrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(information, that.information) &&
            Objects.equals(orderQuantity, that.orderQuantity) &&
            Objects.equals(delivered, that.delivered) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(deliveryDate, that.deliveryDate) &&
            Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        information,
        orderQuantity,
        delivered,
        orderDate,
        deliveryDate,
        itemId
        );
    }

    @Override
    public String toString() {
        return "InOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (information != null ? "information=" + information + ", " : "") +
                (orderQuantity != null ? "orderQuantity=" + orderQuantity + ", " : "") +
                (delivered != null ? "delivered=" + delivered + ", " : "") +
                (orderDate != null ? "orderDate=" + orderDate + ", " : "") +
                (deliveryDate != null ? "deliveryDate=" + deliveryDate + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }

}
