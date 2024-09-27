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
    } else if (StringUtil.in(name, Constants.InBodyStartToHead)) {
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
    } else if (StringUtil.in(name, Constants.InBodyStartPClosers)) {
        if (tb.inButtonScope("p")) {
            tb.process(new Token.EndTag("p"));
        }
        tb.insert(startTag);
    } else if (StringUtil.in(name, Constants.Headings)) {
        if (tb.inButtonScope("p")) {
            tb.process(new Token.EndTag("p"));
        }
        if (StringUtil.in(tb.currentElement().nodeName(), Constants.Headings)) {
            tb.error(this);
            tb.pop();
        }
        tb.insert(startTag);
    } else if (StringUtil.in(name, Constants.InBodyStartPreListing)) {
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
            if (tb.isSpecial(el) && !StringUtil.in(el.nodeName(), Constants.InBodyStartLiBreakers))
                break;
        }
        if (tb.inButtonScope("p")) {
            tb.process(new Token.EndTag("p"));
        }
        tb.insert(startTag);
    } else if (StringUtil.in(name, Constants.DdDt)) {
        tb.framesetOk(false);
        LinkedList<Element> stack = tb.getStack();
        for (int i = stack.size() - 1; i > 0; i--) {
            Element el = stack.get(i);
            if (StringUtil.in(el.nodeName(), Constants.DdDt)) {
                tb.process(new Token.EndTag(el.nodeName()));
                break;
            }
            if (tb.isSpecial(el) && !StringUtil.in(el.nodeName(), Constants.InBodyStartLiBreakers))
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
    } else if (StringUtil.in(name, Constants.Formatters)) {
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
    } else if (StringUtil.in(name, Constants.InBodyStartApplets)) {
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
    } else if (StringUtil.in(name, Constants.InBodyStartEmptyFormatters)) {
        tb.reconstructFormattingElements();
        tb.insertEmpty(startTag);
        tb.framesetOk(false);
    } else if (name.equals("input")) {
        tb.reconstructFormattingElements();
        Element el = tb.insertEmpty(startTag);
        if (!el.attr("type").equalsIgnoreCase("hidden"))
            tb.framesetOk(false);
    } else if (StringUtil.in(name, Constants.InBodyStartMedia)) {
        tb.insertEmpty(startTag);
    } else if (name.equals("hr")) {
        if (tb.inButtonScope("p")) {
            tb.process(new Token.EndTag("p"));
        }
        tb.insertEmpty(startTag);
        tb.framesetOk(false);
    } else if (name.equals("image")) {
        if (tb.inScope("svg")) {
            tb.insert(startTag);
        } else {
            return tb.process(startTag.name("img")); // change <image> to <img>, unless in svg
        }
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
            if (!StringUtil.in(attr.getKey(), Constants.InBodyStartInputAttribs))
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
    } else if (StringUtil.in(name, Constants.InBodyStartOptions)) {
        if (tb.currentElement().nodeName().equals("option"))
            tb.process(new Token.EndTag("option"));
        tb.reconstructFormattingElements();
        tb.insert(startTag);
    } else if (StringUtil.in(name, Constants.InBodyStartRuby)) {
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
    } else if (StringUtil.in(name, Constants.InBodyStartDrop)) {
        tb.error(this);
        return false;
    } else {
        tb.reconstructFormattingElements();
        tb.insert(startTag);
    }
    break;