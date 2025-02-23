void replace() {
    if (firstNode == null) {
        // Don't touch the base case ('goog').
        replacementNode = candidateDefinition;
        return;
    }

    // Handle duplicate definition for explicitly provided symbols
    if (candidateDefinition != null && explicitNode != null) {
        explicitNode.detachFromParent();
        compiler.reportCodeChange();  // Reporting the detachment immediately

        if (NodeUtil.isExpressionNode(candidateDefinition)) {
            candidateDefinition.putBooleanProp(Node.IS_NAMESPACE, true);
            Node assignNode = candidateDefinition.getFirstChild();
            Node nameNode = assignNode.getFirstChild();

            if (nameNode.getType() == Token.NAME) {
                // Convert assign to a var declaration
                Node valueNode = nameNode.getNext();
                assignNode.removeChild(nameNode);
                assignNode.removeChild(valueNode);
                nameNode.addChildToFront(valueNode);
                Node varNode = new Node(Token.VAR, nameNode);
                varNode.copyInformationFromForTree(candidateDefinition);

                Node parent = candidateDefinition.getParent();
                if (parent != null) {
                    parent.replaceChild(candidateDefinition, varNode);
                }
                nameNode.setJSDocInfo(assignNode.getJSDocInfo());
                compiler.reportCodeChange();
                replacementNode = varNode;
            }
        }
    } else {
        // Handle no duplicate definition
        replacementNode = createDeclarationNode();
        if (firstModule == minimumModule) {
            firstNode.getParent().addChildBefore(replacementNode, firstNode);
        } else {
            int indexOfDot = namespace.indexOf('.');
            if (indexOfDot == -1) {
                compiler.getNodeForCodeInsertion(minimumModule).addChildToBack(replacementNode);
            } else {
                ProvidedName parentName = providedNames.get(namespace.substring(0, indexOfDot));
                Preconditions.checkNotNull(parentName);
                Preconditions.checkNotNull(parentName.replacementNode);

                Node parentNode = parentName.replacementNode.getParent();
                if (parentNode != null) {
                    parentNode.addChildAfter(replacementNode, parentName.replacementNode);
                }
            }
        }
        if (explicitNode != null) {
            explicitNode.detachFromParent();
        }
        compiler.reportCodeChange();
    }
}
