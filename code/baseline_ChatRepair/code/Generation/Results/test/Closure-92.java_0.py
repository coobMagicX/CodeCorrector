void replace() {
  if (firstNode == null) {
    // Don't touch the base case ('goog').
    replacementNode = candidateDefinition;
    return;
  }

  if (candidateDefinition != null && explicitNode != null) {
    explicitNode.detachFromParent();
    compiler.reportCodeChange();

    replacementNode = candidateDefinition;
    if (NodeUtil.isExpressionNode(candidateDefinition)) {
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
        candidateDefinition.getParent().replaceChild(
            candidateDefinition, varNode);
        if (assignNode.getJSDocInfo() != null) {
            nameNode.setJSDocInfo(assignNode.getJSDocInfo());
        }
        compiler.reportCodeChange();
        replacementNode = varNode;
      }
    }
  } else {
    replacementNode = createDeclarationNode();
    if (firstModule == minimumModule) {
      firstNode.getParent().addChildBefore(replacementNode, firstNode);
    } else {
      int indexOfDot = namespace.indexOf('.');
      if (indexOfDot == -1) {
        compiler.getNodeForCodeInsertion(minimumModule)
            .addChildToBack(replacementNode);
      } else {
        ProvidedName parentName =
            providedNames.get(namespace.substring(0, indexOfDot));
        Preconditions.checkNotNull(parentName);
        Preconditions.checkNotNull(parentName.replacementNode);
        parentName.replacementNode.getParent().addChildAfter(
            replacementNode, parentName.replacementNode);
      }
    }
    if (explicitNode != null) {
      explicitNode.detachFromParent();
    }
    compiler.reportCodeChange();
  }
}
