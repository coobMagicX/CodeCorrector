private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }

        if (name.globalSets == 1 && name.localSets == 0) {
            boolean anyInlined = false;
            List<Ref> refs = new ArrayList<Ref>(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.removeRef(ref);
                        anyInlined = true;
                    }
                }
            }
            // Update aliasingGets count after inlining attempts.
            if (anyInlined) {
                name.recalculateAliasingGets();
            }
        }

        // Add properties of the name to the workList only if no aliasing gets are existing
        if (name.aliasingGets == 0 && name.props != null && !name.props.isEmpty()) {
            for (Name prop : name.props) {
                if (prop.aliasingGets == 0) {
                    workList.add(prop);
                }
            }
        }
    }
}
