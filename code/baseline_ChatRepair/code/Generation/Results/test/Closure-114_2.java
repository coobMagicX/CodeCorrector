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
      
      
      if (valueNode.isFunction() && parent.isCall() && parent.getFirstChild() == n) {
        
        
        recordDepScope(nameNode, ns);
      } else {
        
        recordDepScope(recordNode, ns);
      }
    }
  }
}
