private void handleObjectLit(NodeTraversal t, Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (child.isStringKey()) { // Check if child is a string key
      Node value = child.getFirstChild();

      // Retrieve the property name
      String name = child.getString();
      
      // Retrieve the property value type
      T type = typeSystem.getType(getScope(), value);

      Property prop = getProperty(name);
      if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
        // TODO: Handle error reporting here
      }
    }
  }
}