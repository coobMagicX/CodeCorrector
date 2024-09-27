private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild();
      child != null;
      child = child.getNext()) {
    // Handle different types of property keys (String, Number)
    String name;
    if (child.isString()) {
      name = child.getString();
    } else if (child.isNumber()) {
      name = String.valueOf(child.getDouble());
    } else {
      continue; // Skip unsupported property types
    }

    T type = typeSystem.getType(getScope(), n, name);
    Property prop = getProperty(name);
    if (!prop.scheduleRenaming(child, type)) {
      // Handle error reporting
      if (propertiesToErrorFor.containsKey(name)) {
        compiler.report(JSError.make(
            t.getSourceName(), child, propertiesToErrorFor.get(name),
            Warnings.INVALIDATION, name,
            (type == null ? "null" : type.toString()), n.toString(), ""));
      }
    } else {
      // Ensure properties are correctly set on the prototype or instance
      if (n.isObjectLit()) {
        // Special handling for object literals 
        Node parent = n.getParent();
        if (parent != null && (parent.isAssign() || parent.isName())) {
          Node targetNode = parent.isAssign() ? parent.getFirstChild() : parent;
          rootTypes.put(targetNode, type);
          renameNodes.add(targetNode);
        }
      }
    }
  }
}