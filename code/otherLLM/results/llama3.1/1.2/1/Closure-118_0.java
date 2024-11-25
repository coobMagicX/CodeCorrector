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
      // TODO(user): It doesn't look like the user can do much in this
      // case right now.
      JSError error = getErrorForType(type);
      if (error != null) {
        compiler.report(error);
      } else if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}

private JSError getErrorForType(T type) {
  if (type != null && !isValidType(type)) {
    return JSError.make(
        "", // sourceName
        null, // location
        Warnings.INVALIDATION, 
        "Invalid property type", 
        type.toString(), 
        n.toString(), 
        "");
  }
  return null;
}

private boolean isValidType(T type) {
  // implementation to check if the type is valid
}