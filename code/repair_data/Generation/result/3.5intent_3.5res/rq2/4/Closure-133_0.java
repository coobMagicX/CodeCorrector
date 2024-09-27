private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();

    // Fix the parsing of type annotation
    result = result.replaceAll("\\{@code (.+?)\\}", "<code>$1</code>");

    // Fix the parsing of text extents
    result = result.replaceAll("\\{@text (\\d+?), (\\d+?)\\}", "<span class=\"text-extent\" start=\"$1\" end=\"$2\"></span>");

    return result;
}