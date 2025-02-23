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

      // Prevent inlining of functions into their definition call
      // This also needs to check if it behaves like a singleton getter
      if (isLikelySingletonGetter(callNode)) {
        return false;
      }
    }
  }

  return canMoveAggressively(value) ||
      canMoveModerately(initialization, reference);
}

private boolean isLikelySingletonGetter(Node callNode) {
    // Implement a simple check to identify singleton getter patterns.
    // This needs to be adapted according to your specific logic to detect such patterns.
    // Example:
    // return callNode.isGetProp() && callNode.getLastChild().getString().equals("getInstance");
    return false; // Replace this with actual logic.
}
