private Node tryFoldArrayAccess(Node n, Node left, Node right) {
    Node parent = n.getParent();
    // If GETPROP/GETELEM is used as assignment target the array literal is
    // acting as a temporary we can't fold it here:
    //    "[][0] += 1"
    if (isAssignmentTarget(n)) {
        return n;
    }

    if (!right.isNumber()) {
        // Sometimes people like to use complex expressions to index into arrays, or strings to index into array methods.
        error("Non-numeric index value encountered", right);
        return n;
    }

    double index = right.getDouble();
    int intIndex = (int) index;
    if (intIndex != index) {
        error("Index is not an integer value", right);
        return n;
    }

    if (intIndex < 0) {
        error("Array index out of bounds", right);
        return n;
    }

    Node current = left.getFirstChild();
    Node elem = null;
    for (int i = 0; current != null && i < intIndex; i++) {
        current = current.getNext(); // Move to the next element before checking
    }

    if (current == null) {
        error("Array index out of bounds", right);
        return n;
    }

    elem = current;
    if (elem.isEmpty()) {
        elem = NodeUtil.newUndefinedNode(elem);
    } else {
        left.removeChild(elem); // Remove the element from its parent to replace it
    }

    // Replace the entire GETELEM with the value
    n.getParent().replaceChild(n, elem);
    reportCodeChange();
    return elem;
}