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

    // Ensure that the value is not self-referential and doesn't contain unsupported ES5 getters/setters
    boolean isSelfReferential = false;
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }
      Node childVal = child.getFirstChild();
      if (childVal == null) continue;

      isSelfReferential |= checkForSelfReference(childVal, refs);
    }

    if (!isSelfReferential) {
      ret = true; // Found a valid object literal without self-reference
    } else {
      return false; // Self-referential object literal found
    }
  }
  return ret;
}

private boolean checkForSelfReference(Node node, List<Reference> refs) {
  for (Reference ref : refs) {
    Node refNode = ref.getNode();
    while (refNode != null && !refNode.equals(node)) {
      if (node.isSameType(refNode)) {
        return true; // Self-referential
      }
      refNode = refNode.getParent();
    }
  }
  return false;
}