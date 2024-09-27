private void resolveLocalFileHeaderData(Map<ZipArchiveEntry, NameAndComment> entriesWithoutUTF8Flag) throws IOException {
    for (ZipArchiveEntry ze : entries.keySet()) {
        OffsetEntry offsetEntry = entries.get(ze);
        long offset = offsetEntry.headerOffset;
        archive.seek(offset + LFH_OFFSET_FOR_FILENAME_LENGTH);
        byte[] b = new byte[SHORT];
        archive.readFully(b);
        int fileNameLen = ZipShort.getValue(b);
        byte[] fileNameBytes = new byte[fileNameLen];
        archive.readFully(fileNameBytes);
        String fileName;
        if (ze.getGeneralPurposeBit().usesUTF8ForNames()) {
            fileName = new String(fileNameBytes, "UTF-8"); // Read the file name using UTF-8 encoding for WinZip archives
        } else {
            fileName = new String(fileNameBytes, "Cp437"); // Read the file name using Cp437 encoding for other archives
        }
        archive.readFully(b);
        int extraFieldLen = ZipShort.getValue(b);
        int lenToSkip = extraFieldLen;
        while (lenToSkip > 0) {
            int skipped = archive.skipBytes(lenToSkip);
            if (skipped <= 0) {
                throw new RuntimeException("failed to skip extra field in local file header");
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
        } else {
            ze.setName(fileName);
        }
    }
}