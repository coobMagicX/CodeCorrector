JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);

  // Check for cycles after attempting to resolve via registry
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
    return null;  // Return null or handle appropriately after detecting cycle
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  // Attempt to resolve via properties if not resolved via registry
  resolveViaProperties(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
    return null;  // Return null or handle appropriately after detecting cycle
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ?
      getReferencedType() : this;
}