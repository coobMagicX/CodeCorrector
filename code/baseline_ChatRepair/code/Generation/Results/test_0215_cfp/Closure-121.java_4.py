private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  List<Reference> references = referenceInfo.references;
  int refCount = references.size();
  Reference declaration = references.get(0);
  Reference init = referenceInfo.getInitializingReference();

  // Ensure we correctly determine the first use after initialization.
  boolean initFound = false;
  int firstRefAfterInit = -1;
  for (int i = 0; i < refCount; i++) {
    if (references.get(i) == init) {
      initFound = true;
      firstRefAfterInit = i + 1;
    }
    if (initFound && i >= firstRefAfterInit) {
      break;
    }
  }

  if (initFound && refCount > firstRefAfterInit && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value = (init != null) ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, references);
    staleVars.add(v);
  } else if (!initFound || refCount == 1) { // Check if it should be inlined when only declared or similar edge cases.
    Node value = (init != null) ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    if (value != null) {
      inlineWellDefinedVariable(v, value, references);
      staleVars.add(v);
    }
  }
  
  // Further condition checks, handling of aliases, debug logs, or corrective actions should be considered if necessary.
  // ...
}
