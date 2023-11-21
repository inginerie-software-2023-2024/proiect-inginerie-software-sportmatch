package com.example.sportmatch;

public class AllMethods {
    private static String username;
    private static String password;
    private static String birthDate;
    private static String fullName;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AllMethods.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AllMethods.password = password;
    }

    public static String getBirthDate() {
        return birthDate;
    }

    public static void setBirthDate(String birthDate) {
        AllMethods.birthDate = birthDate;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        AllMethods.fullName = fullName;
    }
}
