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

    // Only continue to process a name if it is an OBJECTLIT or FUNCTION and it meets specific conditions.
    if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) &&
        name.aliasingGets == 0 && name.props != null) {
      for (Name child : name.props) {
        // Verify if child names have their aliasing gets zeroed down from earlier operations.
        if (child.aliasingGets == 0) {
          workList.add(child);
        }
      }
    }
  }
}
