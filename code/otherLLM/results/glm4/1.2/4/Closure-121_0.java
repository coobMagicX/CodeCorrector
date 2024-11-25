private void inlineNonConstants(
    Var v, ReferenceCollection referenceInfo,
    boolean maybeModifiedArguments) {
  int refCount = referenceInfo.references.size();
  if (refCount == 0) return; // No references means nothing to inline.

  Reference declaration = referenceInfo.references.get(0);
  Reference init = referenceInfo.getInitializingReference();
  int firstRefAfterInit = (declaration == init) ? 2 : 3;

  boolean isImmutableAndWellDefined = isImmutableAndWellDefinedVariable(v, referenceInfo);

  if (refCount > 1 && isImmutableAndWellDefined) {
    // The variable is referenced more than once and is immutable.
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
    // The variable likely only read once and is well-defined.
    Reference reference = referenceInfo.references.get(firstRefAfterInit - 1);
    boolean canInline = canInline(declaration, init, reference);

    if (canInline) {
      inline(v, declaration, init, reference);
      staleVars.add(v);
    }
  } else if (refCount == 2 && declaration != init && isImmutableAndWellDefined) {
    // The variable has a single initialization that's not the declaration.
    Node value = init.getAssignedValue();
    Preconditions.checkNotNull(value);
    inlineWellDefinedVariable(v, value, referenceInfo.references);
    staleVars.add(v);
  }

  if (!maybeModifiedArguments &&
      !staleVars.contains(v) &&
      referenceInfo.isWellDefined() &&
      referenceInfo.isAssignedOnceInLifetime()) {
    // Inlining the variable based solely on well-defined and assigned once is not correct.
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

// Methods that can be utilized in the context:
// - isImmutableAndWellDefinedVariable(Var v, ReferenceCollection referenceInfo): Check if the variable is both immutable and well-defined.
// - canInline(Reference declaration, Reference init, Reference reference): Determine if a variable can be inlined based on its references.
// - inline(Var v, Reference declaration, Reference init, Reference reference): Perform the inlining operation.
// - inlineWellDefinedVariable(Var v, Node value, List<Reference> references): Inline a well-defined variable with a given value and reference information.
// - isVarInlineForbidden(Var v): Check if inlining a specific variable is forbidden due to certain rules or constraints.