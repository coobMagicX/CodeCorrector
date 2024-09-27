protected void setName(String name, byte[] rawName) {
    // Converts rawName using appropriate character encoding
    if (rawName != null && rawName.length > 0) {
        try {
            String rawNameString = new String(rawName, "UTF-8");
            // Normalize the file path by replacing backslashes with forward slashes
            rawNameString = rawNameString.replace('\\', '/');
            this.name = rawNameString;
        } catch (UnsupportedEncodingException e) {
            // Fallback to the original name if there's an encoding issue
            this.name = name;
        }
    } else {
        this.name = name;
    }
}