JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
  } else {
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
    super.resolveInternal(t, enclosing);
  }

  if (isResolved()) {
    validateImplementsInterfaces(t);   // Custom function to validate implemented interfaces
    finishPropertyContinuations();
  }

  return registry.isLastGeneration() ?
    getReferencedType() : this;
}

private void validateImplementsInterfaces(ErrorReporter t) {
  if (!isInterface() && isAttemptingToImplementInterface()) {
    t.reportError("Can only implement interfaces", this);
  }
}
