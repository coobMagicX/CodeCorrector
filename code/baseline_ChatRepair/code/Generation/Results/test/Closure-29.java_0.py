private boolean isInlinableObject(List<Reference> refs) {
  boolean inlinableFound = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Skip properties that do not alter the ability to inline the object
    if (parent.isGetProp()) {
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }
      continue;
    }

    // Only consider variable declarations and simple assignment statements
    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue;
    }

    if (!val.isObjectLit()) {
      return false;
    }

    // Ensure there are no self-referential assignments
    if (containsSelfReferences(val, name)) {
      return false;
    }

    // Check for presence of ES5 getters/setters that are not supported
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }
    }

    // An acceptable object literal assignment is found
    inlinableFound = true;
  }
  return inlinableFound;
}

private boolean containsSelfReferences(Node objLit, Node name) {
  HashBiMap<Node, String> propNames = HashBiMap.create();
  for (Node child = objLit.getFirstChild(); child != null; child = child.getNext()) {
    Node childVal = child.getFirstChild();
    propNames.put(childVal, child.getString());
  }

  for (Map.Entry<Node, String> entry : propNames.entrySet()) {
    Node childVal = entry.getKey();
    if (!childVal.isName()) {
      continue;
    }
    if (childVal.getString().equals(name.getString())) {
      return true; // Direct self-referential found
    }

    // Check indirect references
    while (childVal != null && !NodeUtil.isStatementBlock(childVal)) {
      if (childVal == name) {
        return true;
      }
      childVal = childVal.getParent();
    }
  }

  return false;
}
