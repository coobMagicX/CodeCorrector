private Node tryFoldArrayAccess(Node n, Node left, Node right) {
    Node parent = n.getParent();

    // If GETPROP/GETELEM is used as assignment target the array literal is
    // acting as a temporary we can't fold it here:
    //    "[][0] += 1"
    if (isAssignmentTarget(n)) {
        return n;
    }

    if (!right.isNumber()) {
        // Sometimes people like to use complex expressions to index into
        // arrays, or strings to index into array methods.
        return n;
    }

    double index = right.getDouble();
    int intIndex = (int) Math.round(index); // Use Math.round to round the double value

    if (intIndex != index && intIndex < 0 || index < Integer.MIN_VALUE || index > Integer.MAX_VALUE) {
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
    }

    if (intIndex < 0) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }

    Node current = left.getFirstChild();
    Node elem = null;
    for (int i = 0; current != null && i <= intIndex; i++) { // Correct the condition to include intIndex
        if (i == intIndex) {
            elem = current;
            break;
        }
        current = current.getNext();
    }

    if (elem == null) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }

    if (elem.isEmpty()) {
        elem = NodeUtil.newUndefinedNode(elem); // This line can remain unchanged
    } else {
        left.removeChild(elem);
    }

    // Replace the entire GETELEM with the value
    n.getParent().replaceChild(n, elem);
    reportCodeChange();
    return elem;
}