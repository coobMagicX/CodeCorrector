JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  if (!resolved) {
    // Try to resolve via properties if and only if not resolved via registry
    resolveViaProperties(t, enclosing);
  }
  
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
