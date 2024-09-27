public void close() throws IOException {
    if (!this.closed) {
        super.close();
        this.closed = true;
        
        ArchiveEntry entry;
        while ((entry = getNextEntry()) != null) {
            // Unarchive the file
            closeArchiveEntry();
        }
    }
}