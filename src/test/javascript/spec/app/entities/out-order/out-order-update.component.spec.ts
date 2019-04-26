/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenWarehouseManagementTestModule } from '../../../test.module';
import { OutOrderUpdateComponent } from 'app/entities/out-order/out-order-update.component';
import { OutOrderService } from 'app/entities/out-order/out-order.service';
import { OutOrder } from 'app/shared/model/out-order.model';

describe('Component Tests', () => {
    describe('OutOrder Management Update Component', () => {
        let comp: OutOrderUpdateComponent;
        let fixture: ComponentFixture<OutOrderUpdateComponent>;
        let service: OutOrderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenWarehouseManagementTestModule],
                declarations: [OutOrderUpdateComponent]
            })
                .overrideTemplate(OutOrderUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OutOrderUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OutOrderService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OutOrder(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.outOrder = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OutOrder();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.outOrder = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
