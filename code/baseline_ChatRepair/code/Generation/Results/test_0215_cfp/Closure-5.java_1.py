private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // If a property is being deleted by a delete operation, we cannot inline this object
    if (parent.isDelProp()) {
      return false;
    }

    // Checking for GetProp nodes where most of the logic about property reuse will be located
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);

      // If parent is called and the call target might use 'this', do not inline
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();

      // If the property was not previously valid and it's used in a direct assignment, record it
      if (!validProperties.contains(propName)) {
        if (parent == name.getParent() && (name.isName() || name.isGetProp())) {
          validProperties.add(propName);
        } else {
          return false;
        }
      }
      continue;
    }

    // Only rewrite variable reassignments and not compound assignments or other LHS types
    if (parent.isAssign() && parent.getFirstChild() == name) {
      Node value = parent.getSecondChild();
      if (!value.isObjectLit()) {
        return false;
      }
    
      for (Node child = value.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isGetterDef() || child.isSetterDef()) {
          // If there are getter/setter definitions, do not inline
          return false;
        }

        validProperties.add(child.getString());

        Node childVal = child.getFirstChild();
        for (Reference t : refs) {
          if (NodeUtil.isAncestorOf(childVal, t.getNode())) {
            return false;
          }
        }
      }

      // Set the check that this reference overall has good potential for inlining
      ret = true;
    } else {
      // Not the correct assignment type, we cannot inline
      return false;
    }
  }
  return ret;
}
