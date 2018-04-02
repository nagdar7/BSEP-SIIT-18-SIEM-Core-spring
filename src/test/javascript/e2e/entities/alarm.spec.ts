import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Alarm e2e test', () => {

    let navBarPage: NavBarPage;
    let alarmDialogPage: AlarmDialogPage;
    let alarmComponentsPage: AlarmComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Alarms', () => {
        navBarPage.goToEntity('alarm');
        alarmComponentsPage = new AlarmComponentsPage();
        expect(alarmComponentsPage.getTitle())
            .toMatch(/Alarms/);

    });

    it('should load create Alarm dialog', () => {
        alarmComponentsPage.clickOnCreateButton();
        alarmDialogPage = new AlarmDialogPage();
        expect(alarmDialogPage.getModalTitle())
            .toMatch(/Create or edit a Alarm/);
        alarmDialogPage.close();
    });

    it('should create and save Alarms', () => {
        alarmComponentsPage.clickOnCreateButton();
        alarmDialogPage.setNameInput('name');
        expect(alarmDialogPage.getNameInput()).toMatch('name');
        alarmDialogPage.statusSelectLastOption();
        alarmDialogPage.save();
        expect(alarmDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AlarmComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-alarm div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class AlarmDialogPage {
    modalTitle = element(by.css('h4#myAlarmLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    statusSelect = element(by.css('select#field_status'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    }

    setStatusSelect = function(status) {
        this.statusSelect.sendKeys(status);
    }

    getStatusSelect = function() {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    statusSelectLastOption = function() {
        this.statusSelect.all(by.tagName('option')).last().click();
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
