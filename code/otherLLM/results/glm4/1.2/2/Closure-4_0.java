JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Resolution via registry is attempted first to avoid warnings that might occur if properties are accessed.
  boolean resolved = resolveViaRegistry(t, enclosing);
  if (resolved && !isResolved()) { // Ensure resolution is only marked after it has been successfully done via registry
    super.resolveInternal(t, enclosing); // Call the superclass's method only if resolution was successful via registry
    finishPropertyContinuations(); // Finish property continuations to allow for any further type resolutions
  }

  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // If not resolved via registry, try resolving through properties.
  if (!resolved) {
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t); // Handle cycle detection after attempting to resolve via properties
    }
  }

  // Call the superclass's method only if the type is actually resolved, not just because of a successful registry resolution attempt.
  super.resolveInternal(t, enclosing);

  // Finish property continuations only if the type is resolved.
  if (isResolved()) {
    finishPropertyContinuations();
  }
  
  return registry.isLastGeneration() ? getReferencedType() : this;
}