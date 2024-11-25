JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // TODO(user): Investigate whether it is really necessary to keep two
  // different mechanisms for resolving named types, and if so, which order
  // makes more sense. Now, resolution via registry is first in order to
  // avoid triggering the warnings built into the resolution via properties.
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // Check for inheritance cycle before proceeding with properties resolution
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t); // Handle type cycle immediately when detected
  }
  
  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  resolveViaProperties(t, enclosing);
  
  // Check for inheritance cycle again after properties resolution
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t); // Handle type cycle immediately when detected
  }
  
  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ?
      getReferencedType() : this;
}