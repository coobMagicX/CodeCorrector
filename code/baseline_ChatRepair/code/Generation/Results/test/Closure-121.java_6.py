private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 1 : (init == null ? 1 : 2);

  if (refCount > firstRefAfterInit &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value;
    if (init != null) {
      value = init.getAssignedValue();
    } else {
      Node srcLocation = declaration.getNode();
      value = NodeUtil.newUndefinedNode(srcLocation);
    }
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (firstRefAfterInit < refCount) {
    // Ensure the index firstRefAfterInit - 1 is valid
    Reference reference = referenceInfo.references.get(firstRefAfterInit - 1);
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

  if (!maybeModifiedArguments &&
      !staleVars.contains(v) &&
      referenceInfo.isWellDefined() &&
      referenceInfo.isAssignedOnceInLifetime()) {
    for (int i = 1; i < referenceInfo.references.size(); i++) {
      Node nameNode = referenceInfo.references.get(i).getNode();
      if (aliasCandidates.containsKey(nameNode)) {
        AliasCandidate candidate = aliasCandidates.get(nameNode);
        if (!staleVars.contains(candidate.alias) &&
            !isVarInlineForbidden(candidate.alias)) {
          Reference aliasInit = candidate.refInfo.getInitializingReference();
          if (aliasInit != null) {
            Node value = aliasInit.getAssignedValue();
            Preconditions.checkNotNull(value);
            inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
            staleVars.add(candidate.alias);
          }
        }
      }
    }
  }
}
