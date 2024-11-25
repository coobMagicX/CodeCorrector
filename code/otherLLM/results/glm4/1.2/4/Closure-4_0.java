JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
    // First, attempt to resolve via the registry to avoid triggering warnings
    boolean resolved = resolveViaRegistry(t, enclosing);
    if (resolved && detectImplicitPrototypeCycle()) {
        handleTypeCycle(t);
    }

    // If not already resolved by the registry, try resolving via properties
    if (!resolved) {
        resolveViaProperties(t, enclosing);
        if (detectImplicitPrototypeCycle()) {
            handleTypeCycle(t);
        }
    }

    // Call super's resolveInternal method to ensure proper resolution from parent types
    super.resolveInternal(t, enclosing);

    // If resolved, finish property continuations and return the type accordingly
    if (isResolved()) {
        finishPropertyContinuations();
        return registry.isLastGeneration() ? getReferencedType() : this;
    }

    // Return 'this' if not resolved by either mechanism or by the parent's resolveInternal call
    return this;
}