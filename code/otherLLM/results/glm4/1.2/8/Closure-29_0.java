private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore indirect references, like x.y (except x.y(), since
    // the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      // A call target maybe using the object as a 'this' value.
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
      }

      Node propDef = parent.getParent();
      // Check if the property is actually defined on the object literal
      while (propDef != null && !propDef.isObjectLit()) {
        propDef = propDef.getParent();
      }
      if (propDef == null) {
        // Property not defined, so cannot inline.
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
      for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isGetterDef() ||
            child.isSetterDef()) {
          // ES5 get/set not supported.
          return false;
        }

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
      break; // No need to continue, as we only want the first valid object lit.
    } else if (!isVarOrAssignExprLhs(name)) {
      return false;
    }
  }
  return ret;
}