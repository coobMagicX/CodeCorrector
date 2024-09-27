private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (!validProperties.contains(propName)) {
        if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          validProperties.add(propName);
        } else {
          return false;
        }
      }
      continue;
    }

    if (!NodeUtil.isVarOrAssignExprLhs(name)) {
       return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue;
    }

    if (!val.isObjectLit()) {
      return false;
    }

    // Check for ES5 getter/setter definitions and self-referential assignments
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef() || isSelfReferential(child, refs)) {
        return false;
      }
      validProperties.add(child.getString());
    }

    ret = true;
  }
  return ret;
}

private boolean isSelfReferential(Node child, List<Reference> refs) {
  Node childVal = child.getFirstChild();
  for (Reference ref : refs) {
    Node refNode = ref.getParent();
    while (refNode != null && !NodeUtil.isStatementBlock(refNode)) {
      if (refNode == childVal) {
        return true;
      }
      refNode = refNode.getParent();
    }
  }
  return false;
}