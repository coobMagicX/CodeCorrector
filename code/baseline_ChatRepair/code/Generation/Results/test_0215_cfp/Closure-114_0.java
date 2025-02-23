private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node valueNode = n.getLastChild();  
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);

    if (ns != null) {
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            if (parent.getFirstChild().getNext() != n) {
                recordDepScope(recordNode, ns);
            } else {
                recordDepScope(nameNode, ns);
            }
        } else {
            
            Node grandparent = parent.getParent();
            if (valueNode.isFunction() && grandparent != null && grandparent.isCall() && grandparent.getFirstChild() == parent) {
                
                recordDepScope(nameNode, ns);
            } else {
                
                recordDepScope(recordNode, ns);
            }
        }
    }
}
