private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // Ensure that the property name is a string before proceeding.
    String name = child.getString();
    if (name == null || name.isEmpty()) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "emptyProperty",
          Warnings.INVALIDATION, "Empty Property Name", "", n.toString(), ""));
      continue; // Skip processing this child as it has an invalid property name.
    }

    T type = typeSystem.getType(getScope(), n, name);

    // Ensure the type is not null before proceeding.
    if (type == null) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "nullType",
          Warnings.INVALIDATION, "Null Type", "", n.toString(), ""));
      continue; // Skip processing this child as it has an invalid type.
    }

    Property prop = getProperty(name);
    if (prop == null) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "nullProperty",
          Warnings.INVALIDATION, "Null Property", name, n.toString(), ""));
      continue; // Skip processing this child as it has a null property.
    }

    if (!prop.scheduleRenaming(child,
                               processProperty(t, prop, type, null))) {
      // If renaming fails due to an issue with the property, report it.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}