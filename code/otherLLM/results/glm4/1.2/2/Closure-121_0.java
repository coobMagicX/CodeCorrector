private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();

  // Check if the variable is immutable and well-defined
  if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value;
    if (init != null) {
      value = init.getAssignedValue();
    } else {
      // If the variable is never initialized, create a new node for it.
      Node srcLocation = declaration.getNode();
      value = NodeUtil.newUndefinedNode(srcLocation);
    }
    Preconditions.checkNotNull(value);

    // Inline the variable using the well-defined initialization
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == 2 && !isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    // If there is only one reference and it's not immutable or well-defined,
    // we need to handle this case separately
    Reference reference = referenceInfo.references.get(1);

    // Check the validity of the declaration and initialization
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      value = init.getAssignedValue();
      Preconditions.checkNotNull(value);
      inlineWellDefinedVariable(v, value, referenceInfo.references);
      staleVars.add(v);
    }
  } else if (refCount == 1 && canInlineSingleReference(declaration, init)) {
    // If there is only one reference and it's valid for inlining
    Node value = init.getAssignedValue();
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  }

  // Additional checks for alias inlining if conditions are met
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
          inlineWellDefinedVariable(candidate.alias, value, candidate.refInfo.references);
          staleVars.add(candidate.alias);
        }
      }
    }
  }
}

// Additional method to check if a single reference is valid for inlining
private boolean canInlineSingleReference(Reference declaration, Reference init) {
  return isValidDeclaration(declaration) && isValidInitialization(init);
}