package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.openqa.selenium.By;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiHealingAgent {

    private static final String API_KEY = ConfigReader.get("GEMINI_API_KEY");

    static { 
        System.out.println("Gemini key loaded = " + API_KEY);
        }

    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + API_KEY;
    private static final MediaType JSON = MediaType.get("application/json");
    private final OkHttpClient client = new OkHttpClient();

    public List<By> findCandidates(String brokenLocator, String dom) {
        System.out.println("\n===== GEMINI HEAL START =====");
        System.out.println("Broken locator: " + brokenLocator);
        List<By> candidates = new ArrayList<>();

        try {
            if (API_KEY == null || API_KEY.isBlank()) {
                System.out.println("GEMINI_API_KEY missing");
                return candidates;
            }

            String prompt =
                    """
                    You are a Selenium locator healing assistant.

                    A Selenium locator failed.

                    Broken locator:
                    %s

                    Current DOM:
                    %s

                    Return ONLY valid JSON.

                    Example:
                    {
                      "locators":[
                        {
                          "type":"css",
                          "value":"button[type='submit']"
                        },
                        {
                          "type":"xpath",
                          "value":"//button[text()='Login']"
                        },
                        {
                          "type":"id",
                          "value":"email"
                        }
                      ]
                    }
                    """
                            .formatted(
                                    brokenLocator,
                                    trimDom(dom)
                            );

            JsonObject textPart = new JsonObject();
            textPart.addProperty("text",prompt);
            JsonArray parts = new JsonArray();
            parts.add(textPart);
            JsonObject content =new JsonObject();
            content.add("parts", parts);
            JsonArray contents = new JsonArray();
            contents.add(content);
            JsonObject body = new JsonObject();
            body.add("contents", contents);
            System.out.println("Calling Gemini API...");

            Request request = new Request.Builder()
                            .url(URL)
                            .post(RequestBody.create(body.toString(),JSON))
                            .build();

            Response response = executeWithRetry(request);

            if (!response.isSuccessful()) {
                System.out.println("Gemini API failed: " + response.code());
                return candidates;
            }

            String responseJson = response.body().string();

            JsonObject root = JsonParser.parseString(responseJson).getAsJsonObject();

            JsonArray apiCandidates = root.getAsJsonArray("candidates");

            if (apiCandidates == null || apiCandidates.isEmpty()) {
                return candidates;
            }
        System.out.println("Gemini raw response:");
        System.out.println(responseJson);

        String generatedText =
                apiCandidates
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0)
                .getAsJsonObject()
                .get("text")
                .getAsString();

        System.out.println("Gemini locator JSON:");
        System.out.println(generatedText);

        generatedText = generatedText
                .replace("```json", "")
                .replace("```", "")
                .trim();

        JsonObject parsed = JsonParser.parseString(generatedText).getAsJsonObject();

        JsonArray locators = parsed.getAsJsonArray("locators");

            if (locators == null) {
                return candidates;
            }

            for (JsonElement element : locators) {
                JsonObject item = element.getAsJsonObject();
                String type = item.get("type").getAsString();
                String value =item.get("value").getAsString();
                By locator = convert(type, value);

                if (locator != null) {
                    candidates.add(locator);
                }
            }
        } catch (Exception e) {
            System.out.println("Gemini healing error: " + e.getMessage());
        }
        return candidates;
    }

    private Response executeWithRetry(Request request) throws IOException {

        int attempts = 3;
        long delay = 2000;
        for (int i = 1; i <= attempts; i++) {

            Response response = client.newCall(request).execute();
            if (response.code() != 429) {
                return response;
            }
            System.out.println("Gemini rate limited. retry " + i);

            try {
                Thread.sleep(delay);
            } catch (
                    InterruptedException ignored) {
            }
            delay *= 2;
        }

        throw new IOException("Gemini rate limit exceeded");
    }

    private By convert(String type, String value) {
        return switch (type.toLowerCase()) {

            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "classname" -> By.className(value);
            default -> null;
        };
    }
 
    private String trimDom(String dom) {

        if (dom == null) {
            return "";
        }
        int max = 12000;
        if (dom.length() > max) {
            return dom.substring(0,max);
        }
        return dom;
    }
}
