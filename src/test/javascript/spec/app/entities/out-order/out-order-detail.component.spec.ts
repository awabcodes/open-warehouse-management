/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenWarehouseManagementTestModule } from '../../../test.module';
import { OutOrderDetailComponent } from 'app/entities/out-order/out-order-detail.component';
import { OutOrder } from 'app/shared/model/out-order.model';

describe('Component Tests', () => {
    describe('OutOrder Management Detail Component', () => {
        let comp: OutOrderDetailComponent;
        let fixture: ComponentFixture<OutOrderDetailComponent>;
        const route = ({ data: of({ outOrder: new OutOrder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenWarehouseManagementTestModule],
                declarations: [OutOrderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OutOrderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OutOrderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.outOrder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
