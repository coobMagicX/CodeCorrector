private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    // Check if type is null and report error.
    if (type == null) {
      // Use a defined DiagnosticType instance that fits the error description.
      // Here SOME_ERROR_TYPE should be replaced with an appropriate instance from your DiagnosticGroups.
      compiler.report(JSError.make(
          t.getSourceName(), child, DiagnosticGroups.SOME_ERROR_TYPE, "Could not determine the type for property", name));
      continue; // Skip processing this child node.
    }

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
      // Handle error or fallback scenario if renaming is not successful.
      if (propertiesToErrorFor.containsKey(name)) {
        // Again, use correct DiagnosticType based on context.
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            "Invalid property access or mutation:", name, type.toString(), n.toString()));
      }
    }
  }
}
