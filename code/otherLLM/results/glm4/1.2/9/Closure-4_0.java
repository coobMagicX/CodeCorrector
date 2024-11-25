JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // TODO(user): Investigate whether it is really necessary to keep two
  // different mechanisms for resolving named types, and if so, which order
  // makes more sense. Now, resolution via registry is first in order to
  // avoid triggering the warnings built into the resolution via properties.
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // Ensure that no cycle is detected before attempting to resolve via properties or by calling super.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // If resolved via registry, finish property continuations and return the type accordingly.
  if (resolved) {
    super.resolveInternal(t, enclosing); // Moved this call after the check for cycle to ensure the prototype chain is not broken
    finishPropertyContinuations();
    return registry.isLastGeneration() ? getReferencedType() : this;
  }

  // If not resolved via registry, attempt resolution via properties.
  resolveViaProperties(t, enclosing);
  
  // After attempting resolution via properties, check again for a cycle before calling super.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Call the superclass's resolveInternal method to allow it to process the type.
  super.resolveInternal(t, enclosing);

  // If resolved after processing by the superclass, finish property continuations.
  if (isResolved()) {
    finishPropertyContinuations();
  }
  
  return registry.isLastGeneration() ? getReferencedType() : this;
}