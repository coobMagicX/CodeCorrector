JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // First, try to resolve the type via the registry.
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Process resolution via registry if successful.
  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    JSType referencedType = getReferencedType();
    return registry.isLastGeneration() ? referencedType : this;
  }

  // If not resolved via registry, try to resolve via type properties.
  resolveViaProperties(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ? getReferencedType() : this;
}
