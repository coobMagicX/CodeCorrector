JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = false; // Initialize variable to prevent unexpected behavior

  if (resolveViaRegistry(t, enclosing)) {
    resolved = true; // Set resolved flag when registry resolution succeeds
  }

  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t); // Call handleTypeCycle before resolving via properties
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  } else { // Move resolveViaProperties inside the else block to ensure it's called only when registry resolution fails
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t); // Call handleTypeCycle before resolving via properties
    }
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ?
      getReferencedType() : this;
}