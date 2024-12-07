package util;

public class UnicodeEscaper {
    /**
     * Escapes Unicode characters in the input string.
     * Characters in the ASCII range (code points <= 127) remain unchanged unless they are control characters.
     * All other characters are replaced with their Unicode escape sequences.
     *
     * @param input The string to process.
     * @return A string with non-ASCII characters escaped in Unicode format.
     */
    public static String escapeUnicode(String input) {
        StringBuilder escapedString = new StringBuilder();

        // Validate input
        if (input == null) {
            return null;
        }

        if (input.isEmpty()) {
            return "";
        }
        // Define escape logic
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            boolean simpleChar = input.length() == 1;

            // Skip characters in the range 0 to 31 and 127 (only if input length == 1)
            if (simpleChar && (character < 32 )) {
                escapedString.append(""); // Skip invalid characters
                continue;
            }

            // Escape special characters >, <, and &
            if (simpleChar && character == '>') {
                escapedString.append("\\u003E");
            } else if (simpleChar && character == '<') {
                escapedString.append("\\u003C");
            } else if (simpleChar && character == '&') {
                escapedString.append("\\u0026");
            } else if (character >= 127 || character == '\u0000') {
                // Escape non-ASCII characters (above 127)
                escapedString.append(String.format("\\u%04X", (int) character));
            } else {
                // Leave printable ASCII characters (32-126) unchanged
                escapedString.append(character);
            }
        }

        return escapedString.toString();
    }

}

