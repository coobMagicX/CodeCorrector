public XmlDeclaration asXmlDeclaration() {
    String data = getData();
    String modifiedData = data.replace("<", "&lt;");
    Document doc = Jsoup.parse("<" + modifiedData.substring(1, modifiedData.length() - 1) + ">", baseUri(), Parser.xmlParser());
    XmlDeclaration decl = null;
    if (doc.childNodeSize() > 0) {
        Element el = doc.child(0);
        decl = new XmlDeclaration(NodeUtils.parser(doc).settings().normalizeTag(el.tagName()), data.startsWith("!"));
        decl.attributes().addAll(el.attributes());
    }
    return decl;
}