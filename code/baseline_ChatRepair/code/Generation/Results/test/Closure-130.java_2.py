private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        // Skipping renaming GET and SET types. No processing is needed for these.
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }

        // Process names meeting inline conditions
        if (name.globalSets == 1 && name.localSets == 0) {
            List<Ref> refs = new ArrayList<>(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.aliasingGets--;
                        name.removeRef(ref);
                    }
                }
            }
        }

        // After inlining, check if the name has no aliasing gets
        if (name.aliasingGets == 0) {
            if (name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) {
                // Now add children to the worklist safely, checking for null and non-empty
                if (name.props != null && !name.props.isEmpty()) {
                    for (Name prop : name.props) {
                        if (!workList.contains(prop)) {
                            workList.add(prop);
                        }
                    }
                }
            }
        }
    }
}

private boolean inlineAliasIfPossible(Ref ref, GlobalNamespace namespace) {
    // Assuming this method attempts to inline an alias and returns whether it was successful
    // We'd need to see its implementation to provide accurate fixes here
    return true; // placeholder
}
