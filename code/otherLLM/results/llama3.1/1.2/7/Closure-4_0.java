JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // TODO(user): Investigate whether it is really necessary to keep two
  // different mechanisms for resolving named types, and if so, which order
  // makes more sense. Now, resolution via registry is first in order to
  // avoid triggering the warnings built into the resolution via properties.
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  if (!resolved) {
    // If not resolved yet, try resolving via properties
    resolveViaProperties(t, enclosing);
    
    // Check again for cycle after resolving via properties
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
    
    // Only call super.resolveInternal once, after all attempts at resolution
    super.resolveInternal(t, enclosing);
  } else {
    finishPropertyContinuations();
  }

  return registry.isLastGeneration() ?
      getReferencedType() : this;
}