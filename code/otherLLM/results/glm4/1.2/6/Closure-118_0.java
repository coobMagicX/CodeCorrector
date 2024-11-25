private void handleObjectLit(NodeTraversal t, Node n) {
  Map<String, Property> properties = new HashMap<>(); // Stores existing properties

  // First pass: Collect all properties from the object literal.
  for (Node child = n.getFirstChild();
       child != null;
       child = child.getNext()) {
    if (child.isMemberExpression() || child.isObjectLiteral()) { // Check for member expressions and object literals
      String name = child.getString();
      T type = typeSystem.getType(getScope(), n, name);
      properties.put(name, getProperty(name));
    }
  }

  // Second pass: Process the collected properties.
  for (Node child = n.getFirstChild();
       child != null;
       child = child.getNext()) {
    if (child.isMemberExpression() || child.isObjectLiteral()) { // Check for member expressions and object literals
      String name = child.getString();
      T type = typeSystem.getType(getScope(), n, name);
      Property prop = properties.get(name);

      // Only rename or process the property if there is a change to be made.
      boolean shouldProcess = false;
      if (prop != null) {
        shouldProcess = !prop.scheduleRenaming(child,
                                                processProperty(t, prop, type, null));
      }

      if (!shouldProcess && propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}