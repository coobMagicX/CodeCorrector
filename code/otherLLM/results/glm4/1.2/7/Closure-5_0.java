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
      validProperties.add(child.getString());
      Node childVal = child.getFirstChild();

      // Check if any reference in the list refers to a property that is self-referential
      boolean isSelfReferential = refs.stream()
          .anyMatch(t -> {
            Node refNode = t.getParent();
            while (refNode != null) {
              if (refNode == childVal) {
                return true;
              }
              refNode = refNode.getParent();
            }
            return false;
          });

      if (isSelfReferential) {
        return false;
      }
    }

    ret = true; // Set to true after checking all object literal properties
  }
  return ret;
}