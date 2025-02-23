private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (child.isStringKey()) {
      String name = child.getString();
      JSType type = typeSystem.evaluateType(t, child); // Evaluates the type directly from the node
      
      Property prop = getProperty(name);
      if (prop != null) {
        boolean shouldRename = prop.scheduleRenaming(child, processProperty(t, prop, type, null));
        if (!shouldRename && propertiesToErrorFor.containsKey(name)) {
          compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()),
            n.toString(), ""));
        }
      }
    }
  }
}
