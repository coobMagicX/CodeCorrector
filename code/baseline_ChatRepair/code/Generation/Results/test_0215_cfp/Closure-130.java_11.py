private void inlineAliases(GlobalNamespace namespace) {
  // Invariant: All the names in the worklist meet condition (a).
  Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    // Skip inlining for getter or setter property kinds.
    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    // Criteria check: Only one global set, no local sets, and has aliasing gets.
    if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
      List<Ref> refs = Lists.newArrayList(name.getRefs());
      for (Ref ref : refs) {
        if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
          if (inlineAliasIfPossible(ref, namespace)) {
            ref.name.removeRef(ref);
            // As the alias was successfully inlined, update the alias count.
            --ref.name.aliasingGets;
          }
        }
      }
    }

    // Additionally, see if there are unresolved aliases preventing proper inlining.
    if (name.aliasingGets == 0 && name.props != null) {
      if (name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) {
        // Assure recursive add to worklist only if child names are safe (aliasingGets=0).
        for (Name propName : name.props) {
          if (propName.aliasingGets == 0) {
            workList.add(propName);
          }
        }
      }
    }
  }
}
