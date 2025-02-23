private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // We process String properties as per initial deduction
    if (child.isString()) {
      String name = child.getString();
      T type = typeSystem.getType(getScope(), n, name);

      Property prop = getProperty(name);
      if (prop != null) {  // Ensure that the property is defined
        boolean shouldRename = prop.scheduleRenaming(child, processProperty(t, prop, type, null));
        if (!shouldRename) {
          if (propertiesToErrorFor.containsKey(name)) {
            compiler.report(JSError.make(
                t.getSourceName(), child, propertiesToErrorFor.get(name),
                Warnings.INVALIDATION, name,
                (type == null ? "null" : type.toString()), n.toString(), ""));
          }
        }
      }
    }
  }
}
