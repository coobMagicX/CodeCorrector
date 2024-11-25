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
    for (Node child = val.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isGetterDef() ||
          child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      Node childVal = child.getFirstChild();
      boolean isSelfReferential = false;

      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            // There's a self-referential assignment
            isSelfReferential = true;
            break;
          }
          refNode = refNode.getParent();
        }

        if (isSelfReferential) {
          break;
        }
      }

      if (isSelfReferential) {
        return false;
      }
    }

    // Check for additional properties or expressions in the same scope
    Node parentExpr = getImmediateParentExpression(name);
    if (parentExpr != null && !parentExpr.isExpressionBlock()) {
      return false;
    }

    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}

private Node getImmediateParentExpression(Node node) {
  Node parent = node.getParent();
  while (!NodeUtil.isStatementBlock(parent)) {
    if (parent.isExpression()) {
      return parent;
    }
    parent = parent.getParent();
  }
  return null;
}