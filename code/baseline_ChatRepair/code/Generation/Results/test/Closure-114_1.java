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
      
      if (isFunctionAssignmentImmediatelyInvoked(parent, valueNode)) {
        recordDepScope(nameNode, ns); 
      } else {
        recordDepScope(recordNode, ns); 
      }
    }
  }
}


private boolean isFunctionAssignmentImmediatelyInvoked(Node assignmentParent, Node functionNode) {
  
  if (functionNode.isFunction()) {
    Node maybeCallNode = assignmentParent.getNext(); 
    
    if (maybeCallNode != null && maybeCallNode.isCall() && maybeCallNode.getFirstChild().equals(assignmentParent)) {
      return true;
    }
  }
  return false;
}
