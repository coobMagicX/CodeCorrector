public TarArchiveEntry getNextTarEntry() throws IOException {
    if (hasHitEOF) {
        throw new IOException("No more entries in Tar archive");
    }

    if (currEntry != null) {
        long numToSkip = entrySize - entryOffset;

        while (numToSkip > 0) {
            long skipped = skip(numToSkip);
            if (skipped <= 0) {
                throw new RuntimeException("failed to skip current tar entry");
            }
            numToSkip -= skipped;
        }

        readBuf = null;
    }

    byte[] headerBuf = getRecord();

    if (hasHitEOF) {
        currEntry = null;
        throw new IOException("No more entries in Tar archive");
    }

    currEntry = new TarArchiveEntry(headerBuf);
    entryOffset = 0;
    entrySize = currEntry.getSize();

    if (currEntry.isGNULongNameEntry()) {
        // read in the name
        StringBuffer longName = new StringBuffer();
        byte[] buf = new byte[SMALL_BUFFER_SIZE];
        int length = 0;
        while ((length = read(buf)) >= 0) {
            longName.append(new String(buf, 0, length));
        }
        getNextEntry();
        if (currEntry == null) {
            // Bugzilla: 40334
            // Malformed tar file - long entry name not followed by entry
            throw new IOException("No more entries in Tar archive");
        }
        // remove trailing null terminator
        if (longName.length() > 0
            && longName.charAt(longName.length() - 1) == 0) {
            longName.deleteCharAt(longName.length() - 1);
        }
        currEntry.setName(longName.toString());
    }

    if (currEntry.isPaxHeader()){ // Process Pax headers
        paxHeaders();
    }

    if (currEntry.isGNUSparse()){ // Process sparse files
        readGNUSparse();
    }

    // If the size of the next element in the archive has changed
    // due to a new size being reported in the posix header
    // information, we update entrySize here so that it contains
    // the correct value.
    entrySize = currEntry.getSize();
    
    if (currEntry.getSize() < 0) {
        throw new IOException("Invalid entry size: " + currEntry.getSize());
    }

    return currEntry;
}