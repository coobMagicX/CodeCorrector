private void inlineAliases(GlobalNamespace namespace) {
  // Invariant: All the names in the worklist meet condition (a).
  Deque<Name> workList = new ArrayDeque<>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    // Don't attempt to inline a getter or setter property as a variable.
    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

    if (name.globalSets == 1 && name.localSets == 0 &&
        name.aliasingGets > 0) {
      // Find all of its local aliases and try to inline them.
      List<Ref> refs = Lists.newArrayList(name.getRefs());
      for (Ref ref : refs) {
        if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
          // Try to inline it if possible.
          if (inlineAliasIfPossible(ref, namespace)) {
            name.removeRef(ref);
          }
        }
      }
    }

    // Check if name has any aliases left after the local-alias-inlining above.
    if ((name.type == Name.Type.OBJECTLIT ||
         name.type == Name.Type.FUNCTION) &&
        name.aliasingGets == 0 && name.props != null) {
      // All of name's children meet condition (a), so they can be
      // added to the worklist.
      for (Name prop : name.props) {
        if (prop.isNamespace() &&
            (prop.aliasingGets > 0 || prop.localSets + prop.globalSets > 1 ||
             prop.deleteProps > 0)) {
          checkNamespaces(); // Check for any issues with namespaces.
        }
        workList.push(prop);
      }
    }
  }
}