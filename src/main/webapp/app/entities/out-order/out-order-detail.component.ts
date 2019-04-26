import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOutOrder } from 'app/shared/model/out-order.model';

@Component({
    selector: 'jhi-out-order-detail',
    templateUrl: './out-order-detail.component.html'
})
export class OutOrderDetailComponent implements OnInit {
    outOrder: IOutOrder;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ outOrder }) => {
            this.outOrder = outOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
