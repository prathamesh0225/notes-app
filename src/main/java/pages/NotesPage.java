package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import utils.SelfHealingDriver;
import utils.WaitUtils;

public class NotesPage {

    private final SelfHealingDriver driver;
    private final WaitUtils waitUtils;

    By addNoteButton = By.xpath("//button[contains(text(),'Add Note')]");
    By titleField = By.id("title");
    By descriptionField = By.id("description");
    By categoryDropdown = By.id("category");
    By createButton = By.xpath("//button[contains(text(),'Create')]");
    By validationTitleMessage = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div[3]/div/div/form/div[1]/div[3]/div");
    By validationDescriptionMessage = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div[3]/div/div/form/div[1]/div[4]/div");
    By editButton = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div[4]/div[2]/div/div[4]/div/button[1]");
    By deleteButton = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div[4]/div[2]/div/div[4]/div/button[2]");
    By saveButton = By.xpath("//button[contains(text(),'Save')]");
    By confirmDeleteButton = By.xpath("//*[@data-testid='note-delete-confirm']");
    By noteTitles = By.cssSelector("[data-testid='note-card-title']");

    public NotesPage(SelfHealingDriver driver) {
        this.driver = driver;
        this.waitUtils =new WaitUtils(driver.getWrappedDriver());
    }

    public void clickAddNote() {
        WebElement button = driver.findElement(addNoteButton);
        waitUtils.scrollAndClickUsingJS(button);
    }

    public void enterTitle(String title) {
        driver.findElement(titleField).sendKeys(title);
    }

    public void enterDescription(String description) {
        driver.findElement(descriptionField).sendKeys(description);
    }

    public void selectCategory(String category) {
        driver.findElement(categoryDropdown).sendKeys(category);
    }

    public void clickCreateButton() {
        WebElement button = driver.findElement(createButton);
        waitUtils.scrollAndClickUsingJS(button);
    }

    public boolean verifyNoteExists(String title) {
        return driver.getPageSource().contains(title);
    }

    public String getPageText() {
        return driver.getPageSource();
    }

    public String getTitleValidationMessage() {
        return driver.findElement(validationTitleMessage).getText();
    }

    public String getDescriptionValidationMessage() {
        return driver.findElement(validationDescriptionMessage).getText();
    }

public WebElement getEditButtonForTitle(String title) {

    By noteCard = By.xpath("//div[@data-testid='note-card' and .//div[@data-testid='note-card-title' and contains(text(), '"+ title + "')]]");
    WebElement card = waitUtils.waitForElement(noteCard);
    return card.findElement(By.cssSelector("button[data-testid='note-edit']"));
}

public void clickEditButton(String title) {
    WebElement editBtn = getEditButtonForTitle(title);
    waitUtils.scrollAndClickUsingJS(editBtn);
}

    public void clearTitleField() {
        driver.findElement(titleField).clear();
    }

    public void clearDescriptionField() {
        driver.findElement(descriptionField).clear();
    }

    public void clickSaveButton() {

        WebElement button = driver.findElement(saveButton);
        waitUtils.scrollAndClickUsingJS(button);
    }

public void clickDeleteButton(String title) {

    By noteCard = By.xpath("//div[@data-testid='note-card' and .//div[@data-testid='note-card-title' and normalize-space(text())='"+ title + "']]");
    WebElement card = waitUtils.waitForElement(noteCard);
    WebElement deleteBtn = card.findElement(By.cssSelector("button[data-testid='note-delete']"));
    waitUtils.scrollAndClickUsingJS(deleteBtn);
}
    public void confirmDelete() {

        WebElement button = driver.findElement(confirmDeleteButton);
        waitUtils.scrollAndClickUsingJS(button);
    }

    public boolean verifyNoteDeleted(String noteTitle) {

        By deletedNote = By.xpath("//*[contains(text(),'"+ noteTitle + "')]");

        try {
            waitUtils.waitForInvisibility(deletedNote);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstNoteTitle() {

        List<WebElement> notes = driver.findElements(noteTitles);

        if (notes.isEmpty()) {
            return null;
        }

        return notes.get(0).getText().trim();
    }

    public boolean isNotePresent(String title) throws InterruptedException {

        Thread.sleep(2000);
        return driver.findElements(noteTitles).stream().anyMatch(note -> note.getText().trim().equals(title));
    }
}