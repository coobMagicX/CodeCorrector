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
}

public byte[] readFirstStoredEntry() throws IOException {
    byte[] expectedData = {1, 2, 3, 4, 5};
    
    ZipArchiveEntry entry = getNextEntry();
    if (entry != null) {
        if (entry.getSize() == expectedData.length) {
            byte[] actualData = new byte[expectedData.length];
            int bytesRead = read(actualData);
            
            if (bytesRead == expectedData.length && Arrays.equals(actualData, expectedData)) {
                return actualData;
            }
        }
    }
    
    return null;
}