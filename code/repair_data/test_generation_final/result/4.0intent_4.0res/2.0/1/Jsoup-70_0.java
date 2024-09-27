static boolean preserveWhitespace(Node node) {
    if (node != null && node instanceof Element) {
        Element el = (Element) node;
        Elements parents = new Elements();
        accumulateParents(el, parents); // Retrieve all ancestors
        parents.add(el); // Include the current element as well for checking
        for (Element element : parents) {
            if (element.tag().preserveWhitespace()) {
                return true; // If any element including the current one or its ancestors preserves whitespace, return true
            }
        }
    }
    return false; // No preserving element found, return false
}