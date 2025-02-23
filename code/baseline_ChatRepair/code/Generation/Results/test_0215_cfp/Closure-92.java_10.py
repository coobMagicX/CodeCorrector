void replace() {
  JSDocInfo jsDocInfoToPreserve = null;
  if (explicitNode != null) {
    jsDocInfoToPreserve = explicitNode.getJSDocInfo();
    if (explicitNode.getParent() != null) {
      explicitNode.detachFromParent();
    }
  }

  if (firstNode == null) {
    replacementNode = candidateDefinition; // Base case, don't touch 'goog'.
    return;
  }

  if (candidateDefinition != null) {
    replacementNode = candidateDefinition;

    // When we have a duplicate definition:
    if (explicitNode != null) {
      // Reporting code change before modification.
      compiler.reportCodeChange();
      if (NodeUtil.isExpressionNode(candidateDefinition)) {
        candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);
        Node assignNode = candidateDefinition.getFirstChild();
        Node nameNode = assignNode.getFirstChild();
        if (nameNode.getType() == Token.NAME) {
          // Convert assign to var declaration if needed
          Node valueNode = nameNode.getNext();
          assignNode.detach(); // Detach the assign node
          nameNode.detach();
          valueNode.detach();
          nameNode.addChildToFront(valueNode);
          Node varNode = new Node(Token.VAR, nameNode);
          varNode.copyInformationFromForTree(candidateDefinition);
          candidateDefinition.getParent().replaceChild(candidateDefinition, varNode);
          // Preserve JSDoc information.
          if (jsDocInfoToPreserve != null){
            nameNode.setJSDocInfo(jsDocInfoToPreserve);
          }
          compiler.reportCodeChange();
          replacementNode = varNode;
        }
      }
    }
  } else {
    // Handle case where there's not a duplicate definition
    replacementNode = createDeclarationNode();

    if (firstModule == minimumModule) {
      // Addition inside the same parent module
      firstNode.getParent().addChildBefore(replacementNode, firstNode);
    } else {
      // Merging nodes coming from independently provided symbols in different modules.
      ProvidedName parentProvidedName = namespace.contains(".") ?
          providedNames.get(namespace.substring(0, namespace.lastIndexOf('.')))
          : null;

      if (parentProvidedName != null && parentProvidedName.replacementNode != null) {
        Node parent = parentProvidedName.replacementNode.getParent();
        if (parent != null) {
          parent.addChildAfter(replacementNode, parentProvidedName.replacementNode);
        }
      } else {
        compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
      }
    }
    compiler.reportCodeChange();
  }
}
