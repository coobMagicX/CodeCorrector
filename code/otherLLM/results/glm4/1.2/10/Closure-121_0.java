private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {

  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  if (refCount > 1 &&
      isImmutableAndWellDefinedVariable(v, referenceInfo)) {
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
    Reference reference = referenceInfo.references.get(
        firstRefAfterInit - 1);
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
  } else {
    // If the variable was not inlined normally, check if we can
    // inline an alias of it. (If the variable was inlined, then the
    // reference data is out of sync. We're better off just waiting for
    // the next pass.)
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
}