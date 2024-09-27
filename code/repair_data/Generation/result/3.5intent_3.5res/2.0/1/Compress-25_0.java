public ZipArchiveInputStream(InputStream inputStream,
                             String encoding,
                             boolean useUnicodeExtraFields,
                             boolean allowStoredEntriesWithDataDescriptor) {
    zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    this.useUnicodeExtraFields = useUnicodeExtraFields;
    in = new PushbackInputStream(inputStream, buf.capacity());
    this.allowStoredEntriesWithDataDescriptor =
        allowStoredEntriesWithDataDescriptor;
    // haven't read anything so far
    // read the first stored entry
    try {
        getNextEntry();
    } catch (IOException e) {
        throw new RuntimeException("Error reading first stored entry", e);
    }
}

private void drainCurrentEntryData() throws IOException {
    long remaining = current.entry.getCompressedSize() - current.bytesReadFromStream;
    while (remaining > 0) {
        long n = in.read(buf.array(), 0, (int) Math.min(buf.capacity(), remaining));
        if (n < 0) {
            throw new EOFException("Truncated ZIP entry: " + current.entry.getName());
        } else {
            count(n);
            remaining -= n;
        }
    }
}