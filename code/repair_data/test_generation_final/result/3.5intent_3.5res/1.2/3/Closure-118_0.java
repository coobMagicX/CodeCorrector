private void handleObjectLit(NodeTraversal t, Node n) {
  String objectName = n.getString();
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    String propertyName = objectName + "." + child.getString();
    T type = typeSystem.getType(getScope(), child, propertyName);

    Property prop = getProperty(propertyName);
    if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
      if (propertiesToErrorFor.containsKey(propertyName)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(propertyName),
            Warnings.INVALIDATION, propertyName,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    }
  }
}