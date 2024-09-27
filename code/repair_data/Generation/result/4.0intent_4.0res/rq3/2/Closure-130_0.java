private void inlineAliases(GlobalNamespace namespace) {
    // Invariant: All the names in the worklist meet condition (a).
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        // Don't attempt to inline a getter or setter property as a variable.
        if (name.getType() == Name.Type.GET || name.getType() == Name.Type.SET) {
            continue;
        }

        if (name.getGlobalSets() == 1 && name.getLocalSets() == 0 &&
            name.getAliasingGets() > 0) {
            // {@code name} meets condition (b). Find all of its local aliases
            // and try to inline them.
            List<Ref> refs = new ArrayList<>(name.getRefs());
            for (Ref ref : refs) {
                if (ref.getType() == Ref.Type.ALIASING_GET && ref.getScope().isLocal()) {
                    // {@code name} meets condition (c). Try to inline it.
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.removeRef(ref);
                    }
                }
            }
        }

        // Check if {@code name} has any aliases left after the
        // local-alias-inlining above.
        if ((name.getType() == Name.Type.OBJECTLIT ||
             name.getType() == Name.Type.FUNCTION) &&
            name.getAliasingGets() == 0 && name.getProps() != null) {
            // All of {@code name}'s children meet condition (a), so they can be
            // added to the worklist.
            workList.addAll(name.getProps());
        }
    }
}