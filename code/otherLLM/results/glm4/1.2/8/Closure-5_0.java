private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore most indirect references, like x.y (but not x.y(),
    // since the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      // Check for deleted properties and do not add them to validProperties
      if (!validProperties.contains(propName)) {
        // If the property is not a variable or simple assignment lhs, don't inline
        if (!NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          return false;
        }
        validProperties.add(propName);
      } else {
        // If the property already exists and it's not handled correctly, don't inline
        if (!isPropertyHandledCorrectly(refs, propName)) {
          return false;
        }
      }
      continue;
    }

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

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }
      validProperties.add(child.getString());
      Node childVal = child.getFirstChild();
      // Check for self-referential assignments
      if (isSelfReferentialAssignment(refs, childVal)) {
        return false;
      }
    }

    ret = true; // Found an object literal assignment
  }
  return ret;
}

// Helper method to check if a property is handled correctly
private boolean isPropertyHandledCorrectly(List<Reference> refs, String propName) {
  for (Reference ref : refs) {
    Node node = ref.getNode();
    if (node.isSetProp() && node.getLastChild().getString().equals(propName)) {
      return true; // Property is set and handled correctly
    }
  }
  return false;
}

// Helper method to check for self-referential assignments
private boolean isSelfReferentialAssignment(List<Reference> refs, Node childVal) {
  for (Reference ref : refs) {
    if (ref.getNode().equals(childVal)) {
      // Found a self-referential assignment
      return true;
    }
  }
  return false;
}