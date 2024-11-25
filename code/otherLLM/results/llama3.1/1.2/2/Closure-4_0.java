JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // Check for cycle before proceeding with property resolution
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  // Attempt to resolve via properties, re-checking for cycle
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