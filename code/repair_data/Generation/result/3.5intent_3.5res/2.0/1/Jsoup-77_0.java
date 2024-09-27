private void popStackToClose(Token.EndTag endTag) {
    String elName = endTag.name();
    Element firstFound = null;

    for (int pos = stack.size() - 1; pos >= 0; pos--) {
        Element next = stack.get(pos);
        if (next.nodeName().equals(elName)) {
            firstFound = next;
            break;
        }
    }
    if (firstFound == null)
        return; // not found, skip

    List<Element> toRemove = new ArrayList<>();

    for (int pos = stack.size() - 1; pos >= 0; pos--) {
        Element next = stack.get(pos);
        toRemove.add(next);
        if (next == firstFound)
            break;
    }

    stack.removeAll(toRemove);
}