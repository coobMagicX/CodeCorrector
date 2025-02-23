public static Node reverse_linked_list(Node node) {
    Node prevnode = null;
    Node nextnode;
    while (node != null) {
        nextnode = node.getSuccessor();
        node.setSuccessor(prevnode);
        prevnode = node; // Update the prevnode to the current node
        node = nextnode;
    }
    return prevnode;
}