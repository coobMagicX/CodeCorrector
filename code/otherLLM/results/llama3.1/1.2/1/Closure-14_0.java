public Node getNextNode(Node currentNode) {
    // Get the current node's type and child nodes
    JSType type = getJSType(currentNode);
    Node childNode = currentNode.getChildNode();

    // If the current node is a conditional statement or a loop, 
    // we need to find the next executable node
    if (currentNode.isConditional() || currentNode.isLoop()) {
        return getNextExecutableNode(childNode);
    }

    // For other types of nodes, simply return the first child node
    return childNode != null ? childNode : null;
}

private Node getNextExecutableNode(Node currentNode) {
    // Traverse the CFG to find the next executable node
    for (Node node = currentNode; node != null; node = node.getNextSibling()) {
        if (!node.isComment() && !node.isEmptyStatement()) {
            return node;
        }
    }

    // If no executable node is found, return null
    return null;
}