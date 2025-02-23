private void inlineAliases(GlobalNamespace namespace) {
  Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
  HashSet<Name> processed = new HashSet<>();  // To keep track of processed names

  while (!workList.isEmpty()) {
    Name name = workList.pop();
    if (!processed.add(name)){
      continue; // Skip if name was already processed
    }

    // Skip getter or setter properties.
    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    // Only act on names that have exactly 1 global set, no local sets
    // and at least 1 aliasing get.
    if (name.globalSets == 1 && name.localSets == 0) {
      List<Ref> refs = new ArrayList<>(name.getRefs());
      for (Ref ref : refs) {
        if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
          if (inlineAliasIfPossible(ref, namespace)) {
            name.removeRef(ref);
            name.aliasingGets--; // Update the aliasing count
          }
        }
      }
    }

    // Re-check if the name has any aliases left after local alias inlining
    // and make sure it's an object literal or function to proceed.
    if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) &&
        name.aliasingGets == 0 && name.props != null) {
      // Since no more aliases, it's safe to add properties to the worklist.
      workList.addAll(name.props);
    }
  }
}
