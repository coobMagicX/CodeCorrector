void replace() {
  if (firstNode == null) {
    // Don't touch the base case ('goog'), assign candidateDefinition as replacement node.
    replacementNode = candidateDefinition;
    return;
  }

  // Check for explicit duplicate definitions
  if (candidateDefinition != null && explicitNode != null) {
    // Detach the explicit node if it has a parent
    if (explicitNode.getParent() != null) {
      explicitNode.detachFromParent();
    }
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

        if (candidateDefinition.getParent() != null) {
          candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
        }

        compiler.reportCodeChange();
        replacementNode = varNode;
      }
    }
  } else {
    // Handle the situation of non-duplicate definitions
    replacementNode = createDeclarationNode();

    if (replacementNode != null) {
      if (firstModule == minimumModule) {
        if (firstNode.getParent() != null) {
          firstNode.getParent().addChildBefore(replacementNode, firstNode);
        }
      } else {
        int indexOfDot = namespace.indexOf('.');
        if (indexOfDot == -1) {
          compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
        } else {
          ProvidedName parentName = providedNames.get(namespace.substring(0, indexOfDot));
          Preconditions.checkNotNull(parentName);
          Preconditions.checkNotNull(parentName.replacementNode);
          if (parentName.replacementNode.getParent() != null) {
            parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
          }
        }
      }
    }
    compiler.reportCodeChange();
  }
}
