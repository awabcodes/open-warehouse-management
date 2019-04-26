import { IOutOrder } from 'app/shared/model/out-order.model';
import { IInOrder } from 'app/shared/model/in-order.model';

export interface IItem {
    id?: number;
    name?: string;
    type?: string;
    availableQuantity?: number;
    minimumQuantity?: number;
    price?: number;
    specification?: string;
    supplier?: string;
    description?: string;
    outOrders?: IOutOrder[];
    inOrders?: IInOrder[];
}

export class Item implements IItem {
    constructor(
        public id?: number,
        public name?: string,
        public type?: string,
        public availableQuantity?: number,
        public minimumQuantity?: number,
        public price?: number,
        public specification?: string,
        public supplier?: string,
        public description?: string,
        public outOrders?: IOutOrder[],
        public inOrders?: IInOrder[]
    ) {}
}
