private Map<String, Integer> initializeHeader() throws IOException {
    Map<String, Integer> hdrMap = null;
    final String[] formatHeader = this.format.getHeader();
    if (formatHeader != null) {
        hdrMap = new LinkedHashMap<String, Integer>();

        String[] headerRecord = null;
        if (formatHeader.length == 0) {
            // read the header from the first line of the file
            final CSVRecord nextRecord = this.nextRecord();
            if (nextRecord != null) {
                headerRecord = nextRecord.values();
            }
        } else {
            if (this.format.getSkipHeaderRecord()) {
                this.nextRecord();
            }
            headerRecord = formatHeader;
        }

        // build the name to index mappings
        if (headerRecord != null) {
            for (int i = 0; i < headerRecord.length; i++) {
                final String header = headerRecord[i];
                final boolean containsHeader = hdrMap.containsKey(header);
                final boolean emptyHeader = header == null || header.trim().isEmpty();
                // Check if header is either null or empty and ignoreEmptyHeaders is true, then do not add it to the map
                if (!emptyHeader || !this.format.getIgnoreEmptyHeaders()) {
                    if (containsHeader) {
                        throw new IllegalArgumentException("The header contains a duplicate name: \"" + header +
                                "\" in " + Arrays.toString(headerRecord));
                    }
                    if (!emptyHeader) {
                        hdrMap.put(header, Integer.valueOf(i));
                    }
                }
            }
        }
    }
    return hdrMap;
}