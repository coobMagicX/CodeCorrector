Element insert(Token.StartTag startTag) {
    // handle empty unknown tags
    // when the spec expects an empty tag, will directly hit insertEmpty, so won't generate this fake end tag.
    if (startTag.isSelfClosing()) {
        Element el = insertEmpty(startTag);
        stack.add(el);
        // emit a corresponding end tag for self-closing tags
        tokeniser.emit(new Token.EndTag(el.tagName()));
        return el;
    }
    
    Element el = new Element(Tag.valueOf(startTag.name()), baseUri, startTag.attributes);
    
    // check if the tag is a known empty block tag
    if (el.tag().isKnownEmptyBlockTag()) {
        stack.add(el);
        // emit a corresponding end tag for known empty block tags
        tokeniser.emit(new Token.EndTag(el.tagName()));
    } else {
        insert(el);
    }
    
    return el;
}