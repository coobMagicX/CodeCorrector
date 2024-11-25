void replace() {
  if (firstNode == null) {
    // Don't touch the base case ('goog').
    replacementNode = candidateDefinition;
    return;
  }

  // Handle the case where there is a duplicate definition for an explicitly
  // provided symbol.
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
    } else {
      // If the candidate definition is not an expression node, then it's likely
      // that we should just replace the existing declaration.
      replacementNode = candidateDefinition;
    }
  } else {
    // Handle the case where there's not a duplicate definition.
    replacementNode = createDeclarationNode();
    if (firstModule == minimumModule) {
      firstNode.getParent().addChildBefore(replacementNode, firstNode);
    } else {
      // In this case, the name was implicitly provided by two independent
      // modules. We need to move this code up to a common module.
      int indexOfDot = namespace.indexOf('.');
      if (indexOfDot == -1) {
        // Any old place is fine.
        compiler.getNodeForCodeInsertion(minimumModule)
            .addChildToBack(replacementNode);
      } else {
        // Add it after the parent namespace.
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