void insert(Token.Comment commentToken) {
    Comment comment = new Comment(commentToken.getData());
    Node insert = comment;
    if (commentToken.bogus) { // xml declarations are emitted as bogus comments (which is right for html, but not xml)
        String data = comment.getData();
        // Ensure the handling of XML declaration starts correctly and handle incomplete declarations
        if (data.length() > 1 && (data.startsWith("!") || data.startsWith("?xml"))) {
            if (!data.endsWith(">")) {
                data = data + ">";
            }
            // Parse the fixed data as an element to extract attributes and ensure it handles XML declaration specifically
            Document doc = Jsoup.parse("<" + data.substring(1) + ">", baseUri, Parser.xmlParser());
            if (!doc.children().isEmpty()) {
                Element el = doc.child(0);
                // Check if the parsed element is actually an XML declaration
                if (el.tagName().equalsIgnoreCase("xml")) {
                    insert = new XmlDeclaration("xml", data.startsWith("!"));
                    insert.attributes().addAll(el.attributes());
                }
            }
        }
    }
    insertNode(insert);
}