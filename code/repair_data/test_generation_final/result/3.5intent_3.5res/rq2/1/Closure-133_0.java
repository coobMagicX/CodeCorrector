private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();
    if (result == null) {
        return "";
    }
    return result;
}