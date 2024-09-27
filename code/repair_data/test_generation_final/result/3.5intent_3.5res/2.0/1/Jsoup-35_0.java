boolean process(Token t, HtmlTreeBuilder tb) {
    switch (t.type) {
        case Character: {
            Token.Character c = t.asCharacter();
            if (c.getData().equals(nullString)) {
                // todo confirm that check
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
                return tb.process(t, InHead);
            } else if (name.equals("body")) {
                tb.error(this);
                LinkedList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    // only in fragment case
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
                    // only in fragment case
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
                // todo: ignore LF if next token
                tb.framesetOk(false);
            } else if (name.equals("form")) {
                if (tb.getFormElement() != null) {
                    tb.error(this);
                    return false;
                }
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insertForm(startTag, true);
            } else if (name.equals("li")) {
                tb.framesetOk(false);
                LinkedList<Element> stack = tb.getStack();
                for (int i = stack.size() - 1; i > 0; i--) {
                    Element el = stack.get(i);
                    if (el.nodeName().equals("li")) {
                        tb.process(new Token.EndTag("li"));
                        break;
                    }
                    if (tb.isSpecial(el) && !StringUtil.in(el.nodeName(), "address", "div", "p"))
                        break;
                }
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
            } else if (StringUtil.in(name, "dd", "dt")) {
                tb.framesetOk(false);
                LinkedList<Element> stack = tb.getStack();
                for (int i = stack.size() - 1; i > 0; i--) {
                    Element el = stack.get(i);
                    if (StringUtil.in(el.nodeName(), "dd", "dt")) {
                        tb.process(new Token.EndTag(el.nodeName()));
                        break;
                    }
                    if (tb.isSpecial(el) && !StringUtil.in(el.nodeName(), "address", "div", "p"))
                        break;
                }
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
            } else if (name.equals("plaintext")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
                tb.tokeniser.transition(TokeniserState.PLAINTEXT); // once in, never gets out
            } else if (name.equals("button")) {
                if (tb.inButtonScope("button")) {
                    // close and reprocess
                    tb.error(this);
                    tb.process(new Token.EndTag("button"));
                    tb.process(startTag);
                } else {
                    tb.reconstructFormattingElements();
                    tb.insert(startTag);
                    tb.framesetOk(false);
                }
            } else if (name.equals("a")) {
                if (tb.getActiveFormattingElement("a") != null) {
                    tb.error(this);
                    tb.process(new Token.EndTag("a"));

                    // still on stack?
                    Element remainingA = tb.getFromStack("a");
                    if (remainingA != null) {
                        tb.removeFromActiveFormattingElements(remainingA);
                        tb.removeFromStack(remainingA);
                    }
                }
                tb.reconstructFormattingElements();
                Element a = tb.insert(startTag);
                tb.pushActiveFormattingElements(a);
            } else if (StringUtil.in(name,
                    "b", "big", "code", "em", "font", "i", "s", "small", "strike", "strong", "tt", "u")) {
                tb.reconstructFormattingElements();
                Element el = tb.insert(startTag);
                tb.pushActiveFormattingElements(el);
            } else if (name.equals("nobr")) {
                tb.reconstructFormattingElements();
                if (tb.inScope("nobr")) {
                    tb.error(this);
                    tb.process(new Token.EndTag("nobr"));
                    tb.reconstructFormattingElements();
                }
                Element el = tb.insert(startTag);
                tb.pushActiveFormattingElements(el);
            } else if (StringUtil.in(name, "applet", "marquee", "object")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
                tb.insertMarkerToFormattingElements();
                tb.framesetOk(false);
            } else if (name.equals("table")) {
                if (tb.getDocument().quirksMode() != Document.QuirksMode.quirks && tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insert(startTag);
                tb.framesetOk(false);
                tb.transition(InTable);
            } else if (StringUtil.in(name, "area", "br", "embed", "img", "keygen", "wbr")) {
                tb.reconstructFormattingElements();
                tb.insertEmpty(startTag);
                tb.framesetOk(false);
            } else if (name.equals("input")) {
                tb.reconstructFormattingElements();
                Element el = tb.insertEmpty(startTag);
                if (!el.attr("type").equalsIgnoreCase("hidden"))
                    tb.framesetOk(false);
            } else if (StringUtil.in(name, "param", "source", "track")) {
                tb.insertEmpty(startTag);
            } else if (name.equals("hr")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.insertEmpty(startTag);
                tb.framesetOk(false);
            } else if (name.equals("image")) {
                // we're not supposed to ask.
                startTag.name("img");
                return tb.process(startTag);
            } else if (name.equals("isindex")) {
                // how much do we care about the early 90s?
                tb.error(this);
                if (tb.getFormElement() != null)
                    return false;

                tb.tokeniser.acknowledgeSelfClosingFlag();
                tb.process(new Token.StartTag("form"));
                if (startTag.attributes.hasKey("action")) {
                    Element form = tb.getFormElement();
                    form.attr("action", startTag.attributes.get("action"));
                }
                tb.process(new Token.StartTag("hr"));
                tb.process(new Token.StartTag("label"));
                // hope you like english.
                String prompt = startTag.attributes.hasKey("prompt") ?
                        startTag.attributes.get("prompt") :
                        "This is a searchable index. Enter search keywords: ";

                tb.process(new Token.Character(prompt));

                // input
                Attributes inputAttribs = new Attributes();
                for (Attribute attr : startTag.attributes) {
                    if (!StringUtil.in(attr.getKey(), "name", "action", "prompt"))
                        inputAttribs.put(attr);
                }
                inputAttribs.put("name", "isindex");
                tb.process(new Token.StartTag("input", inputAttribs));
                tb.process(new Token.EndTag("label"));
                tb.process(new Token.StartTag("hr"));
                tb.process(new Token.EndTag("form"));
            } else if (name.equals("textarea")) {
                tb.insert(startTag);
                // todo: If the next token is a U+000A LINE FEED (LF) character token, then ignore that token and move on to the next one. (Newlines at the start of textarea elements are ignored as an authoring convenience.)
                tb.tokeniser.transition(TokeniserState.Rcdata);
                tb.markInsertionMode();
                tb.framesetOk(false);
                tb.transition(Text);
            } else if (name.equals("xmp")) {
                if (tb.inButtonScope("p")) {
                    tb.process(new Token.EndTag("p"));
                }
                tb.reconstructFormattingElements();
                tb.framesetOk(false);
                handleRawtext(startTag, tb);
            } else if (name.equals("iframe")) {
                tb.framesetOk(false);
                handleRawtext(startTag, tb);
            } else if (name.equals("noembed")) {
                // also handle noscript if script enabled
                handleRawtext(startTag, tb);
            } else if (name.equals("select")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
                tb.framesetOk(false);

                HtmlTreeBuilderState state = tb.state();
                if (state.equals(InTable) || state.equals(InCaption) || state.equals(InTableBody) || state.equals(InRow) || state.equals(InCell))
                    tb.transition(InSelectInTable);
                else
                    tb.transition(InSelect);
            } else if (StringUtil.in("optgroup", "option")) {
                if (tb.currentElement().nodeName().equals("option"))
                    tb.process(new Token.EndTag("option"));
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (StringUtil.in("rp", "rt")) {
                if (tb.inScope("ruby")) {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals("ruby")) {
                        tb.error(this);
                        tb.popStackToBefore("ruby"); // i.e. close up to but not include name
                    }
                    tb.insert(startTag);
                }
            } else if (name.equals("math")) {
                tb.reconstructFormattingElements();
                // todo: handle A start tag whose tag name is "math" (i.e. foreign, mathml)
                tb.insert(startTag);
                tb.tokeniser.acknowledgeSelfClosingFlag();
            } else if (name.equals("svg")) {
                tb.reconstructFormattingElements();
                // todo: handle A start tag whose tag name is "svg" (xlink, svg)
                tb.insert(startTag);
                tb.tokeniser.acknowledgeSelfClosingFlag();
            } else if (StringUtil.in(name,
                    "caption", "col", "colgroup", "frame", "head", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                tb.error(this);
                return false;
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
                    // todo: error if stack contains something not dd, dt, li, optgroup, option, p, rp, rt, tbody, td, tfoot, th, thead, tr, body, html
                    tb.transition(AfterBody);
                }
            } else if (name.equals("html")) {
                boolean notIgnored = tb.process(new Token.EndTag("body"));
                if (notIgnored)
                    return tb.process(endTag);
            } else if (StringUtil.in(name,
                    "address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div",
                    "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu",
                    "nav", "ol", "pre", "section", "summary", "ul")) {
                // todo: refactor these lookups
                if (!tb.inScope(name)) {
                    // nothing to close
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (name.equals("form")) {
                Element currentForm = tb.getFormElement();
                tb.setFormElement(null);
                if (currentForm == null || !tb.inScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    // remove currentForm from stack. will shift anything under up.
                    tb.removeFromStack(currentForm);
                }
            } else if (name.equals("p")) {
                if (!tb.inButtonScope(name)) {
                    tb.error(this);
                    tb.process(new Token.StartTag(name)); // if no p to close, creates an empty <p></p>
                    return tb.process(endTag);
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (name.equals("li")) {
                if (!tb.inListItemScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (StringUtil.in(name, "dd", "dt")) {
                if (!tb.inScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (StringUtil.in(name, "h1", "h2", "h3", "h4", "h5", "h6")) {
                if (!tb.inScope(new String[]{"h1", "h2", "h3", "h4", "h5", "h6"})) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose("h1", "h2", "h3", "h4", "h5", "h6");
                }
            } else if (name.equals("sarcasm")) {
                // *sigh*
                return anyOtherEndTag(t, tb);
            } else if (StringUtil.in(name,
                    "a", "b", "big", "code", "em", "font", "i", "nobr", "s", "small", "strike", "strong", "tt", "u")) {
                // Adoption Agency Algorithm.
                OUTER:
                for (int i = 0; i < 8; i++) {
                    Element formatEl = tb.getActiveFormattingElement(name);
                    if (formatEl == null)
                        return anyOtherEndTag(t, tb);
                    else if (!tb.onStack(formatEl)) {
                        tb.error(this);
                        tb.removeFromActiveFormattingElements(formatEl);
                        return true;
                    } else if (!tb.inScope(formatEl.nodeName())) {
                        tb.error(this);
                        return false;
                    } else if (tb.currentElement() != formatEl)
                        tb.error(this);

                    Element furthestBlock = null;
                    Element commonAncestor = null;
                    boolean seenFormattingElement = false;
                    LinkedList<Element> stack = tb.getStack();
                    // the spec doesn't limit to < 64, but in degenerate cases (9000+ stack depth) this prevents
                    // run-aways
                    for (int si = 0; si < stack.size() && si < 64; si++) {
                        Element el = stack.get(si);
                        if (el == formatEl) {
                            commonAncestor = stack.get(si - 1);
                            seenFormattingElement = true;
                        } else if (seenFormattingElement && tb.isSpecial(el)) {
                            furthestBlock = el;
                            break;
                        }
                    }
                    if (furthestBlock == null) {
                        tb.popStackToClose(formatEl.nodeName());
                        tb.removeFromActiveFormattingElements(formatEl);
                        return true;
                    }

                    // todo: Let a bookmark note the position of the formatting element in the list of active formatting elements relative to the elements on either side of it in the list.
                    // does that mean: int pos of format el in list?
                    Element node = furthestBlock;
                    Element lastNode = furthestBlock;
                    INNER:
                    for (int j = 0; j < 3; j++) {
                        if (tb.onStack(node))
                            node = tb.aboveOnStack(node);
                        if (!tb.isInActiveFormattingElements(node)) { // note no bookmark check
                            tb.removeFromStack(node);
                            continue INNER;
                        } else if (node == formatEl)
                            break INNER;

                        Element replacement = new Element(Tag.valueOf(node.nodeName()), tb.getBaseUri());
                        tb.replaceActiveFormattingElement(node, replacement);
                        tb.replaceOnStack(node, replacement);
                        node = replacement;

                        if (lastNode == furthestBlock) {
                            // todo: move the aforementioned bookmark to be immediately after the new node in the list of active formatting elements.
                            // not getting how this bookmark both straddles the element above, but is inbetween here...
                        }
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        node.appendChild(lastNode);

                        lastNode = node;
                    }

                    if (StringUtil.in(commonAncestor.nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        tb.insertInFosterParent(lastNode);
                    } else {
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        commonAncestor.appendChild(lastNode);
                    }

                    Element adopter = new Element(formatEl.tag(), tb.getBaseUri());
                    Node[] childNodes = furthestBlock.childNodes().toArray(new Node[furthestBlock.childNodeSize()]);
                    for (Node childNode : childNodes) {
                        adopter.appendChild(childNode); // append will reparent. thus the clone to avoid concurrent mod.
                    }
                    furthestBlock.appendChild(adopter);
                    tb.removeFromActiveFormattingElements(formatEl);
                    // todo: insert the new element into the list of active formatting elements at the position of the aforementioned bookmark.
                    tb.removeFromStack(formatEl);
                    tb.insertOnStackAfter(furthestBlock, adopter);
                }
            } else if (StringUtil.in(name, "applet", "marquee", "object")) {
                if (!tb.inScope("name")) {
                    if (!tb.inScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                    tb.clearFormattingElementsToLastMarker();
                }
            } else if (name.equals("br")) {
                tb.error(this);
                tb.process(new Token.StartTag("br"));
                return false;
            } else {
                return anyOtherEndTag(t, tb);
            }

            break;
        case EOF:
            // todo: error if stack contains something not dd, dt, li, p, tbody, td, tfoot, th, thead, tr, body, html
            // stop parsing
            break;
    }
    return true;
}

private boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
    // TODO: Add the logic to handle any other end tag
    // ...
    return true;
}