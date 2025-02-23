private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();

        // Don't attempt to inline a getter or setter property as a variable.
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }

        // When globalSets is 1 and localSets is 0, and there are aliasingGets, check for aliases.
        if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref != null && ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.removeRef(ref);
                    }
                }
            }
        }

        if (name.aliasingGets == 0) {
            // Check if name has any aliases left after the local-alias-inlining above.
            if (name.props != null) {
                // Add properties to workList if all properties have no aliasing gets.
                boolean allPropsClear = true;
                for (Name prop : name.props) {
                    if (prop.aliasingGets != 0) {
                        allPropsClear = false;
                        break;
                    }
                }
                if (allPropsClear) {
                    workList.addAll(name.props);
                }
            }
        }
    }
}
