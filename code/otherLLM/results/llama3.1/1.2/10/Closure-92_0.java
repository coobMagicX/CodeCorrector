void replace() {
  if (firstNode == null) {
    replacementNode = candidateDefinition;
    return;
  }

  if (candidateDefinition != null && explicitNode != null) {
    explicitNode.detachFromParent();
    compiler.reportCodeChange();

    // Does this need a VAR keyword?
    replacementNode = candidateDefinition;
    if (NodeUtil.isExpressionNode(candidateDefinition)) {
      candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);
      Node assignNode = candidateDefinition.getFirstChild();
      Node nameNode = assignNode.getFirstChild();
      if (nameNode.getType() == Token.NAME) {
        // Need to convert this assign to a var declaration.
        Node valueNode = nameNode.getNext();
        assignNode.removeChild(nameNode);
        assignNode.removeChild(valueNode);
        nameNode.addChildToFront(valueNode);
        Node varNode = new Node(Token.VAR, nameNode);
        varNode.copyInformationFrom(candidateDefinition);
        candidateDefinition.getParent().replaceChild(
            candidateDefinition, varNode);
        nameNode.setJSDocInfo(assignNode.getJSDocInfo());
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
      if (indexOfDot != -1) {
        ProvidedName parentName =
            providedNames.get(namespace.substring(0, indexOfDot));
        Preconditions.checkNotNull(parentName);
        Preconditions.checkNotNull(parentName.replacementNode);
        getEnclosingDeclNameNode(null).getParent().addChildAfter(
            replacementNode, parentName.replacementNode);
      } else {
        compiler.getNodeForCodeInsertion(minimumModule)
            .addChildToBack(replacementNode);
      }
    }
    if (explicitNode != null) {
      explicitNode.detachFromParent();
    }
    compiler.reportCodeChange();
  }
}