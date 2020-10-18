package commands;

public class PA4Strings {

    public static final String RADIOACTIVE_PINWHEEL_INSTR =
            "Size: equal to the number of rows in the Radioactive Pinwheel\n" +
                    "  -- must be an even integer in the range [%d, %d]\n" +
                    "Upward triangle char: used for drawing the upward triangles\n" +
                    "  -- must be a single character within the ASCII range [%d, %d]\n" +
                    "  -- must be different than downward triangle char\n" +
                    "  -- enter nothing to use default character (%c)\n" +
                    "Downward triangle char: used for drawing the downward triangles\n" +
                    "  -- must be a single character within the ASCII range [%d, %d]\n" +
                    "  -- must be different than upward triangle char\n" +
                    "  -- enter nothing to use default character (%c)\n\n";

    // Strings for getting the Radioactive Pinwheel size from the user
    public static final String SIZE_PROMPT =
            "Enter the size of the Radioactive Pinwheel to display: ";
    public static final String SIZE_INT_ERR =
            "\nError: Radioactive Pinwheel size must be a single integer";
    public static final String SIZE_RANGE_ERR =
            "\nError: Radioactive Pinwheel size must be within the range [%d, %d]\n";
    public static final String SIZE_EVEN_ERR =
            "\nError: Radioactive Pinwheel size must be an even integer";

    // Strings for getting the triangle characters from the user
    public static final String CHAR_PROMPT =
            "Enter the character to use for the %s triangles: ";
    public static final String SINGLE_CHAR_ERR =
            "\nError: The %s triangle char must be a single character\n";
    public static final String CHAR_RANGE_ERR =
            "\nError: The %s triangle char must be within ASCII range [%d, %d]\n";

    // Strings to specify the names of the triangle characters
    // These are used to fill in the %s format specifiers in the strings above
    public static final String UPWARD_CHAR_NAME = "upward";
    public static final String DOWNWARD_CHAR_NAME = "downward";

    public static final String UPWARD_DOWNWARD_SAME_CHAR_ERR =
            "\nError: Upward and downward triangle chars cannot be the same char";
}