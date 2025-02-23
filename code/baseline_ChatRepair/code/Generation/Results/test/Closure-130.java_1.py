private void inlineAliases(GlobalNamespace namespace) {
  Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    if (name.globalSets == 1 && name.localSets == 0 && name.aliasingGets > 0) {
      List<Ref> refs = Lists.newArrayList(name.getRefs());
      for (Ref ref : refs) {
        if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
          if (inlineAliasIfPossible(ref, namespace)) {
            // Decrement aliasingGets when an alias is inlined
            name.aliasingGets--;
            name.removeRef(ref);
          }
        }
      }
    }

    if (name.aliasingGets == 0) {
      if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) &&
          name.props != null && !name.props.isEmpty()) {
        // Include properties only if they still satisfy the initial conditions.
        for (Name child : name.props) {
          if (child.globalSets == 0 && child.localSets == 0 && !workList.contains(child)) {
            workList.add(child);
          }
        }
      }
    }
  }
}
