private void resolveLocalFileHeaderData(Map<ZipArchiveEntry, NameAndComment> entriesWithoutUTF8Flag) throws IOException {
    for (ZipArchiveEntry ze : entries.keySet()) {
        OffsetEntry offsetEntry = entries.get(ze);
        long offset = offsetEntry.headerOffset;
        archive.seek(offset + LFH_OFFSET_FOR_FILENAME_LENGTH);
        byte[] b = new byte[SHORT];
        archive.readFully(b);
        int fileNameLen = ZipShort.getValue(b);
        archive.readFully(b);
        int extraFieldLen = ZipShort.getValue(b);
        int lenToSkip = fileNameLen;
        while (lenToSkip > 0) {
            int skipped = archive.skipBytes(lenToSkip);
            if (skipped <= 0) {
                throw new RuntimeException("failed to skip file name in local file header");
            }
            lenToSkip -= skipped;
        }
        byte[] localExtraData = new byte[extraFieldLen];
        archive.readFully(localExtraData);
        ze.setExtra(localExtraData);
        offsetEntry.dataOffset = offset + LFH_OFFSET_FOR_FILENAME_LENGTH + SHORT + SHORT + fileNameLen + extraFieldLen;

        if (entriesWithoutUTF8Flag.containsKey(ze)) {
            String orig = ze.getName();
            NameAndComment nc = entriesWithoutUTF8Flag.get(ze);
            ZipUtil.setNameAndCommentFromExtraFields(ze, nc.name, nc.comment);
            if (!orig.equals(ze.getName())) {
                nameMap.remove(orig);
                nameMap.put(ze.getName(), ze);
            }
        }
    }
    
    // Fix for the new failed test case
    int nameMapSize = nameMap.size();
    ZipArchiveEntry[] entriesArray = new ZipArchiveEntry[nameMapSize];
    int index = 0;
    for (ZipArchiveEntry entry : nameMap.values()) {
        entriesArray[index++] = entry;
    }
    Arrays.sort(entriesArray, ZipArchiveEntryComparator.NAME_COMPARATOR);
    for (int i = 0; i < nameMapSize - 1; i++) {
        entriesArray[i].setNextEntry(entriesArray[i + 1]);
    }
    if (nameMapSize > 0) {
        entriesArray[nameMapSize - 1].setNextEntry(null);
    }
}