package utils;

import org.openqa.selenium.*;
import java.util.List;

public class SelfHealingDriver {

    private final WebDriver driver;
    private final GeminiHealingAgent healingAgent;
    private final LocatorCache cache;
    private final HealingReport report;

    public SelfHealingDriver(WebDriver driver) {
        this.driver = driver;
        this.healingAgent = new GeminiHealingAgent();
        this.cache = new LocatorCache();
        this.report = HealingReport.getInstance();
    }

    public WebDriver getWrappedDriver() {
        return driver;
    }

    public void get(String url) {
        driver.get(url);
    }

    public WebDriver.Navigation navigate() {
    return driver.navigate();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public WebElement findElement(By locator) {
    try {
        return driver.findElement(locator);
    } catch (NoSuchElementException e) {
        try {
            Thread.sleep(2500);
            return driver.findElement(locator);
        } catch (Exception ignored) {
        }
        return heal(locator);
    }
}

public List<WebElement> findElements(By locator) {
    return driver.findElements(locator);
}

private WebElement heal(By brokenLocator) {

    System.out.println("\n===== GEMINI HEAL START =====");
    System.out.println("Broken locator: " + brokenLocator);
    String key = brokenLocator.toString();
    Object lock = LocatorLockManager.getLock(key);
    synchronized (lock) {
        if (isLocatorPresentInDom(brokenLocator)) {
            for (int i = 0; i < 6; i++) {
                try {
                    Thread.sleep(500);
                    return driver.findElement(brokenLocator);
                } catch (Exception ignored) {
                }
            }
        }

        List<By> cached = cache.get(key);

        for (By locator : cached) {
            try {
                WebElement element = driver.findElement(locator);
                report.log(brokenLocator.toString(), locator.toString(), driver.getCurrentUrl(), "CACHED");
                return element;
            } catch (Exception ignored) {
            }
        }
        System.out.println("Calling Gemini...");
        String dom = driver.getPageSource();
        List<By> candidates =healingAgent
                        .findCandidates(key, dom);
        for (By locator : candidates) {
            try {
                WebElement element = driver.findElement(locator);
                cache.put(key, locator);
                report.log(brokenLocator.toString(), locator.toString(), driver.getCurrentUrl(), "GEMINI");
                return element;
            } catch (Exception ignored) {
            }
        }

        throw new NoSuchElementException("Unable to heal locator: " + brokenLocator);
    }
}
private boolean isLocatorPresentInDom(By locator) {
    String dom = driver.getPageSource();
    String value = locator.toString();
    if (value.contains("By.id:")) {
        String id = value.replace("By.id:","").trim();
        return dom.contains("id=\""+ id + "\"");
    }

    if (value.contains("By.name:")) {
        String name = value.replace("By.name:","").trim();
        return dom.contains("name=\""+ name + "\"");
    }
    return false;
}
}