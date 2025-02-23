JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  // Resolve via registry first to minimize premature resolution warnings.
  boolean resolvedViaRegistry = resolveViaRegistry(t, enclosing);
  if (resolvedViaRegistry && detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  } 

  if (resolvedViaRegistry) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    JSType referencedType = getReferencedType();
    // Ensure the referenced type properly represents an interface when expected.
    if (referencedType != null && !referencedType.isInterface() && expectedToBeInterface()) {
      t.report(JSError.make(ERR_IMPLEMENTS_NON_INTERFACE));
      return this;
    }
    return registry.isLastGeneration() ? referencedType : this;
  }

  // If not resolved via a registry, resolve via properties.
  resolveViaProperties(t, enclosing);
  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  JSType finalType = registry.isLastGeneration() ? getReferencedType() : this;
  if (finalType.isInterface() && !expectedToBeInterface()) {
    t.report(JSError.make(ERR_IMPLEMENTS_NON_INTERFACE));
  }
  return finalType;
}

private boolean expectedToBeInterface() {
  // Implement logic to determine if the current type is expected to be an interface
  // This method should return true if the type is expected to be an interface based on
  // the context or annotations available on the type in the surrounding code.
  return /* conditional logic here */;
}
