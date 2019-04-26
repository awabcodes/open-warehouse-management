import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOutOrder } from 'app/shared/model/out-order.model';

type EntityResponseType = HttpResponse<IOutOrder>;
type EntityArrayResponseType = HttpResponse<IOutOrder[]>;

@Injectable({ providedIn: 'root' })
export class OutOrderService {
    public resourceUrl = SERVER_API_URL + 'api/out-orders';

    constructor(protected http: HttpClient) {}

    create(outOrder: IOutOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(outOrder);
        return this.http
            .post<IOutOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(outOrder: IOutOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(outOrder);
        return this.http
            .put<IOutOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOutOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOutOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(outOrder: IOutOrder): IOutOrder {
        const copy: IOutOrder = Object.assign({}, outOrder, {
            orderDate: outOrder.orderDate != null && outOrder.orderDate.isValid() ? outOrder.orderDate.format(DATE_FORMAT) : null,
            deliveryDate:
                outOrder.deliveryDate != null && outOrder.deliveryDate.isValid() ? outOrder.deliveryDate.format(DATE_FORMAT) : null
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
            res.body.forEach((outOrder: IOutOrder) => {
                outOrder.orderDate = outOrder.orderDate != null ? moment(outOrder.orderDate) : null;
                outOrder.deliveryDate = outOrder.deliveryDate != null ? moment(outOrder.deliveryDate) : null;
            });
        }
        return res;
    }
}
