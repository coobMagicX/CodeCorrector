private void inlineAliases(GlobalNamespace namespace) {
    // Invariant: All the names in the worklist meet condition (a).
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        // Don't attempt to inline a getter or setter property as a variable.
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }

        if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
            // {@code name} meets condition (b). Find all of its local aliases
            // and try to inline them.
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    // Check if the reference is part of a closure.
                    if (!ref.isCapturedInClosure()) {
                        // {@code name} meets condition (c). Try to inline it.
                        if (inlineAliasIfPossible(ref, namespace)) {
                            name.removeRef(ref);
                        }
                    }
                }
            }
        }

        // Check if {@code name} has any aliases left after the
        // local-alias-inlining above.
        if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) &&
            name.aliasingGets == 0 && name.props != null) {
            // All of {@code name}'s children meet condition (a), so they can be
            // added to the worklist.
            workList.addAll(name.props);
        }
    }
}

// Helper method to check if a reference is captured in a closure
private boolean isCapturedInClosure(Ref ref) {
    // Implement logic to determine if the reference is used in a closure.
    // This could involve checking if the reference is used within the scope of any function
    // that is used as a callback or passed to functions like setTimeout, setInterval, etc.
    // Placeholder for actual implementation
    return ref.scope.isInClosure();
}