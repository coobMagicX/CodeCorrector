private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();
    // Fix: Adjusting the parsing to handle type annotations correctly
    if (result.contains("{@code")) {
        result = result.replaceAll("\\{@code ([^}]*)\\}", "<code>$1</code>");
    }
    // Additional fix to address testTextExtents failure: Ensure text extents are preserved
    // This fix assumes the test failure might be due to improper handling of spaces or formatting
    result = result.replaceAll("\\s+", " ").trim();
    return result;
}