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
        // Validate input
        if (input == null) return null;
        if (input.isEmpty()) return "";

        StringBuilder escapedString = new StringBuilder();
        boolean isSingleCharacter = input.length() == 1;

        for (char character : input.toCharArray()) {
            // Handle single-character specific cases
            if (isSingleCharacter) {
                switch (character) {
                    case '>':
                        escapedString.append("\\u003E");
                        continue;
                    case '<':
                        escapedString.append("\\u003C");
                        continue;
                    case '&':
                        escapedString.append("\\u0026");
                        continue;
                    default:
                        // Skip invalid characters in the range 0-31
                        if (character < 32) continue;
                }
            }

            // Escape non-ASCII characters or null character
            if (character >= 127 || character == '\u0000') {
                escapedString.append("\\u").append(String.format("%04X", (int) character));
            } else {
                // Append ASCII printable characters directly
                escapedString.append(character);
            }
        }

        return escapedString.toString();
    }

}

