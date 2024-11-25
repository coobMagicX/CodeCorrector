private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  // Check if variable is declared and well-defined.
  if (refCount > 1 && !isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    return; // If not, do nothing as inlining is not possible or desired.
  }

  if (init != null) {
    Node value = init.getAssignedValue();
    Preconditions.checkNotNull(value);
  } else {
    Node srcLocation = declaration.getNode();
    value = NodeUtil.newUndefinedNode(srcLocation);
  }
  Preconditions.checkNotNull(value);

  // Inline variable based on refCount and whether it is well-defined.
  if (refCount > 1) {
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == firstRefAfterInit && canInline(declaration, init, referenceInfo)) {
    inline(v, declaration, init, referenceInfo);
    staleVars.add(v);
  } else if (refCount == 2 && isValidDeclaration(declaration) && isValidInitialization(init)) {
    Node value = init.getAssignedValue();
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  }

  // Check for inlining an alias.
  if (!maybeModifiedArguments &&
      !staleVars.contains(v) &&
      referenceInfo.isWellDefined() &&
      referenceInfo.isAssignedOnceInLifetime()) {
    List<Reference> refs = referenceInfo.references;
    for (int i = 1 /* start from a read */; i < refs.size(); i++) {
      Node nameNode = refs.get(i).getNode();
      if (aliasCandidates.containsKey(nameNode)) {
        AliasCandidate candidate = aliasCandidates.get(nameNode);
        if (!staleVars.contains(candidate.alias) &&
            !isVarInlineForbidden(candidate.alias)) {
          Reference aliasInit = candidate.refInfo.getInitializingReference();
          Node value = aliasInit != null ? aliasInit.getAssignedValue() : null;
          if (value != null) {
            Preconditions.checkNotNull(value);
            inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
            staleVars.add(candidate.alias);
          }
        }
      }
    }
  }
}

// The methods below are used within the fixed method:
private boolean isValidInitialization(Reference initialization) { /* ... */ }
private boolean isValidDeclaration(Reference declaration) { /* ... */ }
private boolean isImmutableAndWellDefinedVariable(Var v, ReferenceCollection refInfo) { /* ... */ }