private boolean isPrototypePropertyAssign(Node assign) {
  if (assign != null && assign.isAssign()) {
    Node getPropNode = assign.getFirstChild();

    if (getPropNode != null && getPropNode.isGetProp()) {
      Node property = getPropNode.getLastChild();
      
      // Check if the property name of the GETPROP node is 'prototype'
      if (property.isString() && "prototype".equals(property.getString())) {
        Node object = getPropNode.getFirstChild();

        // Check that the object is an actual object and not part of a prototype chain
        if (object.isName() || (object.isGetProp() && !object.getLastChild().getString().equals("prototype"))) {
          return true;
        }
      }
    }
  }
  return false;
}
