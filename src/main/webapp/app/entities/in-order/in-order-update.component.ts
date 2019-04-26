import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IInOrder } from 'app/shared/model/in-order.model';
import { InOrderService } from './in-order.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item';

@Component({
    selector: 'jhi-in-order-update',
    templateUrl: './in-order-update.component.html'
})
export class InOrderUpdateComponent implements OnInit {
    inOrder: IInOrder;
    isSaving: boolean;

    items: IItem[];
    orderDateDp: any;
    deliveryDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected inOrderService: InOrderService,
        protected itemService: ItemService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ inOrder }) => {
            this.inOrder = inOrder;
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
        if (this.inOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.inOrderService.update(this.inOrder));
        } else {
            this.subscribeToSaveResponse(this.inOrderService.create(this.inOrder));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IInOrder>>) {
        result.subscribe((res: HttpResponse<IInOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
