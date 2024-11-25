JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = false;

  // First attempt to resolve via the registry.
  if (!resolveViaRegistry(t, enclosing)) {
    // If not resolved by the registry, try resolving via properties.
    resolveViaProperties(t, enclosing);
  }

  // After attempting resolution, check for implicit prototype cycles.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Only call super.resolveInternal after the above checks and resolutions.
  if (!resolved) { // Ensure that we only call it once, as it is already called in resolveViaRegistry if not resolved.
    resolved = true; // Mark as resolved to prevent calling super again later.
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
  }

  // Return the type based on the last generation of the registry.
  return registry.isLastGeneration() ? getReferencedType() : this;
}