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

    /**
     * Escapes Unicode characters in the input string using a functional approach.
     * Characters in the ASCII range (code points <= 127) remain unchanged unless they are control characters.
     * All other characters are replaced with their Unicode escape sequences.
     *
     * @param input The string to process.
     * @return A string with non-ASCII characters escaped in Unicode format.
     */
    public static String escapeUnicodeFunctional(String input) {
        if (input == null) return null;
        if (input.isEmpty()) return "";

        boolean isSingleCharacter = input.length() == 1;

        return input.chars() // Stream over code points
                .mapToObj(codePoint -> processCharacter((char) codePoint, isSingleCharacter))
                .reduce("", String::concat); // Collect into a single string
    }

    private static String processCharacter(char character, boolean isSingleCharacter) {
        // Handle special cases for single-character input
        if (isSingleCharacter) {
            switch (character) {
                case '>': return "\\u003E";
                case '<': return "\\u003C";
                case '&': return "\\u0026";
                default: if (character < 32) return ""; // Skip invalid control characters
            }
        }

        // Escape non-ASCII characters
        if (character >= 127 || character == '\u0000') {
            return String.format("\\u%04X", (int) character);
        }

        // Default case: return the character as-is
        return String.valueOf(character);
    }

}

