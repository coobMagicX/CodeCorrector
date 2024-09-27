private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(n.getString() + "." + name); // Include object name in property name
    if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
      // TODO(user): It doesn't look like the user can do much in this
      // case right now.
      if (propertiesToErrorFor.containsKey(n.getString() + "." + name)) { // Include object name in property name
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(n.getString() + "." + name), // Include object name in property name
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}