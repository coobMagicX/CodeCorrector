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
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
    }

    double index = right.getDouble();
    int intIndex = (int) Math.round(index); // Use Math.round for proper rounding of floating-point numbers

    if (Math.abs(intIndex - index) > 0.0001) { // Check if the value is too close to an integer to be valid
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
    }

    if (intIndex < 0) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }

    Node current = left.getFirstChild();
    Node elem = null;
    for (int i = 0; current != null && i < intIndex; i++) {
        elem = current;

        // If the element is empty or not an array, we can't fold it
        if (elem.isEmpty() || !elem.getType().equals(Node.ARRAY)) {
            error(INDEX_OUT_OF_BOUNDS_ERROR, right);
            return n;
        }

        current = current.getNext();
    }

    if (current == null) { // If the loop exits without finding an element at the index
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }

    if (elem.getType().equals(Node.UNDEFINED)) {
        elem = NodeUtil.newUndefinedNode(elem); // Create a new undefined node only for undefined elements
    } else {
        left.removeChild(elem); // Remove the element from the original array
    }

    // Replace the entire GETELEM with the value
    n.getParent().replaceChild(n, elem);
    reportCodeChange();
    return elem;
}