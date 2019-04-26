import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInOrder } from 'app/shared/model/in-order.model';

type EntityResponseType = HttpResponse<IInOrder>;
type EntityArrayResponseType = HttpResponse<IInOrder[]>;

@Injectable({ providedIn: 'root' })
export class InOrderService {
    public resourceUrl = SERVER_API_URL + 'api/in-orders';

    constructor(protected http: HttpClient) {}

    create(inOrder: IInOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(inOrder);
        return this.http
            .post<IInOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(inOrder: IInOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(inOrder);
        return this.http
            .put<IInOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IInOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(inOrder: IInOrder): IInOrder {
        const copy: IInOrder = Object.assign({}, inOrder, {
            orderDate: inOrder.orderDate != null && inOrder.orderDate.isValid() ? inOrder.orderDate.format(DATE_FORMAT) : null,
            deliveryDate: inOrder.deliveryDate != null && inOrder.deliveryDate.isValid() ? inOrder.deliveryDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
            res.body.deliveryDate = res.body.deliveryDate != null ? moment(res.body.deliveryDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((inOrder: IInOrder) => {
                inOrder.orderDate = inOrder.orderDate != null ? moment(inOrder.orderDate) : null;
                inOrder.deliveryDate = inOrder.deliveryDate != null ? moment(inOrder.deliveryDate) : null;
            });
        }
        return res;
    }
}
