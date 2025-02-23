JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Attempt to resolve the type via the registry first
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  } else {
    // Proceed to resolve via properties only if unresolved via registry
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }

    super.resolveInternal(t, enclosing);
    if (isResolved()) {
      finishPropertyContinuations();
    }
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }
}
