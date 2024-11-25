private void inlineAliases(GlobalNamespace namespace) {
  // Invariant: All the names in the worklist meet condition (a).
  Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
  while (!workList.isEmpty()) {
    Name name = workList.pop();

    // Don't attempt to inline a getter or setter property as a variable.
    if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
      continue;
    }

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

    // Check if the object or function is not from an extern type and has no aliasing gets
    boolean isFromExternType = false;
    for (Ref ref : refs) {
      if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
        Node node = ref.getNode();
        // Check if the node belongs to an extern type
        isFromExternType |= node.getScope() instanceof ExternNode;
      }
    }

    // Only add children of objects and functions to the worklist if they meet condition (a)
    // and are not from an extern type.
    if ((name.type == Name.Type.OBJECTLIT ||
         name.type == Name.Type.FUNCTION) &&
        !isFromExternType && name.aliasingGets == 0 && name.props != null) {
      workList.addAll(name.props);
    }
  }
}