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
            name.removeRef(ref);
          }
        }
      }
    }

    // Check if {@code name} has any refs left after the local-alias-inlining above
    // and all its children are also clear of aliasing gets.
    boolean allPropsClearOfAliasingGets = true;
    if (name.props != null) {
      for (Name prop : name.props) {
        if (prop.aliasingGets > 0) {
          allPropsClearOfAliasingGets = false;
          break;
        }
      }
    }

    if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) &&
        name.aliasingGets == 0 && allPropsClearOfAliasingGets) {
      workList.addAll(name.props);
    }
  }
}
