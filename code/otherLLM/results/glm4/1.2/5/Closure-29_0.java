private boolean isInlinableObject(List<Reference> refs) {
  if (refs == null || refs.isEmpty()) {
    return false;
  }
  
  boolean ret = true; // Initialize as true to allow inlining unless proven otherwise.
  
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node grandparent = ref.getGrandparent();

    // Ignore indirect references, like x.y (except x.y(), since
    // the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (grandparent.isCall()
          && grandparent.getFirstChild() == parent) {
        return false; // Found a self-referential assignment, disallow inlining.
      }
      
      Node val = ref.getAssignedValue();
      if (val != null && !val.isObjectLit()) {
        continue; // Not an object literal, continue with the next reference.
      }

      // Check for properties that might not be defined on the object literal.
      for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isGetterDef() || child.isSetterDef()) {
          return false; // ES5 get/set not supported, disallow inlining.
        }

        Node childVal = child.getFirstChild();
        // Check for self-referential assignments.
        for (Reference t : refs) {
          Node refNode = t.getParent();
          while (!NodeUtil.isStatementBlock(refNode)) {
            if (refNode == childVal) {
              return false; // Found a self-referential assignment, disallow inlining.
            }
            refNode = refNode.getParent();
          }
        }
      }

    } else if (!isVarOrAssignExprLhs(name)) {
      return false; // Not a valid variable or assignment expression on the left-hand side.
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue; // Variable with no assignment, skip this reference.
    }
    
    // Check for object literal assignments.
    if (!val.isObjectLit()) {
      return false; // Not an object literal assignment, disallow inlining.
    }

    // If all checks pass, we can inline the object literal.
  }
  
  return ret;
}