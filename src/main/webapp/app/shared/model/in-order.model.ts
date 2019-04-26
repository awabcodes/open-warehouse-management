import { Moment } from 'moment';
import { IItem } from 'app/shared/model/item.model';

export interface IInOrder {
    id?: number;
    title?: string;
    information?: string;
    orderQuantity?: number;
    delivered?: boolean;
    orderDate?: Moment;
    deliveryDate?: Moment;
    item?: IItem;
}

export class InOrder implements IInOrder {
    constructor(
        public id?: number,
        public title?: string,
        public information?: string,
        public orderQuantity?: number,
        public delivered?: boolean,
        public orderDate?: Moment,
        public deliveryDate?: Moment,
        public item?: IItem
    ) {
        this.delivered = this.delivered || false;
    }
}
