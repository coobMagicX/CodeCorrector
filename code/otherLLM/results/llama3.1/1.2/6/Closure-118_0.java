private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    if (type == null) { // Verify property types
      compiler.report(JSError.make(
          t.getSourceName(), child, "Null Type", Warnings.INVALIDATION,
          name, "null", n.toString(), ""));
      continue;
    }

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child,
                               processProperty(t, prop, type, null))) {
      // scheduleRenaming returns false when the property is not found or invalid.
      // In this case, we should not report an error prematurely.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            type.toString(), n.toString(), ""));
      }
    }
  }
}