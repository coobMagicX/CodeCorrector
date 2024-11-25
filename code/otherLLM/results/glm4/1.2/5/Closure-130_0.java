private void inlineAliases(GlobalNamespace namespace) {
  Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
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
          // Try to inline it.
          if (inlineAliasIfPossible(ref, namespace)) {
            name.removeRef(ref);
          }
        }
      }

      // After inlining local aliases, check if there are still references
      // that require inlining.
      if ((name.type == Name.Type.OBJECTLIT ||
           name.type == Name.Type.FUNCTION) &&
          name.aliasingGets == 0 && name.props != null) {
        // All of name's children meet condition (a), so they can be
        // added to the worklist.
        for (Name prop : name.props) {
          if ((prop.type == Name.Type.OBJECTLIT ||
              prop.type == Name.Type.FUNCTION) &&
              prop.aliasingGets == 0 && prop.props != null) {
            workList.add(prop);
          }
        }
      }
    } else if ((name.type == Name.Type.OBJECTLIT ||
                name.type == Name.Type.FUNCTION) &&
               name(aliasingGets == 0 && name.props != null)) {
      // Check if the current name has properties that can be inlined.
      for (Name prop : name.props) {
        if ((prop.type == Name.Type.OBJECTLIT ||
            prop.type == Name.Type.FUNCTION) &&
            prop.aliasingGets == 0 && prop.props != null) {
          workList.add(prop);
        }
      }
    }

    // Check if there are still properties that can be inlined after checking for the current name.
    if (name(props != null)) {
      for (Name prop : name.props) {
        if ((prop.type == Name.Type.OBJECTLIT ||
            prop.type == Name.Type.FUNCTION) &&
            prop.aliasingGets == 0 && prop.props != null) {
          workList.add(prop);
        }
      }
    }
  }
}