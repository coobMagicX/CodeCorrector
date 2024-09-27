public static String encodeBase64String(byte[] binaryData) {
    if (binaryData == null || binaryData.length == 0) {
        return null;
    }
    
    byte[] encodedBytes = encodeBase64(binaryData, false, true, Integer.MAX_VALUE);
    return new String(encodedBytes, StandardCharsets.UTF_8);
}