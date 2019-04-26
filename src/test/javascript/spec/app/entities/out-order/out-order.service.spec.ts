/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { OutOrderService } from 'app/entities/out-order/out-order.service';
import { IOutOrder, OutOrder } from 'app/shared/model/out-order.model';

describe('Service Tests', () => {
    describe('OutOrder Service', () => {
        let injector: TestBed;
        let service: OutOrderService;
        let httpMock: HttpTestingController;
        let elemDefault: IOutOrder;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(OutOrderService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new OutOrder(0, 'AAAAAAA', 'AAAAAAA', 0, false, currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        orderDate: currentDate.format(DATE_FORMAT),
                        deliveryDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a OutOrder', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        orderDate: currentDate.format(DATE_FORMAT),
                        deliveryDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        orderDate: currentDate,
                        deliveryDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new OutOrder(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a OutOrder', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        information: 'BBBBBB',
                        orderQuantity: 1,
                        delivered: true,
                        orderDate: currentDate.format(DATE_FORMAT),
                        deliveryDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        orderDate: currentDate,
                        deliveryDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of OutOrder', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        information: 'BBBBBB',
                        orderQuantity: 1,
                        delivered: true,
                        orderDate: currentDate.format(DATE_FORMAT),
                        deliveryDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        orderDate: currentDate,
                        deliveryDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a OutOrder', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
