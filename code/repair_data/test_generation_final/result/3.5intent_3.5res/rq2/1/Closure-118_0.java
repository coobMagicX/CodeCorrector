private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child, type)) {
      // TODO(user): It doesn't look like the user can do much in this
      // case right now.
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      } else {
        // If the property is not in propertiesToErrorFor, it means that the
        // user can do something about it. In that case, we should just skip
        // renaming for this property and continue with the next one.
        continue;
      }
    }
  }
}