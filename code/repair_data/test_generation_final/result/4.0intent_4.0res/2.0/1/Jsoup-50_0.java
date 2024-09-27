static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) {
    String docData;
    Document doc = null;

    if (byteData.remaining() >= 4) {
        byteData.mark();  // mark the position to reset to it later if needed
        int firstByte = byteData.get() & 0xFF;
        int secondByte = byteData.get() & 0xFF;
        int thirdByte = byteData.get() & 0xFF;
        int fourthByte = byteData.get() & 0xFF;

        if (firstByte == 0xFE && secondByte == 0xFF) {
            charsetName = "UTF-16BE"; // Big Endian
            byteData.reset().position(2); // Skip BOM
        } else if (firstByte == 0xFF && secondByte == 0xFE) {
            if (thirdByte == 0x00 && fourthByte == 0x00) {
                charsetName = "UTF-32LE"; // Little Endian
                byteData.reset().position(4); // Skip BOM
            } else {
                charsetName = "UTF-16LE"; // Little Endian
                byteData.reset().position(2); // Skip BOM
            }
        } else if (firstByte == 0x00 && secondByte == 0x00 && thirdByte == 0xFE && fourthByte == 0xFF) {
            charsetName = "UTF-32BE"; // Big Endian
            byteData.reset().position(4); // Skip BOM
        } else {
            byteData.reset(); // No BOM found, reset to start
        }
    }

    if (charsetName == null) { // determine from meta if charsetName still not set
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        doc = parser.parseInput(docData, baseUri);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset = null;
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
            }
            if (foundCharset == null && meta.hasAttr("charset")) {
                try {
                    if (Charset.isSupported(meta.attr("charset"))) {
                        foundCharset = meta.attr("charset");
                    }
                } catch (IllegalCharsetNameException e) {
                    foundCharset = null;
                }
            }

            if (foundCharset != null && foundCharset.length() != 0 && !foundCharset.equals(defaultCharset)) {
                foundCharset = foundCharset.trim().replaceAll("[\"']", "");
                charsetName = foundCharset;
                byteData.rewind();
                docData = Charset.forName(foundCharset).decode(byteData).toString();
                doc = null;
            }
        }
    } else {
        Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        docData = Charset.forName(charsetName).decode(byteData).toString();
    }

    if (doc == null) {
        doc = parser.parseInput(docData, baseUri);
        doc.outputSettings().charset(charsetName);
    }

    return doc;
}