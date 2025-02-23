private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  // Track assigned object literal to ensure only one unique object literal is assigned.
  Node potentialObjectLit = null;

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

    if (potentialObjectLit != null && potentialObjectLit != val) {
      // Multiple different assignments, cannot inline safely.
      return false;
    }
    potentialObjectLit = val;

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }

      ReferencesFinder finder = new ReferencesFinder(child);
      List<Reference> childRefs = finder.findReferences(refs);

      if (childRefs.contains(ref)) {
        return false;
      }
    }

    ret = true;  // found at least one valid object literal
  }
  return ret;
}

// Helper method to check variable references within the object literal
class ReferencesFinder {
  Node target;

  public ReferencesFinder(Node target) {
    this.target = target;
  }

  public List<Reference> findReferences(List<Reference> refs) {
    List<Reference> found = new ArrayList<>();
    for (Reference ref : refs) {
      Node testNode = ref.getNode();
      while (testNode != null) {
        if (testNode == target) {
          found.add(ref);
          break;
        }
        testNode = testNode.getParent();
      }
    }
    return found;
  }
}
