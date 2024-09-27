private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  Set<String> deletedProperties = Sets.newHashSet(); // Set to track deleted properties

  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Handle deleted properties
    if (parent.isDelProp()) {
      String propName = parent.getLastChild().getString();
      deletedProperties.add(propName);
      continue; // Skip further processing for deleted properties
    }

    // Ignore most indirect references, like x.y (but not x.y(),
    // since the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      // A call target may be using the object as a 'this' value.
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (deletedProperties.contains(propName)) {
        return false; // Ensure deleted properties are not inlined
      }

      if (!validProperties.contains(propName)) {
        if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          validProperties.add(propName);
        } else {
          return false;
        }
      }
      continue;
    }

    // Only rewrite VAR declarations or simple assignment statements
    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      // A var with no assignment.
      continue;
    }

    // We're looking for object literal assignments only.
    if (!val.isObjectLit()) {
      return false;
    }

    for (Node child = val.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      String childPropName = child.getString();
      if (deletedProperties.contains(childPropName)) {
        return false; // Ensure deleted properties are not considered
      }

      validProperties.add(childPropName);

      Node childVal = child.getFirstChild();
      // Check if childVal is the parent of any of the passed in
      // references, as that is how self-referential assignments
      // will happen.
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            // There's a self-referential assignment
            return false;
          }
          refNode = refNode.getParent();
        }
      }
    }

    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}