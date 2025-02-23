private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = new HashSet<>();

  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    
    if (parent.isGetProp() && parent.getFirstChild() == name) {
      String propName = parent.getLastChild().getString();
      
      // Check for property delete operation which should inhibit inlining
      if (refs.stream().anyMatch(r -> isDeleteOperationOnProperty(r, propName))) {
        return false;
      }
      
      // Only inline properties that are assigned directly to the object.
      if (isSimpleAssignToProperty(parent, ref.getGrandparent())) {
        validProperties.add(propName);
      } else {
        return false;
      }
    } else if (!isVariableDeclarationOrAssignment(name)) {
      return false;
    }

    Node assignedValue = ref.getAssignedValue();
    if (assignedValue != null && assignedValue.isObjectLit()) {
      if (isSelfReferential(assignedValue, refs)) {
        return false;
      }
      extractValidProperties(assignedValue, validProperties);
      ret = true;
    }
  }
  
  return ret;
}

private boolean isDeleteOperationOnProperty(Reference ref, String propName) {
  Node node = ref.getNode().getParent();
  while (node != null) {
    if (node.isDelProp() && node.getLastChild().getString().equals(propName)) {
      return true;
    }
    node = node.getParent();
  }
  return false;
}

private boolean isSimpleAssignToProperty(Node parent, Node grandparent) {
  return NodeUtil.isAssign(grandparent) && grandparent.getFirstChild() == parent;
}

private boolean isVariableDeclarationOrAssignment(Node node) {
  return NodeUtil.isName(node) || (NodeUtil.isAssign(node) && NodeUtil.isName(node.getFirstChild()));
}

private boolean isSelfReferential(Node value, List<Reference> refs) {
  for (Node child = value.getFirstChild(); child != null; child = child.getNext()) {
    Node childValue = child.getFirstChild();
    for (Reference ref : refs) {
      if (isAncestor(childValue, ref.getNode())) {
        return true;
      }
    }
  }
  return false;
}

private void extractValidProperties(Node value, Set<String> validProperties) {
  for (Node child = value.getFirstChild(); child != null; child = child.getNext()) {
    validProperties.add(child.getString());
  }
}

private boolean isAncestor(Node ancestor, Node descendant) {
  while (descendant != null && descendant != ancestor) {
    descendant = descendant.getParent();
  }
  return descendant == ancestor;
}
