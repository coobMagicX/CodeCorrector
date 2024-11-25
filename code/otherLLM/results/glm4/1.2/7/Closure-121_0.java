private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();

  // Start inlining process with at least one reference.
  if (refCount > 1 && isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    Node value;
    if (init != null) {
      value = init.getAssignedValue();
    } else {
      value = NodeUtil.newUndefinedNode(declaration.getNode());
    }
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  } else if (refCount == 1 && isImmutableAndOnlyReferencedOnce(v, referenceInfo)) {
    // If the variable is referenced only once and is immutable, try inlining.
    Reference reference = referenceInfo.references.get(0);
    if (canInline(declaration, init, reference)) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (refCount == 2 && !isImmutableAndWellDefinedVariable(v, referenceInfo)) {
    // If the variable is referenced twice and not immutable, check for alias candidates.
    if (!maybeModifiedArguments &&
        !staleVars.contains(v) &&
        referenceInfo.isWellDefined() &&
        referenceInfo.isAssignedOnceInLifetime()) {
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
}

// Helper method to check if the variable is immutable and only referenced once.
private boolean isImmutableAndOnlyReferencedOnce(Var v, ReferenceCollection referenceInfo) {
  return isImmutable(v) && referenceInfo.references.size() == 1;
}