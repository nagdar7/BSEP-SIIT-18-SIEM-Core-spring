import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('AlarmHistory e2e test', () => {

    let navBarPage: NavBarPage;
    let alarmHistoryDialogPage: AlarmHistoryDialogPage;
    let alarmHistoryComponentsPage: AlarmHistoryComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load AlarmHistories', () => {
        navBarPage.goToEntity('alarm-history');
        alarmHistoryComponentsPage = new AlarmHistoryComponentsPage();
        expect(alarmHistoryComponentsPage.getTitle())
            .toMatch(/Alarm Histories/);

    });

    it('should load create AlarmHistory dialog', () => {
        alarmHistoryComponentsPage.clickOnCreateButton();
        alarmHistoryDialogPage = new AlarmHistoryDialogPage();
        expect(alarmHistoryDialogPage.getModalTitle())
            .toMatch(/Create or edit a Alarm History/);
        alarmHistoryDialogPage.close();
    });

    it('should create and save AlarmHistories', () => {
        alarmHistoryComponentsPage.clickOnCreateButton();
        alarmHistoryDialogPage.setDateInput(12310020012301);
        expect(alarmHistoryDialogPage.getDateInput()).toMatch('2001-12-31T02:30');
        alarmHistoryDialogPage.save();
        expect(alarmHistoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AlarmHistoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-alarm-history div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class AlarmHistoryDialogPage {
    modalTitle = element(by.css('h4#myAlarmHistoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    dateInput = element(by.css('input#field_date'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setDateInput = function(date) {
        this.dateInput.sendKeys(date);
    }

    getDateInput = function() {
        return this.dateInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
