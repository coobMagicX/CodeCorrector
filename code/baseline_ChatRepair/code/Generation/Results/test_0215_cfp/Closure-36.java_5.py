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

  // Do not cross control structures or scope boundaries
  if (declaration.getBasicBlock() != initialization.getBasicBlock()
      || declaration.getBasicBlock() != reference.getBasicBlock()) {
    return false;
  }

  // Do not inline into a call node. This would change the context (e.g., `this` value).
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
      // Here, we should add additional conditions based on your coding conventions or
      // specific project requirements related to function inlining.
      
      // Example: Prevent inlining if function is a constructor or a specific pattern (singleton)
      // These conditions would be added here:
      /*
        if (isConstructor(callNode) || isSingletonPattern(callNode)) {
          return false;
        }
      */
    }
  }

  // Other conditions for movement based on the specifics of the initialization and reference
  return canMoveAggressively(value) || canMoveModerately(initialization, reference);
}
