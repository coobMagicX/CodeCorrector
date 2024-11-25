private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore most indirect references, like x.y (but not x.y(),
    // since the function referenced by y might reference 'this').
    //
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      // A call target may be using the object as a 'this' value.
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (!validProperties.contains(propName)) {
        validProperties.add(propName);
      } else {
        // If the property is already in the set, it means we've seen
        // a self-referential assignment. However, this doesn't necessarily
        // mean that the object literal can't be inlined. We need to check
        // if all properties in the object literal are valid.
        boolean isValid = true;
        for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
          String childPropName = child.getLastChild().getString();
          if (!validProperties.contains(childPropName)) {
            isValid = false;
            break;
          }
        }
        // If all properties are valid, then the object literal can be inlined.
        if (isValid) {
          continue;
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

    // Make sure that the value is not self-referential. IOW,
    // disallow things like x = {b: x.a}.
    //
    // TODO: Only exclude unorderable self-referential
    // assignments. i.e. x = {a: x.b, b: x.a} is not orderable,
    // but x = {a: 1, b: x.a} is.
    for (Node child = val.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isGetterDef() ||
          child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      validProperties.add(child.getString());

      Node childVal = child.getFirstChild();
      // Check if childVal is the parent of any of the passed in
      // references, as that is how self-referential assignments
      // will happen.
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            // There's a self-referential assignment
            return false;
          }
          refNode = refNode.getParent();
        }
      }
    }


    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}