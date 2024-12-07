package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EscapeUnicodeFunctionalTests {

    @Test
    void testEscapeUnicode_withNullInput() {
        String input = null;
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertNull(result); // null input should return null
    }

    @Test
    void testEscapeUnicode_withEmptyString() {
        String input = "";
        String expected = "";
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, result); // Empty string should return an empty string

    }

    @Test
    void testEscapeUnicode_withAsciiCharacters() {
        String input = "Hello, World!";
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(input, result, "ASCII characters should remain unchanged.");
    }

    @Test
    void testEscapeUnicode_withNonAsciiCharacters() {
        String input = "Hello, 世界!";
        String expected = "Hello, \\u4E16\\u754C!"; // Correct Unicode escape sequence
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testEscapeUnicode_withMixedCharactersABC() {
        String input = "A > B & C < D";
        String expected = "A > B & C < D"; // ASCII characters should remain unchanged
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testEscapeUnicode_withEdgeCases() {
        String input = "\u0000\u007F\u0080\uFFFF"; // Edge case with control characters
        String expected = "\\u0000\\u007F\\u0080\\uFFFF"; // Correct escape sequences for control characters
        String result = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, result);
    }

    // Assuming escapeUnicode is already implemented.
    // Tests for all ASCII characters from 0 to 127
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
            32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64,
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97,
            98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127})
    void testEscapeUnicode_forAsciiCharacters(int codePoint) {
        char character = (char) codePoint;
        // Define expected output
        String expected;
        if (character == '>') {
            expected = "\\u003E";
        } else if (character == '<') {
            expected = "\\u003C";
        } else if (character == '&') {
            expected = "\\u0026";
        }
        else if (codePoint < 32) {
            expected = "";
        }
        else if (codePoint < 127) {
            expected = String.valueOf(character); // All other ASCII characters remain unchanged
        } else {
            expected = String.format("\\u%04X", codePoint); // Non-ASCII characters are escaped
        }

        // Escape the character using UnicodeEscaper
        String actual = UnicodeEscaper.escapeUnicodeFunctional(String.valueOf(character));

        System.out.println("Expected: '" + expected + "' (length: " + expected.length() + ")");
        System.out.println("Actual: '" + actual + "' (length: " + actual.length() + ")");
        // Assert that expected matches actual
        Assertions.assertEquals(expected.trim(), actual.trim(), "Mismatch for codePoint: " + codePoint);
    }

    @Test
    public void testUnicodeEscaper() {

        // ASCII characters (code points 0–127) should not be escaped
        Assertions.assertEquals("A", UnicodeEscaper.escapeUnicodeFunctional("A")); // ASCII letter
        Assertions.assertEquals("0", UnicodeEscaper.escapeUnicodeFunctional("0")); // Null character as ASCII
        Assertions.assertEquals("~", UnicodeEscaper.escapeUnicodeFunctional("~")); // ASCII tilde

        // Non-ASCII characters should be escaped
        Assertions.assertEquals("\\u00E9", UnicodeEscaper.escapeUnicodeFunctional("é")); // Non-ASCII character
        Assertions.assertEquals("\\u263A", UnicodeEscaper.escapeUnicodeFunctional("☺")); // Smiley face
    }

    // Edge case: test escaping higher Unicode characters like \u0080 and \uFFFF
    @Test
    void testEscapeUnicode_forEdgeCases() {
        String input = "\u0080\uFFFF"; // Testing a range beyond ASCII
        String expected = "\\u0080\\uFFFF"; // We expect the Unicode escape sequence for these characters
        String actual = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, actual);
    }

    // Test with mixed input including non-ASCII characters
    @Test
    void testEscapeUnicode_withMixedCharacters() {
        String input = "Hello, 世界!"; // Contains ASCII and non-ASCII characters
        String expected = "Hello, \\u4E16\\u754C!"; // The Chinese characters should be escaped
        String actual = UnicodeEscaper.escapeUnicodeFunctional(input);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\uD83D\uDE00", "\uD83E\uDD73"})
    void testEscapeUnicode_highUnicodeCodePoints(String input) {
        // Define expected output manually, based on logic.
        String expected = input
                .replace("\uD83D\uDE00", "\\uD83D\\uDE00")  // Unicode emoji: 😀
                .replace("\uD83E\uDD73", "\\uD83E\\uDD73"); // Unicode emoji: 🤳

        // Escape the string using UnicodeEscaper
        String actual = UnicodeEscaper.escapeUnicodeFunctional(input);

        // Assert that expected matches actual
        Assertions.assertEquals(expected, actual, "Mismatch for input: " + input);
    }
}