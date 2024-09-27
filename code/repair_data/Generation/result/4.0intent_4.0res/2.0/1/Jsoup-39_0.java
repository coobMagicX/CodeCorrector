static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) {
    String docData;
    Document doc = null;
    if (charsetName == null) { // determine from meta. safe parse as UTF-8
        // Decode initially as UTF-8 to handle potential BOM
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        // Check and handle UTF-8 BOM if present
        if (docData.startsWith("\uFEFF")) {
            docData = docData.substring(1); // Remove BOM
        }
        doc = parser.parseInput(docData, baseUri);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) { // if not found, will keep utf-8 as best attempt
            String foundCharset;
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
                if (foundCharset == null && meta.hasAttr("charset")) {
                    try {
                        if (Charset.isSupported(meta.attr("charset"))) {
                            foundCharset = meta.attr("charset");
                        }
                    } catch (IllegalCharsetNameException e) {
                        foundCharset = null;
                    }
                }
            } else {
                foundCharset = meta.attr("charset");
            }

            if (foundCharset != null && foundCharset.length() != 0 && !foundCharset.equals(defaultCharset)) { // need to re-decode
                foundCharset = foundCharset.trim().replaceAll("[\"']", "");
                charsetName = foundCharset;
                byteData.rewind();
                docData = Charset.forName(foundCharset).decode(byteData).toString();
                if (docData.startsWith("\uFEFF")) {
                    docData = docData.substring(1); // Remove BOM after re-decoding
                }
                doc = null;
            }
        }
    } else { // specified by content type header (or by user on file load)
        Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        docData = Charset.forName(charsetName).decode(byteData).toString();
        // Check and handle UTF-8 BOM if present
        if (docData.startsWith("\uFEFF")) {
            docData = docData.substring(1); // Remove BOM
        }
    }
    if (doc == null) {
        doc = parser.parseInput(docData, baseUri);
        doc.outputSettings().charset(charsetName);
    }
    return doc;
}