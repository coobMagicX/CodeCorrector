JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // TODO(user): Investigate whether it is really necessary to keep two
  // different mechanisms for resolving named types, and if so, which order
  // makes more sense. Now, resolution via registry is first in order to
  // avoid triggering the warnings built into the resolution via properties.
  
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // Check for cycle detection after resolving via registry
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }
  
  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }
  
  // Only resolve via properties if not resolved via registry
  resolveViaProperties(t, enclosing);
  
  // Check for cycle detection after resolving via properties
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