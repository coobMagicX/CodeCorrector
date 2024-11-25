public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      Preconditions.checkNotNull(ns);
      recordSet(ns.name, n);
    } else if (NodeUtil.isFunctionDeclaration(n)) {
      Node nameNode = n.getFirstChild();
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null) {
        JsName nameInfo = getName(nameNode.getString(), true);
        recordSet(nameInfo.name, nameNode);
      }
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null) {
        recordSet(ns.name, n);
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();

    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      if (ns.isPrototype) {
        recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
      } else {
        recordSet(ns.name, nameNode);
      }
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.onlyAffectsClassDef) {
      JsName name = getName(ns.name, false);
      if (name != null) {
        // Check for inheritance patterns and class definitions
        if (name.isClass()) {
          refNodes.add(new ClassDefiningFunctionNode(
              name, n, parent, parent.getParent()));
        }
      }
    }
  }

  // Additional logic for handling complex constructor calls involving inheritance
  if (n.isConstructor() && NodeUtil.hasSuperCall(n)) {
    Node superCall = NodeUtil.getSuperCall(n);
    NameInformation ns = createNameInformation(t, superCall, n);
    if (ns != null) {
      recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, superCall);
    }
  }
}

// Helper method to check for inheritance patterns
private boolean hasInheritancePattern(Node node) {
  // Implement the logic to check for inheritance patterns in the node and its children
  // This could involve checking constructor calls, prototype chains, etc.
  // For this example, let's assume we have a helper method that can tell us if there is an inheritance pattern:
  return NodeUtil.hasInheritancePattern(node);
}