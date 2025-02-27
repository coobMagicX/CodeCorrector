private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    if (type == null) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "null",
          Warnings.INVALIDATION, name, "", n.toString(), ""));
      continue;
    }

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child,
                               processProperty(t, prop, type, null))) {
      // TODO(user): It doesn't look like the user can do much in this
      // case right now.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            type.toString(), n.toString(), ""));
      } else if (type instanceof MixedType && ((MixedType) type).hasNumbersAndStrings()) {
        // Handle mixed-type properties
        compiler.report(JSError.make(
            t.getSourceName(), child, "mixed types",
            Warnings.INVALIDATION, name,
            type.toString(), n.toString(), ""));
      }
    }
  }
}