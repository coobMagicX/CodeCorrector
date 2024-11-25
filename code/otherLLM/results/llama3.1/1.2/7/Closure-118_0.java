private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();

    if (isNumber(name)) { // Check if property name is a number
      T type = typeSystem.getType(getScope(), n, name);
      Property prop = getProperty(name);
      if (!prop.scheduleRenaming(child,
                                 processProperty(t, prop, type, null))) {
        // TODO(user): It doesn't look like the user can do much in this
        // case right now.
        if (propertiesToErrorFor.containsKey(name)) {
          compiler.report(JSError.make(
              t.getSourceName(), child, propertiesToErrorFor.get(name),
              Warnings.INVALIDATION, name,
              (type == null ? "null" : type.toString()), n.toString(), ""));
        }
      }
    } else { // If property name is not a number, assume it's a string
      T type = typeSystem.getType(getScope(), n, name);
      Property prop = getProperty(name);
      if (!prop.scheduleRenaming(child,
                                 processProperty(t, prop, type, null))) {
        // TODO(user): It doesn't look like the user can do much in this
        // case right now.
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

// New method to check if a property name is a number
private boolean isNumber(String name) {
  try {
    Double.parseDouble(name);
    return true;
  } catch (NumberFormatException e) {
    return false;
  }
}