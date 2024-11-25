JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Check for implicit prototype cycle before resolving via registry to avoid triggering warnings.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // If resolution was successful via registry and a cycle was not detected, proceed with the method chain.
  if (resolved) {
    super.resolveInternal(t, enclosing); // Move this call before finishPropertyContinuations() for consistency
    finishPropertyContinuations();
    return registry.isLastGeneration() ? getReferencedType() : this;
  }

  // If not resolved via registry, attempt to resolve via properties and check for cycles again.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }
  
  resolveViaProperties(t, enclosing);

  // After resolving via properties, check for cycles and finish property continuations if type is resolved.
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing); // Ensure this call remains here for consistency
  return isResolved() ? finishPropertyContinuations() : this;
}