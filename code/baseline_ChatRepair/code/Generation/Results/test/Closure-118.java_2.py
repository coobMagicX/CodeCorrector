private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (prop != null) {
      if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
        if (propertiesToErrorFor.containsKey(name)) {
          compiler.report(JSError.make(
              t.getSourceName(), child, propertiesToErrorFor.get(name),
              Warnings.INVALIDATION, name,
              (type == null ? "null" : type.toString()), n.toString(), ""));
        }
      }
    } else {
      // Log an error if the property does not exist
      compiler.report(JSError.make(
          t.getSourceName(), child,
          "PROPERTY_NOT_FOUND",
          "Property " + name + " not found in current scope."));
    }
  }
}
