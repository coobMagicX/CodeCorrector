void writePaxHeaders(String entryName, Map<String, String> headers) throws IOException {
    // Use the original entryName for creating the header to preserve non-ASCII characters.
    String name = "./PaxHeaders.X/" + entryName;
    // Check if the name exceeds the maximum length after converting to a safe format.
    if (name.getBytes(CharsetNames.UTF_8).length >= TarConstants.NAMELEN) {
        // Truncate based on byte length, not character count, to handle multi-byte characters.
        byte[] nameBytes = name.getBytes(CharsetNames.UTF_8);
        name = new String(nameBytes, 0, TarConstants.NAMELEN - 1, CharsetNames.UTF_8);
    }
    // Use LF_PAX_EXTENDED_HEADER_LC to indicate that this is a PAX header.
    TarArchiveEntry pex = new TarArchiveEntry(name, TarConstants.LF_PAX_EXTENDED_HEADER_LC);
    // Ensure that the entry is correctly flagged as a directory if the original entry name ended with a slash.
    if (entryName.endsWith("/")) {
        pex.setDirectory(true);
    }

    StringWriter w = new StringWriter();
    for (Map.Entry<String, String> h : headers.entrySet()) {
        String key = h.getKey();
        String value = h.getValue();
        String line = key + "=" + value + "\n";
        int len = line.getBytes(CharsetNames.UTF_8).length + 1; // +1 for the length digit itself
        line = len + " " + line;
        int actualLength = line.getBytes(CharsetNames.UTF_8).length;
        while (len != actualLength) {
            len = actualLength;
            line = len + " " + key + "=" + value + "\n";
            actualLength = line.getBytes(CharsetNames.UTF_8).length;
        }
        w.write(line);
    }
    byte[] data = w.toString().getBytes(CharsetNames.UTF_8);
    pex.setSize(data.length);
    putArchiveEntry(pex);
    write(data);
    closeArchiveEntry();
}