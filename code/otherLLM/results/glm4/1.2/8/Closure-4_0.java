JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Initialize resolved to false before attempting resolution.
  boolean resolved = false;

  // First attempt to resolve via registry. If successful, set resolved to true.
  if (resolveViaRegistry(t, enclosing)) {
    resolved = true;
  }

  // Check for an implicit prototype cycle after the first resolution attempt.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Finish property continuations only if we have a resolved type to work with.
  if (resolved) {
    super.resolveInternal(t, enclosing); // Call super's resolveInternal method
    finishPropertyContinuations(); // Ensure all property continuations are finished
    return registry.isLastGeneration() ? getReferencedType() : this; // Return the referenced type or this depending on last generation
  }

  // If not resolved via registry, attempt to resolve via properties.
  if (resolveViaProperties(t, enclosing)) {
    resolved = true;
  }

  // Check for an implicit prototype cycle after the second resolution attempt.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Call super's resolveInternal method only if we have a resolved type to work with.
  if (resolved) {
    super.resolveInternal(t, enclosing); // This is necessary as super's resolveInternal could also potentially resolve the type
    finishPropertyContinuations(); // Ensure all property continuations are finished
  }

  // Return the object that should be returned after resolution attempts.
  return resolved ? this : registry.isLastGeneration() ? getReferencedType() : this;
}