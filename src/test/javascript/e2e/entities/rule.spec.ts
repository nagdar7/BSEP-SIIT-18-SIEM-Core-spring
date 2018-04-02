import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Rule e2e test', () => {

    let navBarPage: NavBarPage;
    let ruleDialogPage: RuleDialogPage;
    let ruleComponentsPage: RuleComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Rules', () => {
        navBarPage.goToEntity('rule');
        ruleComponentsPage = new RuleComponentsPage();
        expect(ruleComponentsPage.getTitle())
            .toMatch(/Rules/);

    });

    it('should load create Rule dialog', () => {
        ruleComponentsPage.clickOnCreateButton();
        ruleDialogPage = new RuleDialogPage();
        expect(ruleDialogPage.getModalTitle())
            .toMatch(/Create or edit a Rule/);
        ruleDialogPage.close();
    });

    it('should create and save Rules', () => {
        ruleComponentsPage.clickOnCreateButton();
        ruleDialogPage.setNameInput('name');
        expect(ruleDialogPage.getNameInput()).toMatch('name');
        ruleDialogPage.setRuleInput('rule');
        expect(ruleDialogPage.getRuleInput()).toMatch('rule');
        ruleDialogPage.save();
        expect(ruleDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class RuleComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-rule div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class RuleDialogPage {
    modalTitle = element(by.css('h4#myRuleLabel'));
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
