protected Object _deserializeFromEmptyString() throws IOException {
    // As per [databind#398], URI requires special handling
    if (_kind == STD_URI) {
        return URI.create("");
    }
    // As per [databind#1123], handling for Locale
    if (_kind == STD_LOCALE) {
        return Locale.ROOT;
    }
    // Handling for InetAddress based on new failing test
    if (_kind == STD_INET_ADDRESS) {
        return InetAddress.getByName("");
    }
    return super._deserializeFromEmptyString();
}