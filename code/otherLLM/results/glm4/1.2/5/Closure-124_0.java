private boolean isSafeReplacement(Node node, Node replacement) {
    // Check for simple names first to quickly determine if replacement is safe.
    if (node.isName()) {
        return true;
    }
    
    Preconditions.checkArgument(node.isGetProp(), "Node must be gettable property");

    while (node != null) { // Loop through the children until we find a name or reach the end of the node.
        if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
            return false; // The replacement conflicts with an existing name.
        }
        
        node = node.getNextSibling(); // Move to the next sibling.
    }

    // If no conflicting names were found, the replacement is safe.
    return true;
}