JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  // Resolve properties after validating the registry resolution failed
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
