public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
    if (binaryData == null || binaryData.length == 0) {
        return binaryData;
    }

    // Adjust the calculation of the encoded length based on chunking
    int chunkSize = isChunked ? MIME_CHUNK_SIZE : 0;
    long len = getEncodeLength(binaryData, chunkSize, CHUNK_SEPARATOR);
    if (len > maxResultSize) {
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" +
            len +
            ") than the specified maximum size of " +
            maxResultSize);
    }
    
    Base64 b64 = new Base64(chunkSize, CHUNK_SEPARATOR, urlSafe);
    return b64.encode(binaryData);
}