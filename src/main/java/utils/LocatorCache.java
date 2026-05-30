package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class LocatorCache {
    private static final String CACHE_FILE = "src/test/resources/locator-cache.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, List<String>> cache = load();
    public List<By> get( String key) {
        List<String> values = cache.getOrDefault(key, new ArrayList<>());
        List<By> locators = new ArrayList<>();
        for (String value : values) {
            locators.add(parse(value));
        }
        return locators;
    }

    public synchronized void put(String key, By locator) {
        List<String> list = cache.getOrDefault(key, new ArrayList<>());
        String value = locator.toString();
        
        if (!list.contains(value)) {
            list.add(value);
            cache.put(key, list);
            save();
        }
    }

    private static void save() {
        try (FileWriter writer = new FileWriter(CACHE_FILE)) {
            gson.toJson(cache, writer);
        } catch (Exception e) {
            System.out.println("Cache save failed");
        }
    }

    private static Map<String, List<String>> load() {
        try (
            FileReader reader = new FileReader(CACHE_FILE)) {
            Type type = new TypeToken<Map<String, List<String>>>() {
                    }.getType();

            Map<String, List<String>> data =gson.fromJson(reader, type);
            return data == null ? new HashMap<>() : data;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private By parse(String value) {
        if (value.startsWith("By.xpath:")) {
            return By.xpath(value.replace("By.xpath:","").trim());
        }

        if (value.startsWith("By.cssSelector:")) {
            return By.cssSelector(value.replace("By.cssSelector:","").trim());
        }

        if (value.startsWith("By.id:")) {
            return By.id(value.replace("By.id:","").trim());
        }

        if (value.startsWith("By.name:")) {
            return By.name(value.replace("By.name:","").trim());
        }

        throw new RuntimeException("Unsupported locator");
    }
}