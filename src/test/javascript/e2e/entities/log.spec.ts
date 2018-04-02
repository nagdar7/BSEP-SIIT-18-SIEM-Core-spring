import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Log e2e test', () => {

    let navBarPage: NavBarPage;
    let logDialogPage: LogDialogPage;
    let logComponentsPage: LogComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Logs', () => {
        navBarPage.goToEntity('log');
        logComponentsPage = new LogComponentsPage();
        expect(logComponentsPage.getTitle())
            .toMatch(/Logs/);

    });

    it('should load create Log dialog', () => {
        logComponentsPage.clickOnCreateButton();
        logDialogPage = new LogDialogPage();
        expect(logDialogPage.getModalTitle())
            .toMatch(/Create or edit a Log/);
        logDialogPage.close();
    });

    it('should create and save Logs', () => {
        logComponentsPage.clickOnCreateButton();
        logDialogPage.setFacilityInput('5');
        expect(logDialogPage.getFacilityInput()).toMatch('5');
        logDialogPage.setSevernityInput('5');
        expect(logDialogPage.getSevernityInput()).toMatch('5');
        logDialogPage.setMessageInput('message');
        expect(logDialogPage.getMessageInput()).toMatch('message');
        logDialogPage.save();
        expect(logDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class LogComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-log div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class LogDialogPage {
    modalTitle = element(by.css('h4#myLogLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    facilityInput = element(by.css('input#field_facility'));
    severnityInput = element(by.css('input#field_severnity'));
    messageInput = element(by.css('input#field_message'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setFacilityInput = function(facility) {
        this.facilityInput.sendKeys(facility);
    }

    getFacilityInput = function() {
        return this.facilityInput.getAttribute('value');
    }

    setSevernityInput = function(severnity) {
        this.severnityInput.sendKeys(severnity);
    }

    getSevernityInput = function() {
        return this.severnityInput.getAttribute('value');
    }

    setMessageInput = function(message) {
        this.messageInput.sendKeys(message);
    }

    getMessageInput = function() {
        return this.messageInput.getAttribute('value');
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
