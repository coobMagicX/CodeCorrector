JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = false;

  // First attempt to resolve via the registry.
  if (!resolveViaRegistry(t, enclosing)) {
    // If not resolved through the registry and a cycle is detected during properties resolution,
    // handle it immediately before trying to resolve via properties.
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
  }

  // Now attempt to resolve via properties.
  boolean propertyResolved = resolveViaProperties(t, enclosing);

  if (!resolved && propertyResolved) {
    resolved = true;
  }

  if (resolved || propertyResolved) { // Check if either resolution was successful.
    super.resolveInternal(t, enclosing); // Call the superclass method only if we have a resolved type.
    finishPropertyContinuations(); // Finish property continuations after resolving via registry or properties.
    return registry.isLastGeneration() ? getReferencedType() : this;
  }

  if (detectImplicitPrototypeCycle()) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing); // Call the superclass method even if we haven't resolved.
  
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ? getReferencedType() : this;
}