private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  Set<String> deletedProperties = new HashSet<>();
  
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
      if (gramps.isDelProp()) {
        // Mark property as deleted if it is being deleted
        deletedProperties.add(propName);
        return false; // Cannot inline if properties are being deleted
      } else if (!validProperties.contains(propName) && !deletedProperties.contains(propName)) {
        if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          validProperties.add(propName);
        } else {
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

      String currentProp = child.getString();
      if (deletedProperties.contains(currentProp)) {
        return false; // If property was marked as deleted, cannot inline
      }

      validProperties.add(currentProp);
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
    ret = true;
  }

  return ret;
}
