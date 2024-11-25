JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = false;
  try {
    // First attempt to resolve via registry
    resolved = resolveViaRegistry(t, enclosing);
    if (detectImplicitPrototypeCycle()) {
      handleTypeCycle(t);
    }
  } catch (Exception e) {
    t.error("Error resolving type via registry", sourceName, lineno, charno);
    return null; // Handle exception or error appropriately
  }

  if (!resolved) {
    try {
      // If not resolved via registry, attempt to resolve via properties
      resolveViaProperties(t, enclosing);
      if (detectImplicitPrototypeCycle()) {
        handleTypeCycle(t);
      }
    } catch (Exception e) {
      t.error("Error resolving type via properties", sourceName, lineno, charno);
      return null; // Handle exception or error appropriately
    }
  }

  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ? getReferencedType() : this;
}