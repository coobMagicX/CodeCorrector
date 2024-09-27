void insert(Token.Comment commentToken) {
    Comment comment = new Comment(commentToken.getData());
    Node insert = comment;
    if (commentToken.bogus) {
        String data = comment.getData();
        if (data.length() > 1 && (data.startsWith("!") || data.startsWith("?"))) {
            String xmlDeclaration = data.substring(1, data.length() - 1);
            try {
                List<Node> parsedFragment = parseFragment("<root>" + xmlDeclaration + "</root>", baseUri, errors, settings);
                if (parsedFragment.size() > 0 && parsedFragment.get(0) instanceof Element) {
                    Element el = (Element) parsedFragment.get(0);
                    insert = new XmlDeclaration(settings.normalizeTag(el.tagName()), data.startsWith("!"));
                    insert.attributes().addAll(el.attributes());
                }
            } catch (Exception e) {
                // Handle the case where the XML declaration is not valid
                // For example, log an error or do some other handling
            }
        }
    }
    insertNode(insert);
}