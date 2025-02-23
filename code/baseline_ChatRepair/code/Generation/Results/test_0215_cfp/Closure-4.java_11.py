JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  if (!resolved) {
    resolved = resolveViaRegistry(t, enclosing);
    if (!resolved) {
      resolveViaProperties(t, enclosing);
    }
  }
  
  detectAndHandleTypeCycle(t, enclosing);
  
  if (!registered) {
    super.resolveInternal(t, enclosing);
  }
  
  if (isResolved()) {
    registerResolution();
    finishPropertyContinuations();
  }

  return resolvedTypeOrThis();
}

private void detectAndHandleTypeCycle(ErrorReporter t, StaticScope<JSType> enclosing) {
  if (detectImplicitPrototypeCycle() && !cycleHandled) {
    handleTypeCycle(t);
    cycleHandled = true;  // To ensure cycles are only handled once
  }
}

private void registerResolution() {
  if (!registered) {
    registered = true;  // Ensure super.resolveInternal is called only once
  }
}

private JSType resolvedTypeOrThis() {
  return registry.isLastGeneration() ? getReferencedType() : this;
}
