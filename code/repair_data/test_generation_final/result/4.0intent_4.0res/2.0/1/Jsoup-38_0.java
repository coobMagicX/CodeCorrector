boolean process(Token t, HtmlTreeBuilder tb) {
    switch (t.type) {
        case Character: {
            Token.Character c = t.asCharacter();
            if (c.getData().equals(nullString)) {
                tb.error(this);
                return false;
            } else if (isWhitespace(c)) {
                tb.reconstructFormattingElements();
                tb.insert(c);
            } else {
                tb.reconstructFormattingElements();
                tb.insert(c);
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
            String name = startTag.name();
            if (name.equals("html")) {
                tb.error(this);
                Element html = tb.getStack().getFirst();
                for (Attribute attribute : startTag.getAttributes()) {
                    if (!html.hasAttr(attribute.getKey()))
                        html.attributes().put(attribute);
                }
            } else if (StringUtil.in(name, Constants.InBodyStartToHead)) {
                return tb.process(t, InHead);
            } else if (name.equals("body")) {
                tb.error(this);
                LinkedList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false; // ignore
                } else {
                    tb.framesetOk(false);
                    Element body = stack.get(1);
                    for (Attribute attribute : startTag.getAttributes()) {
                        if (!body.hasAttr(attribute.getKey()))
                            body.attributes().put(attribute);
                    }
                }
            } else if (name.equals("frameset")) {
                tb.error(this);
                LinkedList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false; // ignore
                } else if (!tb.framesetOk()) {
                    return false; // ignore frameset
                } else {
                    Element second = stack.get(1);
                    if (second.parent() != null)
                        second.remove();
                    while (stack.size() > 1)
                        stack.removeLast();
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                }
            } else if (StringUtil.in(name, Constants.InBodyStartPClosers)) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
            } else if (name.equals("image")) {
                if (tb.currentElement().nodeName().equals("svg")) {
                    tb.insert(startTag); // Keep as <image> within SVG context
                } else {
                    tb.error(this); // Log an error for incorrect tag usage outside SVG
                    return tb.process(new Token.StartTag("img")); // Change <image> to <img> outside SVG
                }
            } else {
                // Handle other tags as per existing logic, not shown for brevity
            }
            break;

        case EndTag:
            Token.EndTag endTag = t.asEndTag();
            name = endTag.name();
            // Handle end tags as per existing logic, not shown for brevity
            break;

        case EOF:
            // Handle EOF as per existing logic, not shown for brevity
            break;
    }
    return true;
}