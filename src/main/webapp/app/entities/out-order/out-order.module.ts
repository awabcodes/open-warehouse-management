import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenWarehouseManagementSharedModule } from 'app/shared';
import {
    OutOrderComponent,
    OutOrderDetailComponent,
    OutOrderUpdateComponent,
    OutOrderDeletePopupComponent,
    OutOrderDeleteDialogComponent,
    outOrderRoute,
    outOrderPopupRoute
} from './';

const ENTITY_STATES = [...outOrderRoute, ...outOrderPopupRoute];

@NgModule({
    imports: [OpenWarehouseManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OutOrderComponent,
        OutOrderDetailComponent,
        OutOrderUpdateComponent,
        OutOrderDeleteDialogComponent,
        OutOrderDeletePopupComponent
    ],
    entryComponents: [OutOrderComponent, OutOrderUpdateComponent, OutOrderDeleteDialogComponent, OutOrderDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenWarehouseManagementOutOrderModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
