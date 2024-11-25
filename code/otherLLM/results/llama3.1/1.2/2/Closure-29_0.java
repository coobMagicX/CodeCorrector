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
      boolean hasSelfReference = false;

      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal || isDescendant(childVal, refNode)) {
            // There's a self-referential assignment
            hasSelfReference = true;
            break;
          }
          refNode = refNode.getParent();
        }

        if (hasSelfReference) {
          break;
        }
      }

      if (hasSelfReference) {
        return false;
      }
    }


    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}

// Helper method to check if child is a descendant of parent
private boolean isDescendant(Node child, Node parent) {
  while (child != null) {
    if (child == parent) {
      return true;
    }
    child = child.getParent();
  }
  return false;
}