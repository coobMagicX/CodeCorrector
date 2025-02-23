private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int indexInit = referenceInfo.references.indexOf(init);
  int firstRefAfterInit = (declaration == init) ? indexInit + 1 : indexInit;

  if (refCount > firstRefAfterInit && // Only consider inlining if there are further references after the initialization
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value;
    if (init != null) {
      value = init.getAssignedValue();
    } else {
      Node srcLocation = declaration.getNode();
      value = NodeUtil.newUndefinedNode(srcLocation);
    }
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references.subList(firstRefAfterInit, refCount));
    staleVars.add(v);
  } else if (refCount == firstRefAfterInit + 1 && // Correctly identify when the first reference after initialization happens
           init != null && 
           !maybeModifiedArguments) {
    Reference onlyRef = referenceInfo.references.get(firstRefAfterInit);
    if (canInline(declaration, init, onlyRef)) {
      inline(v, declaration, init, onlyRef);
      staleVars.add(v);
    }
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, Collections.singletonList(declaration));
      staleVars.add(v);
    }
  }

  if (!maybeModifiedArguments && !staleVars.contains(v) &&
      referenceInfo.isWellDefined() && referenceInfo.isAssignedOnceInLifetime()) {
    List<Reference> refs = referenceInfo.references;
    for (int i = 1; i < refs.size(); i++) {
      Node nameNode = refs.get(i).getNode();
      if (aliasCandidates.containsKey(nameNode)) {
        AliasCandidate candidate = aliasCandidates.get(nameNode);
        if (!staleVars.contains(candidate.alias) &&
            !isVarInlineForbidden(candidate.alias)) {
          Reference aliasInit = candidate.refInfo.getInitializingReference();
          Node value = aliasInit.getAssignedValue();
          Preconditions.checkNotNull(value);
          inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
          staleVars.add(candidate.alias);
        }
      }
    }
  }
}
