private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  if (refCount > 1 &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    // if the variable is referenced more than once, we can only
    // inline it if it's immutable and never defined before referenced.
    Node value;
    if (init != null) {
      value = init.getAssignedValue();
    } else {
      // Create a new node for variable that is never initialized.
      Node srcLocation = declaration.getNode();
      value = NodeUtil.newUndefinedNode(srcLocation);
    }
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == firstRefAfterInit) {
    // The variable likely only read once, try some more
    // complex inlining heuristics.
    Reference reference = referenceInfo.references.get(
        firstRefAfterInit - 1);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (refCount == 2 && declaration != init) {
    // Only one initialization and not the same as the declaration.
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, referenceInfo.references);
      staleVars.add(v);
    }
  }

  // If this variable was not inlined normally, check if we can
  // inline an alias of it. (If the variable was inlined, then the
  // reference data is out of sync. We're better off just waiting for
  // the next pass.)
  if (!maybeModifiedArguments &&
      !staleVars.contains(v) &&
      referenceInfo.isWellDefined() &&
      referenceInfo.isAssignedOnceInLifetime()) {
    // Inlining the variable based solely on well-defined and assigned
    // once is *NOT* correct. We relax the correctness requirement if
    // the variable is declared constant.
    List<Reference> refs = referenceInfo.references;
    for (int i = 1 /* start from a read */; i < refs.size(); i++) {
      Node nameNode = refs.get(i).getNode();
      if (aliasCandidates.containsKey(nameNode)) {
        AliasCandidate candidate = aliasCandidates.get(nameNode);
        if (!staleVars.contains(candidate.alias) &&
            !isVarInlineForbidden(candidate.alias)) {
          Reference aliasInit;
          aliasInit = candidate.refInfo.getInitializingReference();
          Node value = aliasInit.getAssignedValue();
          Preconditions.checkNotNull(value);
          inlineWellDefinedVariable(candidate.alias,
              value,
              candidate.refInfo.references);
          staleVars.add(candidate.alias);
        }
      }
    }
  }
}

// Assuming the following methods are defined elsewhere in the class:
private boolean isImmutableAndWellDefinedVariable(Var v, ReferenceCollection referenceInfo) {
  // Implementation should check immutability and well-definedness.
}

private void inlineWellDefinedVariable(Var v, Node value, List<Reference> references) {
  // Implementation should handle inlining a variable with a defined value.
}

private boolean canInline(Reference declaration, Reference init, Reference reference) {
  // Implementation should decide whether to inline based on the given references.
}

private void inline(Var v, Reference declaration, Reference init, Reference reference) {
  // Implementation should perform the actual inlining.
}

private boolean isValidDeclaration(Reference declaration) {
  // Implementation should validate the declaration of the variable.
}

private boolean isValidInitialization(Reference init) {
  // Implementation should validate the initialization of the variable.
}