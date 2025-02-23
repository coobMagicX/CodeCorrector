private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (!child.isString()) {
      continue;  // Ensure child node corresponds to a property name
    }

    String name = child.getString();
    JSType type = typeSystem.getType(getScope(), n, name); // Assuming getType returns JSType

    Property prop = getProperty(name);
    if (prop == null) {
      continue; // Skip further processing if property is not defined
    }

    boolean scheduledRenaming = prop.scheduleRenaming(child, processProperty(t, prop, type, null));
    if (!scheduledRenaming) {
      // Additional handling when renaming was not done
      JSError error = propertiesToErrorFor.get(name);
      if (error != null) {
        compiler.report(JSError.make(
          t.getSourceName(), child, error,
          "INVALIDATION", name,
          (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}
