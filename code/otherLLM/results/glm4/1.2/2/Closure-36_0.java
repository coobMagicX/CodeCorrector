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
  if (declaration.getBasicBlock() != initialization.getBasicBlock()
      || declaration.getBasicBlock() != reference.getBasicBlock()) {
    return false;
  }

  // Do not inline into a call node. This would change
  // the context in which it was being called.
  Node value = initialization.getAssignedValue();
  if (value == null) {
    Preconditions.checkState(false);
  }
  if (value.isGetProp()
      && reference.getParent().isCall()
      && reference.getParent().getFirstChild() == reference.getNode()) {
    // Singleton getter method call detection
    return false;
  }

  if (value.isFunction()) {
    Node callNode = reference.getParent();
    if (callNode.isCall()) {
      CodingConvention convention = compiler.getCodingConvention();
      // Bug 2388531: Don't inline subclass definitions into class defining
      // calls as this confused class removing logic.
      SubclassRelationship relationship =
          convention.getClassesDefinedByCall(callNode);
      if (relationship != null) {
        return false;
      }

      // issue 668: Don't inline singleton getter methods calls
      // by checking for singleton getProp nodes and ensuring they are not part of a class defining call.
      if (value.isGetProp() && relationship == null) {
        Node parent = value.getParent();
        while (parent != null && !parent.isCall()) {
          parent = parent.getParent();
        }
        return !(parent != null && parent.isCall());
      }
    }
  }

  return canMoveAggressively(value) ||
         canMoveModerately(initialization, reference);
}