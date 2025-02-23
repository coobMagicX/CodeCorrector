JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  // First cycle check after attempting to resolve via registry
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  // Only proceed with other resolution methods if not resolved via registry
  if (!resolved) {
    resolveViaProperties(t, enclosing);
  }
  
  // Perform resolution within superclass which might involve further checks
  super.resolveInternal(t, enclosing);
  
  // Check cycles again after all resolutions tried
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }
  
  // Ensure properties are finished if the type is fully resolved
  if (isResolved()) {
    finishPropertyContinuations();
  }

  // Return the correctly referenced type if in the last generation of registry, else return itself [conditional path might vary based on your system]
  return registry.isLastGeneration() ? getReferencedType() : this;
}
