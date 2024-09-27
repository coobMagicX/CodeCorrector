public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
    if (binaryData == null || binaryData.length == 0) {
        return binaryData;
    }

    long len = getEncodeLength(binaryData, MIME_CHUNK_SIZE, CHUNK_SEPARATOR);
    if (len > maxResultSize) {
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" +
            len +
            ") than the specified maxium size of " +
            maxResultSize);
    }

    Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
    return b64.encode(binaryData);
}

public static String encodeBase64String(byte[] binaryData) {
    byte[] encoded = encodeBase64(binaryData, false, false, binaryData.length);
    return StringUtils.newStringUtf8(encoded);
}