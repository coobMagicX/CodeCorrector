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
    if (callNode.isCall()) {
      CodingConvention convention = compiler.getCodingConvention();
      SubclassRelationship relationship =
          convention.getClassesDefinedByCall(callNode);
      if (relationship != null) {
        return false;
      }

      // Check if it’s potentially a singleton getter.
      // This requires concrete implementation of isSingletonGetter or replacing this
      // with an actual construct that checks against your project conventions.
      if (isSingletonGetterPattern(callNode)) {
        return false;
      }
    }
  }

  return canMoveAggressively(value) ||
      canMoveModerately(initialization, reference);
}

private boolean isSingletonGetterPattern(Node callNode) {
    // Implementing a basic heuristic 
    // Check if the method call is to a known singleton pattern method
    // e.g., A typical singleton might use `getInstance` or similar as an identifier.
    Node target = callNode.getFirstChild();
    return target.isGetProp() && "getInstance".equals(target.getLastChild().getString());
}
