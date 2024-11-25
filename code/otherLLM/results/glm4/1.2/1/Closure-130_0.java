private void inlineAliases(GlobalNamespace namespace) {
  // Invariant: All the names in the worklist meet condition (a).
  Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    // Don't attempt to inline a getter or setter property as a variable.
    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    // Handle extern types separately since they might have different aliasing behavior.
    if (isExternType(name)) {
      handleExternType(name, namespace);
    } else {
      if (name.globalSets == 1 && name.localSets == 0 &&
          name.aliasingGets > 0) {
        // {@code name} meets condition (b). Find all of its local aliases
        // and try to inline them.
        List<Ref> refs = Lists.newArrayList(name.getRefs());
        for (Ref ref : refs) {
          if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
            // {@code name} meets condition (c). Try to inline it.
            if (inlineAliasIfPossible(ref, namespace)) {
              name.removeRef(ref);
            }
          }
        }
      }

      // Check if {@code name} has any aliases left after the
      // local-alias-inlining above.
      if ((name.type == Name.Type.OBJECTLIT ||
           name.type == Name.Type.FUNCTION) &&
          name.aliasingGets == 0 && name.props != null) {
        // All of {@code name}'s children meet condition (a), so they can be
        // added to the worklist.
        workList.addAll(name.props);
      }
    }
  }
}

// Helper method to check if a type is an extern type.
private boolean isExternType(Name name) {
  // Placeholder: Implement logic to determine if the Name represents an extern type.
  return false;
}

// Helper method to handle the aliasing for extern types.
private void handleExternType(Name name, GlobalNamespace namespace) {
  // Placeholder: Implement logic specific to handling extern types during inlining.
}