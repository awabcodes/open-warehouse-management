import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOutOrder } from 'app/shared/model/out-order.model';
import { OutOrderService } from './out-order.service';

@Component({
    selector: 'jhi-out-order-delete-dialog',
    templateUrl: './out-order-delete-dialog.component.html'
})
export class OutOrderDeleteDialogComponent {
    outOrder: IOutOrder;

    constructor(protected outOrderService: OutOrderService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.outOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'outOrderListModification',
                content: 'Deleted an outOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-out-order-delete-popup',
    template: ''
})
export class OutOrderDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ outOrder }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OutOrderDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.outOrder = outOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/out-order', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/out-order', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
