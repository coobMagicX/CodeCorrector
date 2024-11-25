private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        // Don't attempt to inline a getter or setter property as a variable.
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }

        if (name.globalSets == 1 && name.localSets == 0 &&
            name.aliasingGets > 0) {
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    // Try to inline it and check the result.
                    boolean inlined = inlineAliasIfPossible(ref, namespace);
                    if (inlined) {
                        name.removeRef(ref);
                    }
                }
            }
        }

        // Check if name has any aliases left after local-alias-inlining above.
        if ((name.type == Name.Type.OBJECTLIT ||
             name.type == Name.Type.FUNCTION) &&
            name.aliasingGets == 0 && name.props != null) {
            for (Name child : name.props) {
                // Ensure the children are also added to the worklist if they meet condition (a).
                if (child.meetsConditionA()) { // Assuming there's a method to check this condition
                    workList.push(child);
                }
            }
        }
    }
}