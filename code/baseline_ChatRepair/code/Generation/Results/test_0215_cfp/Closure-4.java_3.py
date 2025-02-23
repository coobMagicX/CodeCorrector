JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Attempt to resolve type via registry first
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ? getReferencedType() : this;
  } else { // If not resolved via registry, attempt to resolve via properties
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
}
