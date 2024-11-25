private boolean canInline(
    Reference declaration,
    Reference initialization,
    Reference reference) {
  if (!isValidDeclaration(declaration)
      || !isValidInitialization(initialization)
      || !isValidReference(reference)) {
    return false;
  }

  // If the value is read more than once, skip it.
  if (declaration != initialization &&
      !initialization.getGrandparent().isExprResult()) {
    return false;
  }

  // Be very conservative and do not cross control structures or
  // scope boundaries
  if (declaration.getBasicBlock() != null && initialization.getBasicBlock() != null &&
      declaration.getBasicBlock() != initialization.getBasicBlock()
      || declaration.getBasicBlock() != reference.getBasicBlock()) {
    return false;
  }

  // Do not inline into a call node. This would change
  // the context in which it was being called.
  Node value = initialization.getAssignedValue();
  if (value == null) {
    Preconditions.checkState(false);
    return false;
  }
  if (value.isGetProp()
      && reference.getParent().isCall()
      && reference.getParent().getFirstChild() == reference.getNode()) {
    return false;
  }

  if (value.isFunction()) {
    Node callNode = reference.getParent();
    if (callNode != null && callNode.isCall()) {
      CodingConvention convention = compiler.getCodingConvention();
      // Bug 2388531: Don't inline subclass definitions into class defining
      // calls as this confused class removing logic.
      SubclassRelationship relationship =
          convention.getClassesDefinedByCall(callNode);
      if (relationship != null) {
        return false;
      }

      // issue 668: Don't inline singleton getter methods
      // calls as this confused class removing logic.
    }
  }

  return canMoveAggressively(value) ||
         canMoveModerately(initialization, reference);
}