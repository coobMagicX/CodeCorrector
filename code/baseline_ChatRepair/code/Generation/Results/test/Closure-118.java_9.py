private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Keys in object literals might be STRING, GET, SET
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    if (type == null) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "Could not determine the type for property.",
          "TYPE_DETERMINATION_ERROR", name));
      continue; // Skip further processing for this property if type is undetermined.
    }

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
      // Handle error or fallback scenario if renaming is not successful.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            type.toString(), n.toString(), ""));
      }
    }
  }
}
