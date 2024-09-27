private boolean canInline(
    Reference declaration,
    Reference initialization,
    Reference reference) {
  if (!isValidDeclaration(declaration)
      || !isValidInitialization(initialization)
      || !isValidReference(reference)) {
    return false;
  }

  if (declaration != initialization &&
      !initialization.getGrandparent().isExprResult()) {
    return false;
  }

  if (declaration.getBasicBlock() != initialization.getBasicBlock()
      || declaration.getBasicBlock() != reference.getBasicBlock()) {
    return false;
  }

  Node value = initialization.getAssignedValue();
  Preconditions.checkState(value != null);
  if (value.isGetProp()
      && reference.getParent().isCall()
      && reference.getParent().getFirstChild() == reference.getNode()) {
    return false;
  }

  if (value.isFunction()) {
    Node callNode = reference.getParent();
    if (reference.getParent().isCall()) {
      CodingConvention convention = compiler.getCodingConvention();
      SubclassRelationship relationship =
          convention.getClassesDefinedByCall(callNode);
      if (relationship != null) {
        return false;
      }
      
      // Issue 668: Don't inline singleton getter methods calls
      if (value.isFunction() && isSingletonGetter(value)) {
        return false;
      }
    } else {
      return false; // Added to handle non-call parent nodes
    }
  }

  return canMoveAggressively(value) ||
      canMoveModerately(initialization, reference);
}

private boolean isSingletonGetter(Node functionNode) {
  if (functionNode == null || !functionNode.isFunction()) {
    return false;
  }

  Node body = functionNode.getLastChild();
  if (body == null || !body.hasChildren()) {
    return false;
  }

  Node returnStatement = body.getLastChild();
  if (returnStatement == null || !returnStatement.isReturn()) {
    return false;
  }

  Node returnValue = returnStatement.getFirstChild();
  return returnValue != null && returnValue.isGetProp();
}