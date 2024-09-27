InputStream decode(final InputStream in, final Coder coder, byte[] password) throws IOException {
    byte propsByte = coder.properties[0];
    long dictSize = 0;
    for (int i = 0; i < 4; i++) {
        dictSize |= ((long)(coder.properties[i + 1] & 0xFF)) << (8 * i);
    }
    if (dictSize > Integer.MAX_VALUE) {
        throw new IOException("Dictionary size exceeds maximum integer value");
    }
    if (dictSize > LZMAInputStream.DICT_SIZE_MAX) {
        throw new IOException("Dictionary larger than 4GiB maximum size");
    }
    return new LZMAInputStream(in, -1, propsByte, (int) dictSize);
}