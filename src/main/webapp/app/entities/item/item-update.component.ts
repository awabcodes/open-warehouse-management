import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from './item.service';

@Component({
    selector: 'jhi-item-update',
    templateUrl: './item-update.component.html'
})
export class ItemUpdateComponent implements OnInit {
    item: IItem;
    isSaving: boolean;

    constructor(protected itemService: ItemService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ item }) => {
            this.item = item;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.item.id !== undefined) {
            this.subscribeToSaveResponse(this.itemService.update(this.item));
        } else {
            this.subscribeToSaveResponse(this.itemService.create(this.item));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>) {
        result.subscribe((res: HttpResponse<IItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
