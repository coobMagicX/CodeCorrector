boolean process(Token t, TreeBuilder tb) {
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
                // merge attributes onto real html
                Element html = tb.getStack().getFirst();
                for (Attribute attribute : startTag.getAttributes()) {
                    if (!html.hasAttr(attribute.getKey()))
                        html.attributes().put(attribute);
                }
            } else if (StringUtil.in(name, "base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "title")) {
                // Handling data-containing tags including script and style
                tb.process(t, InHead);
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
                    // pop up to html element
                    while (stack.size() > 1)
                        stack.removeLast();
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                }
            } else if (StringUtil.in(name,
                    "address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl",
                    "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol",
                    "p", "section", "summary", "ul")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
            } else if (StringUtil.in(name, "h1", "h2", "h3", "h4", "h5", "h6")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                if (StringUtil.in(tb.currentElement().nodeName(), "h1", "h2", "h3", "h4", "h5", "h6")) {
                    tb.error(this);
                    tb.pop();
                }
                tb.insert(startTag);
            } else if (StringUtil.in(name, "pre", "listing")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
                tb.framesetOk(false);
            } else if (name.equals("form")) {
                if (tb.getFormElement() != null) {
                    tb.error(this);
                    return false;
                }
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                Element form = tb.insert(startTag);
                tb.setFormElement(form);
            } else {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            }
            break;

        case EndTag:
            Token.EndTag endTag = t.asEndTag();
            name = endTag.name();
            if (name.equals("body")) {
                if (!tb.inScope("body")) {
                    tb.error(this);
                    return false;
                } else {
                    tb.transition(AfterBody);
                }
            } else if (name.equals("html")) {
                boolean notIgnored = tb.process(new Token.EndTag("body"));
                if (notIgnored)
                    return tb.process(endTag);
            } else {
                tb.popStackToClose(name);
            }
            break;
        case EOF:
            // stop parsing
            break;
    }
    return true;
}