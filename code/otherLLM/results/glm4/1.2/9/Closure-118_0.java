private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Maybe STRING, GET, SET

    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (prop == null) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "Property not found", Warnings.UNEXPECTED, name,
          "", n.toString(), ""));
      continue;
    }

    boolean shouldRename = scheduleRenaming(child, type);
    if (!shouldRename) {
      // If the property is invalidating and we need to invalidate,
      // but we are skipping renaming (e.g., if it's a known case that doesn't require renaming),
      // then we still need to report this.
      if (typeSystem.isInvalidatingType(type) && !skipRenaming) {
        compiler.report(JSError.make(
            t.getSourceName(), child, "Property with invalidating type found", Warnings.INVALIDATION,
            name, (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    } else if (!prop.scheduleRenaming(child, type)) {
      // If the property cannot be scheduled for renaming for some other reason, report it.
      compiler.report(JSError.make(
          t.getSourceName(), child, propertiesToErrorFor.get(name), Warnings.INVALIDATION,
          name, (type == null ? "null" : type.toString()), n.toString(), ""));
    }
  }
}