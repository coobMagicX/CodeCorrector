private boolean isInlinableObject(List<Reference> refs) {
  boolean isInlineable = false;
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

      if (containsReference(childRefs, ref)) {
        return false;
      }
    }

    isInlineable = true;  // at least one valid inlineable object literal found
  }
  return isInlineable;
}

private boolean containsReference(List<Reference> references, Reference ref) {
  for (Reference foundRef : references) {
    if (foundRef == ref) {
      return true;
    }
  }
  return false;
}

class ReferencesFinder {
  Node target;

  public ReferencesFinder(Node target) {
    this.target = target;
  }

  public List<Reference> findReferences(List<Reference> refs) {
    List<Reference> found = new ArrayList<Reference>();  // specify type explicitly
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
