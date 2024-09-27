public void println() throws IOException {
    final String recordSeparator = format.getRecordSeparator();
    if (recordSeparator != null) {
        out.append(recordSeparator);
    } else {
        // Handle the case of a null record separator
        // For example, you can use a default record separator or throw an exception
        out.append(System.lineSeparator());
    }
    newRecord = true;
}