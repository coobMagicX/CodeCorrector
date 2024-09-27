private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (prop != null) {
        T processedType = processProperty(t, prop, type, null);
        if (!prop.scheduleRenaming(child, processedType)) {
            // TODO(user): It doesn't look like the user can do much in this
            // case right now.
            if (propertiesToErrorFor.containsKey(name)) {
                compiler.report(JSError.make(
                    t.getSourceName(), child, propertiesToErrorFor.get(name),
                    Warnings.INVALIDATION, name,
                    (type == null ? "null" : type.toString()), n.toString(), ""));
            }
        }
    } else {
        // Handling case where property is not found which could be a missing scenario in the original code.
        compiler.report(JSError.make(
            t.getSourceName(), child, DiagnosticType.warning("MISSING_PROPERTY"),
            "Missing property definition for " + name));
    }
  }
}