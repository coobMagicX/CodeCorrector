void replace() {
  if (firstNode == null) {
    replacementNode = candidateDefinition;
    return;
  }
  
  if (candidateDefinition != null && explicitNode != null) {
    // Explicit definition is present and a collision with candidate is detected.
    explicitNode.detachFromParent();
    compiler.reportCodeChange();
    
    replacementNode = adjustCandidateDefinition(candidateDefinition);
  } else {
    // No explicit collision, usual replacement logic.
    replacementNode = createDeclarationNode();
    if (!placeNodeInProperModule()) {
      // fallback or other necessary action
      replacementNode = null;
    }
    compiler.reportCodeChange();
  }
  
  if (explicitNode != null) {
    explicitNode.detachFromParent();
  }
}

private Node adjustCandidateDefinition(Node candidate) {
  if (NodeUtil.isExpressionNode(candidate)) {
    candidate.putBooleanProp(Node.IS_NAMESPACE, true);
    Node transformedNode = transformToVarNode(candidate);
    if (transformedNode != null) {
      return transformedNode;
    }
  }
  return candidate;
}

private Node transformToVarNode(Node exprNode) {
  Node assignNode = exprNode.getFirstChild();
  Node nameNode = assignNode.getFirstChild();
  if (nameNode.getType() == Token.NAME) {
    Node valueNode = nameNode.getNext();
    Node varNode = new Node(Token.VAR, new Node(Token.NAME, nameNode.getString(), valueNode));
    varNode.copyInformationFrom(nameNode);
    exprNode.getParent().replaceChild(exprNode, varNode);
    compiler.reportCodeChange();
    return varNode;
  }
  return null;
}

private boolean placeNodeInProperModule() {
  if (firstModule == minimumModule) {
    firstNode.getParent().addChildBefore(replacementNode, firstNode);
    return true;
  } else {
    int indexOfDot = namespace.indexOf('.');
    if (indexOfDot != -1) {
      String parentNamespace = namespace.substring(0, indexOfDot);
      ProvidedName parentName = providedNames.get(parentNamespace);
      if (parentName != null && parentName.replacementNode != null) {
        parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
        return true;
      }
    }
    return false;
  }
}
