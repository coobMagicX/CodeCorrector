public XmlDeclaration asXmlDeclaration() {
    String data = getData();
    // Check if the data is within a script tag and contains a pseudo XML declaration
    if (data.contains("<!") && baseUri().endsWith("/script")) {
        // Treat content as text, not as XML declaration
        return new XmlDeclaration(data, data.startsWith("!"));
    } else {
        Document doc = Jsoup.parse("<" + data.substring(1, data.length() -1) + ">", baseUri(), Parser.xmlParser());
        XmlDeclaration decl = null;
        if (doc.childNodeSize() > 0) {
            Element el = doc.child(0);
            decl = new XmlDeclaration(NodeUtils.parser(doc).settings().normalizeTag(el.tagName()), data.startsWith("!"));
            decl.attributes().addAll(el.attributes());
        }
        return decl;
    }
}