import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Agent e2e test', () => {

    let navBarPage: NavBarPage;
    let agentDialogPage: AgentDialogPage;
    let agentComponentsPage: AgentComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Agents', () => {
        navBarPage.goToEntity('agent');
        agentComponentsPage = new AgentComponentsPage();
        expect(agentComponentsPage.getTitle())
            .toMatch(/Agents/);

    });

    it('should load create Agent dialog', () => {
        agentComponentsPage.clickOnCreateButton();
        agentDialogPage = new AgentDialogPage();
        expect(agentDialogPage.getModalTitle())
            .toMatch(/Create or edit a Agent/);
        agentDialogPage.close();
    });

    it('should create and save Agents', () => {
        agentComponentsPage.clickOnCreateButton();
        agentDialogPage.setDirectoryInput('directory');
        expect(agentDialogPage.getDirectoryInput()).toMatch('directory');
        agentDialogPage.setDescriptionInput('description');
        expect(agentDialogPage.getDescriptionInput()).toMatch('description');
        agentDialogPage.setFilterExpressionInput('filterExpression');
        expect(agentDialogPage.getFilterExpressionInput()).toMatch('filterExpression');
        agentDialogPage.save();
        expect(agentDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AgentComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-agent div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class AgentDialogPage {
    modalTitle = element(by.css('h4#myAgentLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    directoryInput = element(by.css('input#field_directory'));
    descriptionInput = element(by.css('input#field_description'));
    filterExpressionInput = element(by.css('input#field_filterExpression'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setDirectoryInput = function(directory) {
        this.directoryInput.sendKeys(directory);
    }

    getDirectoryInput = function() {
        return this.directoryInput.getAttribute('value');
    }

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    }

    setFilterExpressionInput = function(filterExpression) {
        this.filterExpressionInput.sendKeys(filterExpression);
    }

    getFilterExpressionInput = function() {
        return this.filterExpressionInput.getAttribute('value');
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
