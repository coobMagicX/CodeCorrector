void replace() {
  if (firstNode == null) {
    // Base case handling
    replacementNode = candidateDefinition;
    return;
  }

  if (candidateDefinition != null && explicitNode != null) {
    // Detach the existing node
    explicitNode.detachFromParent();
    compiler.reportCodeChange();

    replacementNode = candidateDefinition;

    // Ensure namespace property is set to true if it's an expression node
    if (NodeUtil.isExpressionNode(candidateDefinition)) {
      candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);

      Node assignNode = candidateDefinition.getFirstChild();
      Node nameNode = assignNode.getFirstChild();
      if (nameNode.getType() == Token.NAME) {
        // Transform an assignment expression into a variable declaration
        Node valueNode = nameNode.getNext();
        assignNode.removeChild(nameNode);
        assignNode.removeChild(valueNode);
        nameNode.addChildToFront(valueNode);
        Node varNode = new Node(Token.VAR, nameNode);
        varNode.copyInformationFromForTree(candidateDefinition);
        candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
        if (assignNode.getJSDocInfo() != null) {
          nameNode.setJSDocInfo(assignNode.getJSDocInfo());
        }
        compiler.reportCodeChange();
        replacementNode = varNode;
      }
    }
  } else {
    // Handle no duplication detected
    replacementNode = createDeclarationNode();

    if (firstModule == minimumModule) {
      firstNode.getParent().addChildBefore(replacementNode, firstNode);
    } else {
      // In modules that do not first define the node, place correctly
      int indexOfDot = namespace.indexOf('.');
      if (indexOfDot == -1) {
        compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
      } else {
        String parentNamespace = namespace.substring(0, indexOfDot);
        ProvidedName parentName = providedNames.get(parentNamespace);
        Preconditions.checkNotNull(parentName);
        Preconditions.checkNotNull(parentName.replacementNode);
        parentName.replacementNode.getParent().addChildAfter(replacementNode, parentName.replacementNode);
      }
    }

    if (explicitNode != null) {
      explicitNode.detachFromParent();
    }
    compiler.reportCodeChange();
  }
}
