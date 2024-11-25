JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
    boolean resolved = false;

    // First attempt to resolve via registry
    if (!resolved) {
        resolved = resolveViaRegistry(t, enclosing);
    }

    // Check for implicit prototype cycle after attempting resolution via registry
    if (detectImplicitPrototypeCycle()) {
        handleTypeCycle(t);
    }

    // If not already resolved, try resolving via properties
    if (!resolved) {
        resolveViaProperties(t, enclosing);
    }

    // Check for implicit prototype cycle after attempting resolution via properties
    if (detectImplicitPrototypeCycle()) {
        handleTypeCycle(t);
    }

    // Resolve super type if necessary
    if (isResolved() && !registry.isLastGeneration()) {
        super.resolveInternal(t, enclosing);
    }

    // If resolved, finish property continuations and return the appropriate type
    if (resolved) {
        finishPropertyContinuations();
        return registry.isLastGeneration() ? getReferencedType() : this;
    }

    return this; // Return current type if not resolved or last generation is reached
}