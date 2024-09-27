InputStream decode(final InputStream in, final Coder coder, byte[] password) throws IOException {
    byte propsByte = coder.properties[0];
    long dictSize = 0;
    for (int i = 1; i < 5; i++) {
        dictSize |= (coder.properties[i] & 0xFF) << (8 * (i - 1));
    }
    if (dictSize > LZMAInputStream.DICT_SIZE_MAX) {
        throw new IOException("Dictionary larger than 4GiB maximum size");
    }
    return new LZMAInputStream(in, -1, propsByte, (int) dictSize);
}