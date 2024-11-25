private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {

  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  // Correct the logic to check if the variable is well-defined and immutable.
  boolean isWellDefinedAndImmutable = false;
  if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    isWellDefinedAndImmutable = true;
  }

  Node value = null;
  if (init != null) {
    value = init.getAssignedValue();
  } else {
    // Create a new node for variable that is never initialized.
    Node srcLocation = declaration.getNode();
    value = NodeUtil.newUndefinedNode(srcLocation);
  }
  Preconditions.checkNotNull(value);

  if (isWellDefinedAndImmutable) {
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == firstRefAfterInit && maybeModifiedArguments) {
    // The variable likely only read once and arguments might be modified.
    Reference reference = referenceInfo.references.get(firstRefAfterInit - 1);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if ((declaration != init && refCount == 2) || (refCount > 1 && isWellDefinedAndImmutable)) {
    if (isValidDeclaration(declaration) && isValidInitialization(init)) {
      // The only reference is the initialization or variable is well-defined and immutable.
      inlineWellDefinedVariable(v, value, referenceInfo.references);
      staleVars.add(v);
    }
  }

  // Check for alias inlining conditionally.
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
          Node valueAlias = aliasInit.getAssignedValue();
          Preconditions.checkNotNull(valueAlias);
          inlineWellDefinedVariable(candidate.alias, valueAlias,
              candidate.refInfo.references);
          staleVars.add(candidate.alias);
        }
      }
    }
  }
}