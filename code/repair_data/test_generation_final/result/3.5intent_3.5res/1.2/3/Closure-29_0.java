private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
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

      Node childVal = child.getFirstChild();
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            return false;
          }
          refNode = refNode.getParent();
        }
      }
    }

    // Check if the object has a function applied to it
    if (val.hasChildren() && val.getFirstChild().isFunction()) {
      // Handle the scenario where an object has a function applied to it
      // Additional handling for objects with functions applied
      Node functionNode = val.getFirstChild();
      if (functionNode.getFirstChild() != null && functionNode.getFirstChild().isName()) {
        String functionName = functionNode.getFirstChild().getString();
        if ("toString".equals(functionName)) {
          continue; // Skip objects with toString function applied
        }
      }
    }

    ret = true;
  }
  return ret;
}