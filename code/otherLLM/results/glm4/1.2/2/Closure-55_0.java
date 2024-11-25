private static boolean checkCompilesToSame(String code1, String code2) {
    // Trim both strings to remove leading and trailing whitespace
    code1 = code1.trim();
    code2 = code2.trim();

    // Split the strings into lines and compare them line by line
    String[] lines1 = code1.split("\\r?\\n");
    String[] lines2 = code2.split("\\r?\\n");

    if (lines1.length != lines2.length) {
        return false;
    }

    for (int i = 0; i < lines1.length; i++) {
        // Trim each line to remove any leading/trailing whitespace before comparison
        String trimmedLine1 = lines1[i].trim();
        String trimmedLine2 = lines2[i].trim();

        // If the lines are not equal, return false
        if (!trimmedLine1.equals(trimmedLine2)) {
            return false;
        }
    }

    // If all lines matched, return true
    return true;
}

// Example usage of the checkCompilesToSame method:
private static void exampleUsage() {
    String codeSnippet1 = "function MyObject() { /* Comment */\n  this.prop = 'value'; \n}\nMyObject.prototype.getProp = function() {\n  return this.prop;\n}";
    String codeSnippet2 = "function MyObject() { /* Comment */ this.prop = 'value'; }\nMyObject.prototype.getProp = function() { return this.prop; }";

    boolean result = checkCompilesToSame(codeSnippet1, codeSnippet2);
    System.out.println("Code compiles to the same: " + result);
}

// Call the exampleUsage method to test if it works correctly
exampleUsage();