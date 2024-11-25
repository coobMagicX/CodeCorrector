private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child,
                               processProperty(t, prop, type, null))) {
      // Check if the property type is inconsistent
      if (type != null && (type instanceof NumberType || type instanceof StringType) 
          && !prop.getType().equals(type)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, "Invalid property type",
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      } else if (propertiesToErrorFor.containsKey(name)) {
        // Report invalidation errors for properties that do not conform to expected types
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}