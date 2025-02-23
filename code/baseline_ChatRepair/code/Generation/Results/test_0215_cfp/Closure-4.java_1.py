JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);

  // Handle type cycles before marking resolution as complete for registry resolved types
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolvedViaRegistry) {
    // Resolve within super, which might include further type checks or completions
    super.resolveInternal(t, enclosing);
    if (isResolved()) {
      finishPropertyContinuations();
    }
  } else {
    // Attempt to resolve via properties if not yet resolved
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
    super.resolveInternal(t, enclosing);
    if (isResolved()) {
      finishPropertyContinuations();
    }
  }
  
  // Perform final checks and return the correct type based on whether the type registry is at last generation
  return registry.isLastGeneration() ? getReferencedType() : this;
}
