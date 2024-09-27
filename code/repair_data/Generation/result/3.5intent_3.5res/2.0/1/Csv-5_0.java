public void println() throws IOException {
    final String recordSeparator = format.getRecordSeparator();
    if (recordSeparator != null && !recordSeparator.endsWith("null")) {
        out.append(recordSeparator);
    }
    newRecord = true;
}