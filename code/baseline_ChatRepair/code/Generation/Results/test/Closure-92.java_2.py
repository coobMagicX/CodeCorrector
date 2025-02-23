void replace() {
  if (firstNode == null) {
    replacementNode = candidateDefinition;
    return;
  }

  // Detach explicitNode to prevent any wrong references or memory leaks
  if (explicitNode != null) {
    explicitNode.detachFromParent();
    compiler.reportCodeChange();
  }

  if (candidateDefinition != null) {
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
        candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
        replacementNode = varNode;
        if (assignNode.getJSDocInfo() != null) {
          nameNode.setJSDocInfo(assignNode.getJSDocInfo());
        }
        compiler.reportCodeChange();
      }
    }
  } else {
    replacementNode = createDeclarationNode();
  }

  // Adjust node placement in the compiler's AST, considering module boundaries
  if (firstModule == minimumModule) {
    firstNode.getParent().addChildBefore(replacementNode, firstNode);
  } else {
    // Handling when the provided name spread across independent modules
    int indexOfDot = namespace.indexOf('.');
    if (indexOfDot == -1) {
      compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
    } else {
      String parentNamespace = namespace.substring(0, indexOfDot);
      ProvidedName parentName = providedNames.get(parentNamespace);
      if (parentName == null || parentName.replacementNode == null) {
        // Create and insert the parent namespace if not already present
        Node parentNamespaceNode = createNamespaceNode(parentNamespace);
        compiler.getNodeForCodeInsertion(minimumModule).addChildToFront(parentNamespaceNode);
        parentNamespaceNode.addChild(replacementNode);
      } else {
        parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
      }
    }
  }
  compiler.reportCodeChange();
}
