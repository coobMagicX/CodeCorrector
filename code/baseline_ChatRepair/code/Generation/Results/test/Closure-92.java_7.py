void replace() {
  if (firstNode == null) {
    // Base case where there's no first node
    replacementNode = candidateDefinition;
  } else if (candidateDefinition != null && explicitNode != null) {
    // Handle duplicate explicit definition
    replaceExplicitDefinition();
  } else {
    // Handle non-duplicate case normally
    replaceOrMoveDefinition();
  }
  if (explicitNode != null) {
    explicitNode.detachFromParent();
    compiler.reportCodeChange();
  }
}

private void replaceExplicitDefinition() {
  explicitNode.detachFromParent();
  compiler.reportCodeChange();

  replacementNode = candidateDefinition;
  if (NodeUtil.isExpressionNode(candidateDefinition)) {
    transformExpressionToVar();
  }
}

private void replaceOrMoveDefinition() {
  replacementNode = createDeclarationNode();
  if (firstModule == minimumModule) {
    firstNode.getParent().addChildBefore(replacementNode, firstNode);
  } else {
    moveDefinitionToCommonModule();
  }
  compiler.reportCodeChange();
}

private void transformExpressionToVar() {
  candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);
  Node assignNode = candidateDefinition.getFirstChild();
  Node nameNode = assignNode.getFirstChild();
  if (nameNode.getType() == Token.NAME) {
    Node valueNode = nameNode.getNext();
    assignNode.removeChild(nameNode);
    assignNode.removeChild(valueNode);
    nameNode.addChildToFront(valueNode);
    Node varNode = new Node(Token.VAR, nameNode);
    varNode.copyInformationFrom(candidateDefinition);
    candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
    nameNode.setJSDocInfo(assignNode.getJSDocInfo());
    replacementNode = varNode;
  }
}

private void moveDefinitionToCommonModule() {
  int indexOfDot = namespace.indexOf('.');
  if (indexOfDot == -1) {
    compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
  } else {
    ProvidedName parentName = providedNames.get(namespace.substring(0, indexOfDot));
    Preconditions.checkNotNull(parentName);
    Preconditions.checkNotNull(parentName.replacementNode);
    parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
  }
}
