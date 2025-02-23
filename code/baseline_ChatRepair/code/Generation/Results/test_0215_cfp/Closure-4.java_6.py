JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  } else {
    // Ensure resolution via properties only if necessary and safe.
    if (registry.canResolveProperties(this)) {
        resolveViaProperties(t, enclosing);
    }
  }
  
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  // only consider registry's last generation if the type hasn't been fully resolved during this cycle
  return isResolved() ? getReferencedType() : this;
}
