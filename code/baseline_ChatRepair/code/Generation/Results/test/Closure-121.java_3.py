private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  List<Reference> references = referenceInfo.references;
  int refCount = references.size();
  Reference declaration = references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = refCount; // Initialize with a default that gets updated

  // Checking if the initializing reference is one of the first few references
  for (int i = 0; i < refCount; i++) {
    if (references.get(i) == init) {
      firstRefAfterInit = i + 1;
      break;
    }
  }

  // More rigorous checking of conditions for inlining based on ref counts
  if (refCount == firstRefAfterInit) {
    Reference reference = references.get(firstRefAfterInit - 1);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (refCount > 1 && firstRefAfterInit < refCount &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value = (init != null) ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, references);
    staleVars.add(v);
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, references);
      staleVars.add(v);
    }
  }

  // Remaining code regarding aliases handling should stay consistent with previous logic
  // ...
}
