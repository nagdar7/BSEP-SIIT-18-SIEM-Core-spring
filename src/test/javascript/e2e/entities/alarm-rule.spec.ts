import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('AlarmRule e2e test', () => {

    let navBarPage: NavBarPage;
    let alarmRuleDialogPage: AlarmRuleDialogPage;
    let alarmRuleComponentsPage: AlarmRuleComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load AlarmRules', () => {
        navBarPage.goToEntity('alarm-rule');
        alarmRuleComponentsPage = new AlarmRuleComponentsPage();
        expect(alarmRuleComponentsPage.getTitle())
            .toMatch(/Alarm Rules/);

    });

    it('should load create AlarmRule dialog', () => {
        alarmRuleComponentsPage.clickOnCreateButton();
        alarmRuleDialogPage = new AlarmRuleDialogPage();
        expect(alarmRuleDialogPage.getModalTitle())
            .toMatch(/Create or edit a Alarm Rule/);
        alarmRuleDialogPage.close();
    });

    it('should create and save AlarmRules', () => {
        alarmRuleComponentsPage.clickOnCreateButton();
        alarmRuleDialogPage.setNameInput('name');
        expect(alarmRuleDialogPage.getNameInput()).toMatch('name');
        alarmRuleDialogPage.setRuleInput('rule');
        expect(alarmRuleDialogPage.getRuleInput()).toMatch('rule');
        alarmRuleDialogPage.save();
        expect(alarmRuleDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AlarmRuleComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-alarm-rule div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class AlarmRuleDialogPage {
    modalTitle = element(by.css('h4#myAlarmRuleLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    ruleInput = element(by.css('input#field_rule'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    }

    setRuleInput = function(rule) {
        this.ruleInput.sendKeys(rule);
    }

    getRuleInput = function() {
        return this.ruleInput.getAttribute('value');
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
