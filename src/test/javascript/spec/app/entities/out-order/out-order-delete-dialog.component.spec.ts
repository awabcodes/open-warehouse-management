/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenWarehouseManagementTestModule } from '../../../test.module';
import { OutOrderDeleteDialogComponent } from 'app/entities/out-order/out-order-delete-dialog.component';
import { OutOrderService } from 'app/entities/out-order/out-order.service';

describe('Component Tests', () => {
    describe('OutOrder Management Delete Component', () => {
        let comp: OutOrderDeleteDialogComponent;
        let fixture: ComponentFixture<OutOrderDeleteDialogComponent>;
        let service: OutOrderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenWarehouseManagementTestModule],
                declarations: [OutOrderDeleteDialogComponent]
            })
                .overrideTemplate(OutOrderDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OutOrderDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutOrderService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
