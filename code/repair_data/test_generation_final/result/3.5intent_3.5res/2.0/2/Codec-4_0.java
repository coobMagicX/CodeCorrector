public Base64(boolean isChunked, boolean urlSafe) {
    if (isChunked) {
        this(0, CHUNK_SEPARATOR, urlSafe);
    } else {
        this(urlSafe);
    }
}