package utils;

import java.util.concurrent.ConcurrentHashMap;

public class LocatorLockManager {
    private static final ConcurrentHashMap<String, Object> LOCKS = new ConcurrentHashMap<>();
    private LocatorLockManager() {
    }
    public static Object getLock(String locatorKey) {
        return LOCKS.computeIfAbsent(locatorKey, key -> new Object());
    }
}