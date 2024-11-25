private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  if (refCount > 1 &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    // ...
  } else if (refCount == firstRefAfterInit) {
    Reference reference = referenceInfo.references.get(
        firstRefAfterInit - 1);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      // ...
    }
  }

  // If this variable was not inlined normally, check if we can
  // inline an alias of it. (If the variable was inlined, then the
  // reference data is out of sync. We're better off just waiting for
  // the next pass.)
  if (!maybeModifiedArguments &&
      !staleVars.contains(v) &&
      referenceInfo.isWellDefined() &&
      isValidAssignment(referenceInfo.getInitializingReference())) {
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

// New method to validate assignment
private boolean isValidAssignment(Reference reference) {
  List<Reference> refSet = referenceInfo.references;
  int startingReadRef = 1;
  Reference refDecl = refSet.get(0);
  if (!isValidDeclaration(refDecl)) {
    return false;
  }

  if (reference == null || !isValidInitialization(reference)) {
    return false;
  }

  if (refDecl != reference) {
    Preconditions.checkState(reference == refSet.get(1));
    startingReadRef = 2;
  }

  return true;
}