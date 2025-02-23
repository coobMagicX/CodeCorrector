private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  List<Reference> references = referenceInfo.references;
  int refCount = references.size();
  Reference declaration = references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (init != null) ? references.indexOf(init) + 1 : 1;

  if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value = (init != null) ? init.getAssignedValue() : NodeUtil.newUndefinedNode(declaration.getNode());
    Preconditions.checkNotNull(value, "Inlined value must not be null");
    inlineWellDefinedVariable(v, value, references);
    staleVars.add(v);
  } else if (refCount >= firstRefAfterInit && refCount > 2) {
    for (int i = firstRefAfterInit; i < refCount; i++) {
      Reference ref = references.get(i);
      if (canInline(declaration, init, ref)) {
        inline(v, declaration, init, ref);
        staleVars.add(v);
        break;
      }
    }
  } else if (declaration != init && refCount == 2) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      Node value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, references);
      staleVars.add(v);
    }
  }

  // Inlining aliases, if applicable
  if (!maybeModifiedArguments && !staleVars.contains(v) && referenceInfo.isWellDefined() && referenceInfo.isAssignedOnceInLifetime()) {
    attemptAliasInlining(referenceInfo, v);
  }
}

private void attemptAliasInlining(ReferenceCollection referenceInfo, Var var) {
  for (Reference ref : referenceInfo.references) {
    Node nameNode = ref.getNode();
    AliasCandidate candidate = aliasCandidates.get(nameNode);
    if (candidate != null && !staleVars.contains(candidate.alias) && !isVarInlineForbidden(candidate.alias)) {
      Node value = candidate.refInfo.getInitializingReference().getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
      staleVars.add(var);
    }
  }
}
