private SevenZArchiveEntry getNextEntry() {
    // existing code
    
    // Add this check for entry size
    if (entry.getSize() == 0) {
        // Skip the entry with 0 size
        continue;
    }
    
    // existing code
}

private InputStream getCurrentStream() throws IOException {
    if (deferredBlockStreams.isEmpty()) {
        throw new IllegalStateException("No current 7z entry (call getNextEntry() first).");
    }
    
    InputStream stream = new ByteArrayInputStream(deferredBlockStreams.get(0));
    deferredBlockStreams.remove(0);
    return stream;
}