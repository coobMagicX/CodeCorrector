private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Assume child may only be either a STRING, GET, or SET
    // We should not encounter a mix of numbers and strings for property names
    
    String name = child.getString();
    if (name == null) continue; // Skips if the child does not have a valid string name
    
    T type = typeSystem.getType(getScope(), n, name);
    
    Property prop = getProperty(name);
    if (prop == null) continue; // Proceed only if the property is valid and retrieved

    boolean shouldRename = prop.scheduleRenaming(child, processProperty(t, prop, type, null));
    if (!shouldRename) {
      // Handle the case when renaming is not scheduled
      JSError error = propertiesToErrorFor.get(name);
      if (error != null) {
        compiler.report(JSError.make(
            t.getSourceName(), child, error,
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}
