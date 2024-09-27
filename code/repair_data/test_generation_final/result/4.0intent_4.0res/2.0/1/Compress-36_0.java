private InputStream getCurrentStream() throws IOException {
    if (deferredBlockStreams.isEmpty()) {
        throw new IllegalStateException("No current 7z entry (call getNextEntry() first).");
    }
    
    while (deferredBlockStreams.size() > 1) {
        // In solid compression mode we need to decompress all leading folder'
        // streams to get access to an entry. We defer this until really needed
        // so that entire blocks can be skipped without wasting time for decompression.
        final InputStream stream = deferredBlockStreams.remove(0);
        IOUtils.skip(stream, Long.MAX_VALUE);
        stream.close();
    }

    InputStream currentStream = deferredBlockStreams.get(0);
    // Checks if the current stream corresponds to an entry of size 0 and provides a proper end-of-file indication
    if (archive.files[currentEntryIndex].getSize() == 0) {
        return new ByteArrayInputStream(new byte[0]); // Return an empty stream for files of size 0
    }

    return currentStream;
}