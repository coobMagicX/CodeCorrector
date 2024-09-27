boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
    String name = t.asEndTag().normalName();
    ArrayList<Element> stack = tb.getStack();
    for (int pos = stack.size() - 1; pos >= 0; pos--) {
        Element node = stack.get(pos);
        if (node.nodeName().equals(name)) {
            tb.generateImpliedEndTags(name);
            if (!name.equals(tb.currentElement().nodeName()))
                tb.error(this);
            tb.popStackToClose(name);
            break;
        } else {
            if (tb.isSpecial(node)) {
                tb.error(this);
                return false;
            }
            if (tb.isCaseSensitive()) {
                if (node.nodeName().equalsIgnoreCase(name)) { // Added additional check to handle case-insensitive comparison
                    tb.error(this);
                    return false;
                }
            }
        }
    }
    return true;
}