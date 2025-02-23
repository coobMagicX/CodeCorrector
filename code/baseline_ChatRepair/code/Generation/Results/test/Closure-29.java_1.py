private boolean isInlinableObject(List<Reference> refs) {
  boolean inlinableFound = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    if (parent.isGetProp()) {
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

    if (isSelfReferential(val, name)) {
      return false;
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }
    }

    inlinableFound = true;
  }

  return inlinableFound;
}

private boolean isSelfReferential(Node objLit, Node name) {
  for (Node property = objLit.getFirstChild(); property != null; property = property.getNext()) {
    Node value = property.getFirstChild();
    if (isReferencing(value, name)) {
      return true;
    }
  }
  return false;
}

private boolean isReferencing(Node node, Node target) {
  if (node == target) {
    return true;
  }
  for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
    if (isReferencing(child, target)) {
      return true;
    }
  }
  return false;
}
