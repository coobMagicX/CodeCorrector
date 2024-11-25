private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore indirect references, like x.y (except x.y(), since
    // the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }
      continue; // Short-circuit to next iteration as no further checks are needed for get properties
    }

    // Only rewrite VAR declarations or simple assignment statements
    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue; // Skip variables with no assignments
    }

    // We're looking for object literal assignments only.
    if (!val.isObjectLit()) {
      return false;
    }

    // Make sure that the value is not self-refential. IOW,
    // disallow things like x = {b: x.a}. Also, ensure we handle all children
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false; // ES5 get/set not supported.
      }

      Node childVal = child.getFirstChild();
      if (isSelfReferential(childVal, refs)) { // Check for self-referential assignments
        return false;
      }
    }

    // If we reach here, it means the object literal is inlinable
    ret = true;
  }
  return ret;
}

// Helper method to check for self-referential assignments
private boolean isSelfReferential(Node node, List<Reference> refs) {
  for (Node childVal : NodeUtil.getAllDescendants(node)) { // Recursively get all descendants
    for (Reference ref : refs) {
      if (isSelfReferral(childVal, ref.getNode())) {
        return true; // If any descendant is a self-referral, the object literal has self-references
      }
    }
  }
  return false;
}

// Helper method to determine if there's a self-referral in the reference list
private boolean isSelfReferral(Node childVal, Node targetNode) {
  if (childVal == targetNode) {
    return true; // Target node is a direct referral
  }
  Node parentNode = childVal.getParent();
  while (parentNode != null && parentNode != targetNode) { // Traverse parent nodes up to the target node or stop at the top
    parentNode = parentNode.getParent();
  }
  return parentNode == targetNode; // If the last parent node equals targetNode, there's a referral
}