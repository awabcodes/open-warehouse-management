/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenWarehouseManagementTestModule } from '../../../test.module';
import { InOrderDetailComponent } from 'app/entities/in-order/in-order-detail.component';
import { InOrder } from 'app/shared/model/in-order.model';

describe('Component Tests', () => {
    describe('InOrder Management Detail Component', () => {
        let comp: InOrderDetailComponent;
        let fixture: ComponentFixture<InOrderDetailComponent>;
        const route = ({ data: of({ inOrder: new InOrder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenWarehouseManagementTestModule],
                declarations: [InOrderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InOrderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InOrderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.inOrder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
