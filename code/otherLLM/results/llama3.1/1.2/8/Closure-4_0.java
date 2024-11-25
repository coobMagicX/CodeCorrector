JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }
  
  if (!resolved && !super.resolveInternal(t, enclosing)) {
    resolveViaProperties(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
    finishPropertyContinuations();
  } else if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }
  
  return registry.isLastGeneration() ?
      getReferencedType() : this;
}