/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenWarehouseManagementTestModule } from '../../../test.module';
import { InOrderUpdateComponent } from 'app/entities/in-order/in-order-update.component';
import { InOrderService } from 'app/entities/in-order/in-order.service';
import { InOrder } from 'app/shared/model/in-order.model';

describe('Component Tests', () => {
    describe('InOrder Management Update Component', () => {
        let comp: InOrderUpdateComponent;
        let fixture: ComponentFixture<InOrderUpdateComponent>;
        let service: InOrderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenWarehouseManagementTestModule],
                declarations: [InOrderUpdateComponent]
            })
                .overrideTemplate(InOrderUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InOrderUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InOrderService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new InOrder(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.inOrder = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new InOrder();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.inOrder = entity;
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
