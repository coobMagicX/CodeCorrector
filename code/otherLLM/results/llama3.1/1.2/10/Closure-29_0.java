private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore indirect references, like x.y (except x.y(), since
    // the function referenced by y might reference 'this').
    //
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      // A call target maybe using the object as a 'this' value.
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
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

    // Make sure that the value is not self-refential. IOW,
    // disallow things like x = {b: x.a}.
    //
    // TODO: Only exclude unorderable self-referential
    // assignments. i.e. x = {a: x.b, b: x.a} is not orderable,
    // but x = {a: 1, b: x.a} is.
    //
    // Also, ES5 getters/setters aren't handled by this pass.
    for (Node child = val.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isGetterDef() ||
          child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      Node childVal = child.getFirstChild();
      // Check if childVal is the parent of any of the passed in
      // references, as that is how self-referential assignments
      // will happen. Also, handle nested object literal assignments.
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal || isNestedObjectLiteral(childVal, refs)) {
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

private boolean isNestedObjectLiteral(Node childVal, List<Reference> refs) {
  for (Reference t : refs) {
    Node refNode = t.getParent();
    while (!NodeUtil.isStatementBlock(refNode)) {
      if (refNode == childVal && hasMultipleProperties(childVal, refs)) {
        return true;
      }
      refNode = refNode.getParent();
    }
  }
  return false;
}

private boolean hasMultipleProperties(Node childVal, List<Reference> refs) {
  int propertyCount = 0;
  for (Reference t : refs) {
    Node refNode = t.getParent();
    while (!NodeUtil.isStatementBlock(refNode)) {
      if (refNode == childVal) {
        propertyCount++;
      }
      refNode = refNode.getParent();
    }
  }
  return propertyCount > 1;
}