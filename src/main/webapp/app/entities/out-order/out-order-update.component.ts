import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IOutOrder } from 'app/shared/model/out-order.model';
import { OutOrderService } from './out-order.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item';

@Component({
    selector: 'jhi-out-order-update',
    templateUrl: './out-order-update.component.html'
})
export class OutOrderUpdateComponent implements OnInit {
    outOrder: IOutOrder;
    isSaving: boolean;

    items: IItem[];
    orderDateDp: any;
    deliveryDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected outOrderService: OutOrderService,
        protected itemService: ItemService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ outOrder }) => {
            this.outOrder = outOrder;
        });
        this.itemService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IItem[]>) => mayBeOk.ok),
                map((response: HttpResponse<IItem[]>) => response.body)
            )
            .subscribe((res: IItem[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.outOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.outOrderService.update(this.outOrder));
        } else {
            this.subscribeToSaveResponse(this.outOrderService.create(this.outOrder));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOutOrder>>) {
        result.subscribe((res: HttpResponse<IOutOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackItemById(index: number, item: IItem) {
        return item.id;
    }
}
