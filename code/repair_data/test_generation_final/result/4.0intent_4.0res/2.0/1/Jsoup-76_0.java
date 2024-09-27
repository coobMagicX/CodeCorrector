boolean process(Token t, HtmlTreeBuilder tb) {
    switch (t.type) {
        case Character: {
            Token.Character c = t.asCharacter();
            if (c.getData().equals(nullString)) {
                tb.error(this);
                return false;
            } else if (tb.framesetOk() && isWhitespace(c)) {
                tb.reconstructFormattingElements();
                tb.insert(c);
            } else {
                tb.reconstructFormattingElements();
                // Ensure the very first newline in <pre> is skipped
                if (tb.currentElement().tagName().equals("pre") && c.getData().equals("\n") && !tb.hasSeenPreNewline()) {
                    tb.setSeenPreNewline(true); // Mark that we've skipped the newline
                } else {
                    tb.insert(c);
                }
                tb.framesetOk(false);
            }
            break;
        }
        case Comment: {
            tb.insert(t.asComment());
            break;
        }
        case Doctype: {
            tb.error(this);
            return false;
        }
        case StartTag:
            Token.StartTag startTag = t.asStartTag();
            String name = startTag.normalName();
            if (name.equals("pre")) {
                tb.insert(startTag);
                tb.framesetOk(false);
                tb.setSeenPreNewline(false); // Reset flag for new <pre> block
            } else if (StringUtil.inSorted(name, Constants.InBodyStartPClosers)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else {
                // Handling other tags remains unchanged
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            }
            break;
        case EndTag:
            Token.EndTag endTag = t.asEndTag();
            name = endTag.normalName();
            if (name.equals("pre")) {
                tb.popStackToClose("pre");
                tb.framesetOk(true);
            } else {
                // Handling other end tags remains unchanged
                return anyOtherEndTag(t, tb);
            }
            break;
        case EOF:
            // Handling EOF remains unchanged
            break;
    }
    return true;
}