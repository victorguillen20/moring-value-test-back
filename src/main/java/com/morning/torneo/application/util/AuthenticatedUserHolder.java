package com.morning.torneo.application.util;

public final class AuthenticatedUserHolder {

    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    private AuthenticatedUserHolder() {
    }

    public static void set(String username) {
        currentUsername.set(username);
    }

    public static String get() {
        return currentUsername.get();
    }

    public static void clear() {
        currentUsername.remove();
    }
}
