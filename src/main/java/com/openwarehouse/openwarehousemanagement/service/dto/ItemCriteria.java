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

/**
 * Criteria class for the Item entity. This class is used in ItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter type;

    private DoubleFilter availableQuantity;

    private DoubleFilter minimumQuantity;

    private DoubleFilter price;

    private StringFilter specification;

    private StringFilter supplier;

    private StringFilter description;

    private LongFilter outOrdersId;

    private LongFilter inOrdersId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public DoubleFilter getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(DoubleFilter availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public DoubleFilter getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(DoubleFilter minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getSpecification() {
        return specification;
    }

    public void setSpecification(StringFilter specification) {
        this.specification = specification;
    }

    public StringFilter getSupplier() {
        return supplier;
    }

    public void setSupplier(StringFilter supplier) {
        this.supplier = supplier;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getOutOrdersId() {
        return outOrdersId;
    }

    public void setOutOrdersId(LongFilter outOrdersId) {
        this.outOrdersId = outOrdersId;
    }

    public LongFilter getInOrdersId() {
        return inOrdersId;
    }

    public void setInOrdersId(LongFilter inOrdersId) {
        this.inOrdersId = inOrdersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemCriteria that = (ItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(availableQuantity, that.availableQuantity) &&
            Objects.equals(minimumQuantity, that.minimumQuantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(specification, that.specification) &&
            Objects.equals(supplier, that.supplier) &&
            Objects.equals(description, that.description) &&
            Objects.equals(outOrdersId, that.outOrdersId) &&
            Objects.equals(inOrdersId, that.inOrdersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        availableQuantity,
        minimumQuantity,
        price,
        specification,
        supplier,
        description,
        outOrdersId,
        inOrdersId
        );
    }

    @Override
    public String toString() {
        return "ItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (availableQuantity != null ? "availableQuantity=" + availableQuantity + ", " : "") +
                (minimumQuantity != null ? "minimumQuantity=" + minimumQuantity + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (specification != null ? "specification=" + specification + ", " : "") +
                (supplier != null ? "supplier=" + supplier + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (outOrdersId != null ? "outOrdersId=" + outOrdersId + ", " : "") +
                (inOrdersId != null ? "inOrdersId=" + inOrdersId + ", " : "") +
            "}";
    }

}
