Element insert(Token.StartTag startTag) {
    // Known HTML tags that should not be self-closing
    Set<String> nonSelfClosingTags = new HashSet<>(Arrays.asList("div", "span", "p", "ul", "ol", "li", "table", "tr", "td", "th", "thead", "tbody", "tfoot", "script", "style"));

    if (startTag.isSelfClosing()) {
        // Check if the tag is known and typically not self-closing
        if (nonSelfClosingTags.contains(startTag.name())) {
            // Create as normal element with separate opening and closing tags
            Element el = new Element(Tag.valueOf(startTag.name()), baseUri, startTag.attributes);
            insert(el); // Insert the open tag
            stack.add(el);
            // Manually emit and handle the corresponding end tag for proper closure
            tokeniser.emit(new Token.EndTag(startTag.name()));
            process(new Token.EndTag(startTag.name())); // Simulate processing of the end tag
            return el;
        } else {
            // Handle empty unknown tags or tags that are typically self-closing
            Element el = insertEmpty(startTag);
            stack.add(el);
            // No need to emit an end tag since it's already handled by insertEmpty
            return el;
        }
    }

    // Handle normal start tags that are not self-closing
    Element el = new Element(Tag.valueOf(startTag.name()), baseUri, startTag.attributes);
    insert(el);
    return el;
}