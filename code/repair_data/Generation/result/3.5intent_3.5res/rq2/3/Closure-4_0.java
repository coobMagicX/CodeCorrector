JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // TODO(user): Investigate whether it is really necessary to keep two
  // different mechanisms for resolving named types, and if so, which order
  // makes more sense. Now, resolution via registry is first in order to
  // avoid triggering the warnings built into the resolution via properties.
  boolean resolved = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  resolved = resolveViaProperties(t, enclosing); // Add assignment to resolved variable
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolved) { // Add condition to check if resolved
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  return this; // Return this if not resolved
}