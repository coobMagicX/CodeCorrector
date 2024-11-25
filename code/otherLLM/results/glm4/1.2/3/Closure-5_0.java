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
      
      // A call target may be using the object as a 'this' value.
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (!validProperties.contains(propName)) {
        boolean isVarOrSimpleAssignLhs = NodeUtil.isVarOrSimpleAssignLhs(parent, gramps);
        if (isVarOrSimpleAssignLhs) {
          validProperties.add(propName);
        } else {
          return false;
        }
      }
    } else if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      // A var with no assignment.
      continue;
    }

    if (!val.isObjectLit()) {
      return false;
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      String propName = child.getString();
      validProperties.add(propName);

      Node childVal = child.getFirstChild();
      // Check for self-referential assignments
      if (NodeUtil.containsSelfReference(childVal, refs)) {
        return false;
      }
    }
  }
  
  ret = true;
  return ret;
}