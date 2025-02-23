private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {

  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  
  // Adjust initialization to ensure init is always non-null when used.
  if (init == null && refCount > 1) {
    init = declaration;
  }

  int firstRefAfterInit = (declaration == init) ? 1 : 2;

  if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value = init != null ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    Preconditions.checkNotNull(value, "Value cannot be null when inlining");
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == firstRefAfterInit) {
    Reference reference = referenceInfo.references.get(firstRefAfterInit - 1);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value, "Initialization value cannot be null");
      inlineWellDefinedVariable(v, value, referenceInfo.references);
      staleVars.add(v);
    }
  }

  if (!maybeModifiedArguments && !staleVars.contains(v) &&
      referenceInfo.isWellDefined() && referenceInfo.isAssignedOnceInLifetime()) {
    inlineAlias(v, referenceInfo);
  }
}

private void inlineAlias(Var v, ReferenceCollection referenceInfo) {
  List<Reference> refs = referenceInfo.references;
  for (int i = 1; i < refs.size(); i++) {
    Node nameNode = refs.get(i).getNode();
    if (aliasCandidates.containsKey(nameNode)) {
      AliasCandidate candidate = aliasCandidates.get(nameNode);
      if (!staleVars.contains(candidate.alias) && !isVarInlineForbidden(candidate.alias)) {
        Reference aliasInit = candidate.refInfo.getInitializingReference();
        Node value = (aliasInit != null) ? aliasInit.getAssignedValue() : null;
        Preconditions.checkNotNull(value, "Alias value cannot be null when inlining");
        inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
        staleVars.add(candidate.alias);
      }
    }
  }
}
