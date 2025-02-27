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

      // Check for singleton getter methods
      if (convention.isSingletonGetter(callNode)) {
        return false; // Disallow inlining if it's a singleton getter method.
      }
    }
  }

  return canMoveAggressively(value) ||
      canMoveModerately(initialization, reference);
}
