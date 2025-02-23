private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 1 : 2;  // More adaptive condition

  for (int i = 0; i < referenceInfo.references.size(); i++) {  // Determine after which reference the variable gets read.
      if (referenceInfo.references.get(i) == init) {
          firstRefAfterInit = i + 1;
          break;
      }
  }

  if (refCount > 1 &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value = (init != null) ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount > firstRefAfterInit) {
    Reference reference = referenceInfo.references.get(firstRefAfterInit); // Use calculated index
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, referenceInfo.references);
      staleVars.add(v);
    }
  }

  // The rest of the original code provided would remain unchanged...
}
