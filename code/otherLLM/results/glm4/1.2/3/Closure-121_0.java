private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();

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
  } else if (refCount == 1) { // Changed condition to refCount == 1
    Reference reference = referenceInfo.references.get(0);

    if (!maybeModifiedArguments &&
        !staleVars.contains(v) &&
        referenceInfo.isWellDefined() &&
        referenceInfo.isAssignedOnceInLifetime()) {
      if (canInline(declaration, init, reference)) { // Check if inlining is possible
        inline(v, declaration, init, reference);
        staleVars.add(v);
      }
    }
  } else if (refCount == 2 && declaration != init) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      // The only reference is the initialization, remove the assignment and
      // the variable declaration.
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