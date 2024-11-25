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
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
      }

      // Short-circuit to avoid undefined properties on Object.prototype
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
    boolean isSelfReferential = false;
    Node childVal = val.getFirstChild();
    while (childVal != null) {
      if (childVal.isGetterDef() ||
          childVal.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      if (isSelfReferentialAssignment(childVal, refs)) {
        isSelfReferential = true;
        break;
      }
      childVal = childVal.getNext();
    }

    if (isSelfReferential) {
      // There's a self-referential assignment
      return false;
    }

    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}

private boolean isSelfReferentialAssignment(Node child, List<Reference> refs) {
  if (child == null) {
    return false;
  }

  Node parent = child.getParent();
  while (!NodeUtil.isStatementBlock(parent)) {
    if (parent == child) {
      // Found a self-referential assignment
      return true;
    }
    parent = parent.getParent();
  }

  // Check for references that point to the property being defined in a manner
  // that would cause a self-referential assignment.
  for (Reference t : refs) {
    if (isSelfReferential(t, child)) {
      return true;
    }
  }

  return false;
}

private boolean isSelfReferential(Reference ref, Node child) {
  Node refNode = ref.getParent();
  while (!NodeUtil.isStatementBlock(refNode)) {
    if (refNode == child) {
      return true;
    }
    refNode = refNode.getParent();
  }
  return false;
}