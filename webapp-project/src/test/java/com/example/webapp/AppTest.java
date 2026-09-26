package com.example.webapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 tests, run by Maven Surefire during the "Test" stage.
 * Requires the Jenkins build agent to have internet access to Maven
 * Central so it can download junit-jupiter (declared in pom.xml).
 */
public class AppTest {

    @Test
    void homePageContainsExpectedContent() {
        String html = App.buildHomePage();
        assertTrue(html.contains("Jenkins pipeline"), "Homepage should mention 'Jenkins pipeline'");
        assertTrue(html.contains("/health"), "Homepage should link to /health");
    }

    @Test
    void homePageIsWellFormedHtml() {
        String html = App.buildHomePage();
        assertTrue(html.trim().startsWith("<!DOCTYPE html>"), "Should start with DOCTYPE");
        assertTrue(html.trim().endsWith("</html>"), "Should end with </html>");
    }
}
