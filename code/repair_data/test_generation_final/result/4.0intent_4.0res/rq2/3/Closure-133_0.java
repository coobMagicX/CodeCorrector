private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();
    if (result.contains("{@code")) {
        result = result.replace("{@code", "{@code ");
    }
    // Ensure closing curly brace for `@code` annotation has a space before it if not already present
    result = result.replaceAll("\\}(?! )", "} ");
    return result;
}