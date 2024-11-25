JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Perform resolution via the registry first to avoid warnings from properties resolution.
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // If a cycle is detected during the registry resolution, handle it.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Check if resolution was successful via the registry.
  if (resolved) {
    super.resolveInternal(t, enclosing); // Call superclass to ensure proper handling of inherited types.
    finishPropertyContinuations(); // Finish property continuations after calling the superclass method.
    return registry.isLastGeneration() ? getReferencedType() : this;
  }

  // If resolution via registry did not succeed, proceed with properties resolution.
  resolveViaProperties(t, enclosing);

  // Check for a cycle if it wasn't caught during the initial registry resolution.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing); // Call superclass method regardless of whether we've resolved via properties or not.

  // Finish property continuations only if the type is fully resolved.
  if (isResolved()) {
    finishPropertyContinuations();
  }
  
  return registry.isLastGeneration() ? getReferencedType() : this;
}