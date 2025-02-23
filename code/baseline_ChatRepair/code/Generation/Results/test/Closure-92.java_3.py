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

    // Check if it needs a VAR keyword
    if (NodeUtil.isExpressionNode(candidateDefinition)) {
      candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);
      Node assignNode = candidateDefinition.getFirstChild();
      Node nameNode = assignNode.getFirstChild();

      if (nameNode.getType() == Token.NAME) {
        // Convert this assign to a var declaration if it's a NAME type
        Node valueNode = nameNode.getNext();
        assignNode.removeChild(nameNode);
        assignNode.removeChild(valueNode);
        nameNode.addChildToFront(valueNode);
        Node varNode = new Node(Token.VAR, nameNode);
        varNode.copyInformationFromForTree(candidateDefinition);
        if (candidateDefinition.getParent() != null) {
          candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
        } else {
          // Handle orphan candidateDefinition without a parent.
          candidateDefinition.replaceWith(varNode);
        }
        nameNode.setJSDocInfo(assignNode.getJSDocInfo());
        compiler.reportCodeChange();
        replacementNode = varNode;
      }
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
        compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
      } else {
        // Add it after the parent namespace.
        ProvidedName parentName = providedNames.get(namespace.substring(0, indexOfDot));
        Preconditions.checkNotNull(parentName);
        Preconditions.checkNotNull(parentName.replacementNode);

        if (parentName.replacementNode.getParent() != null) {
          parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
        } else {
          // Handle parent replacementNode without a parent.
          parentName.replacementNode.replaceWith(replacementNode);
        }
      }
    }
    if (explicitNode != null) {
      explicitNode.detachFromParent();
    }
    compiler.reportCodeChange();
  }
}
