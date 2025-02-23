public void visit(NodeTraversal t, Node n, Node parent) {
  try {
    // Record global variable and function declarations
    if (t.inGlobalScope()) {
      if (NodeUtil.isVarDeclaration(n)) {
        NameInformation ns = createNameInformation(t, n, parent);
        Preconditions.checkNotNull(ns, "NameInformation cannot be null");
        recordSet(ns.name, n);
      } else if (NodeUtil.isFunctionDeclaration(n)) {
        Node nameNode = n.getFirstChild();
        if (nameNode != null && nameNode.isName()) {
          NameInformation ns = createNameInformation(t, nameNode, n);
          if (ns != null) {
            JsName nameInfo = getName(nameNode.getString(), true);
            if (nameInfo != null) {
              recordSet(nameInfo.name, nameNode);
            }
          }
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
      if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null) {
          if (ns.isPrototype) {
            recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
          } else {
            recordSet(ns.name, nameNode);
          }
        }
      }
    } else if (n.isCall()) {
      Node nameNode = n.getFirstChild();
      if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null && ns.onlyAffectsClassDef) {
          JsName name = getName(ns.name, false);
          if (name != null) {
            refNodes.add(new ClassDefiningFunctionNode(
                name, n, parent, parent.getParent()));
          }
        }
      }
    }
  } catch (Exception e) {
    throw new RuntimeException("An error occurred while visiting node: " + n, e);
  }
}
