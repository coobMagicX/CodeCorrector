private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();
    if (right != null && !NodeUtil.isImmutableValue(right)) {
        return n; // If the join argument isn't immutable, don't try to fold it.
    }

    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();

    if (arrayNode == null || functionName == null || arrayNode.getType() != Token.ARRAYLIT || 
        !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right); // Default join string is ","
    StringBuilder sb = new StringBuilder();

    // Pulling all immutable values or empty slots in the array into one joined string and leaving other
    // values (like function calls or variables) intact.
    LinkedList<Node> parts = new LinkedList<>();
    boolean hasNonString = false;
    int foldedSize = 0;

    for (Node child = arrayNode.getFirstChild(); child != null; child = child.getNext()) {
        if (NodeUtil.isImmutableValue(child) && child.getType() != Token.OBJECTLIT) {
            if (child.getType() == Token.EMPTY) {
                sb.append("");
            } else {
                if (sb.length() > 0 && !parts.isEmpty() && NodeUtil.isImmutableValue(parts.getLast())) {
                    sb.append(joinString);
                }
                sb.append(NodeUtil.getArrayElementStringValue(child));
            }
        } else {
            if (sb.length() > 0) {
                // Add collected strings to parts
                parts.add(Node.newString(sb.toString()).setJSDocInfo(child.getJSDocInfo()));
                foldedSize += sb.length();
                sb = new StringBuilder();
            }
            parts.add(child);
            hasNonString = true;
            foldedSize += InlineCostEstimator.getCost(child);
        }
    }

    // If anything was added to StringBuilder after last non-string
    if (sb.length() > 0) {
        parts.add(Node.newString(sb.toString()).setJSDocInfo(n.getJSDocInfo()));
        foldedSize += sb.length();
    }

    // Decide if folding is beneficial
    if (hasNonString || parts.size() > 1) { // Complex scenario: Folding not beneficial or not possible
        if (foldedSize >= InlineCostEstimator.getCost(n)) {
            return n;
        }
    } else if (parts.size() == 1) {
        // Simple scenario: All parts are foldable into one string, replace node
        Node singlePart = parts.getFirst();
        n.getParent().replaceChild(n, singlePart);
        reportCodeChange();
        return singlePart;
    }

    reportCodeChange();
    return n;
}
