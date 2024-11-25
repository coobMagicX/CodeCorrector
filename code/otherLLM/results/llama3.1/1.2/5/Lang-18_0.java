public class Main {
    public static void main(String[] args) {
        // Create an instance of the class containing the parsePattern() method
        DateFormatParser parser = new DateFormatParser();

        // Set the date format string to be parsed
        String pattern = "yyyy-MM-dd HH:mm:ss z";

        // Call the parsePattern() method and store the result in a list of rules
        List<Rule> rules = parser.parsePattern(pattern);

        // Use the rules for parsing dates (this part depends on how you've defined the Rule class)
    }
}