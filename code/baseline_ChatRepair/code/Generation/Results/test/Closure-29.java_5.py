private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  outerloop:
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
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

    HashSet<Node> analyzedNodes = new HashSet<>();
    HashSet<Node> selfRefs = new HashSet<>();
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }

      Node childVal = child.getFirstChild();
      analyzeNodeDeep(childVal, refs, analyzedNodes, selfRefs);
    }

    if (!selfRefs.isEmpty()) {
      return false;
    }

    ret = true;
  }
  return ret;
}

private void analyzeNodeDeep(Node node, List<Reference> refs, Set<Node> analyzedNodes, Set<Node> selfRefs) {
  if (analyzedNodes.contains(node)) {
    selfRefs.add(node);
    return;
  }
  analyzedNodes.add(node);
  
  for (Reference t : refs) {
    Node refNode = t.getParent();
    while (!NodeUtil.isStatementBlock(refNode)) {
      if (refNode == node) {
        selfRefs.add(node);
        return;
      }
      refNode = refNode.getParent();
    }
  }
  
  for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
    analyzeNodeDeep(child, refs, analyzedNodes, selfRefs);
  }
}
