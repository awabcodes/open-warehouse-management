import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInOrder } from 'app/shared/model/in-order.model';

@Component({
    selector: 'jhi-in-order-detail',
    templateUrl: './in-order-detail.component.html'
})
export class InOrderDetailComponent implements OnInit {
    inOrder: IInOrder;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ inOrder }) => {
            this.inOrder = inOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
