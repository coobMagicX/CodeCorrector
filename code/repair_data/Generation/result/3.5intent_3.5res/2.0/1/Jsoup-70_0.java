static boolean preserveWhitespace(Node node) {
    // looks only at this element and five levels up, to prevent recursion & needless stack searches
    if (node != null && node instanceof Element) {
        Element el = (Element) node;
        for (int i = 0; i < 6; i++) {
            if (el.tag.preserveWhitespace())
                return true;
            el = el.parent();
            if (el == null)
                break;
        }
    }
    return false;
}