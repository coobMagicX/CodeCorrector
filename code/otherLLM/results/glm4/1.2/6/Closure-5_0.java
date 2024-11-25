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
      // Check if the property is valid or needs to be added
      boolean isValidProperty = validProperties.contains(propName) || NodeUtil.isVarOrSimpleAssignLhs(parent, gramps);
      if (!isValidProperty) {
        return false;
      }
      validProperties.add(propName);
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
      // Check for self-referential assignments
      boolean isSelfReferential = refs.stream()
          .anyMatch(t -> t.getParent().equals(childVal));
      if (isSelfReferential) {
        return false;
      }
    }

    ret = true; // Found an acceptable object literal assignment, so set the result to true
  }
  return ret;
}