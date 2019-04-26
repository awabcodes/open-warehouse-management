import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInOrder } from 'app/shared/model/in-order.model';
import { InOrderService } from './in-order.service';

@Component({
    selector: 'jhi-in-order-delete-dialog',
    templateUrl: './in-order-delete-dialog.component.html'
})
export class InOrderDeleteDialogComponent {
    inOrder: IInOrder;

    constructor(protected inOrderService: InOrderService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.inOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'inOrderListModification',
                content: 'Deleted an inOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-in-order-delete-popup',
    template: ''
})
export class InOrderDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ inOrder }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InOrderDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.inOrder = inOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/in-order', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/in-order', { outlets: { popup: null } }]);
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
