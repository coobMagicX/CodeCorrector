// Now that we are done with the special cases, follow should be its
// immediate sibling, unless its sibling is a function.
Node nextSibling = node.getNext();

// Skip over function declarations because control does not directly pass into them,
// but ensure a return path is defined if it's function's last sibling.
while (nextSibling != null) {
    if (nextSibling.isFunction()) {
        // If it's the last sibling and a function, simulate a return from this function
        if (nextSibling.getNext() == null) {
            // You should define your logic here how you receive a return node for your control flow
            // For example, invoking a method that gives a symbolic return node related to this function
            return getSymbolicReturnNodeFromFunction(nextSibling);
        }
        // Else, just move to next sibling
        nextSibling = nextSibling.getNext();
    } else {
        break;
    }
}

if (nextSibling != null) {
    return computeFallThrough(nextSibling);
} else {
    // If there are no more siblings, control is transferred up the AST.
    return computeFollowNode(fromNode, parent, cfa);
}
