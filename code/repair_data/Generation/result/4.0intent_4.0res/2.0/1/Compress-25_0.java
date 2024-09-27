public ZipArchiveInputStream(InputStream inputStream,
                             String encoding,
                             boolean useUnicodeExtraFields,
                             boolean allowStoredEntriesWithDataDescriptor) {
    zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    this.useUnicodeExtraFields = useUnicodeExtraFields;
    // Ensure the buffer size in PushbackInputStream is large enough to handle zip entries properly
    in = new PushbackInputStream(inputStream, 4096); // Increased buffer size to accommodate larger entries
    this.allowStoredEntriesWithDataDescriptor =
        allowStoredEntriesWithDataDescriptor;
    // haven't read anything so far
}