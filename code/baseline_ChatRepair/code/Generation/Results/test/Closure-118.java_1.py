private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    // Ensure that type is not null before processing properties.
    if (type != null) {
      Property prop = getProperty(name);
      if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
        if (propertiesToErrorFor.containsKey(name)) {
          compiler.report(JSError.make(
              t.getSourceName(), child, propertiesToErrorFor.get(name),
              Warnings.INVALIDATION, name,
              type.toString(), n.toString(), ""));
        }
      }
    } else {
      // Handle the case where type is null, for example, log an error or warning.
      compiler.report(JSError.make(
          t.getSourceName(), child, DiagnosticType.warning("TYPE_NOT_FOUND"),
          "Type for property " + name + " not found."));
    }
  }
}
