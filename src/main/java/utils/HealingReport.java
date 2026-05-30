package utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HealingReport {
    private static final HealingReport INSTANCE = new HealingReport();
    private final List<String> events = new ArrayList<>();
    private int recovered = 0;
    private int cacheHits = 0;
    private int geminiHits = 0;
    private HealingReport() {
    }

    public static HealingReport getInstance() {
        return INSTANCE;
    }

    public synchronized void log(String original, String healed, String url, String source) {

        recovered++;
        if ("CACHED".equalsIgnoreCase(source)) {
            cacheHits++;
        } else if ("GEMINI".equalsIgnoreCase(source)) {
            geminiHits++;
        }

        String msg =
                "\n ----- SELF HEALING EVENT ------\n"
                + "Page:\n" + url + "\n\n"
                + "Failed Locator:\n" + original + "\n\n"
                + "Recovered Locator:\n" + healed + "\n\n"
                + "Source:\n" + source + "\n\n"
                + "Time:\n" + LocalDateTime.now() + "\n"
                + "----------------------------------\n";
        events.add(msg);
        System.out.println(msg);
    }
    public void printSummary() {
        System.out.println("\n----- SELF HEALING SUMMARY -----");
        System.out.println("Recovered Locators: " + recovered);
        System.out.println("From Cache: " + cacheHits);
        System.out.println("From Gemini: " + geminiHits);
        System.out.println("Total Events: " + events.size());
        System.out.println("------------------------------");
    }
}