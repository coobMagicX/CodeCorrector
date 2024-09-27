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
        recordSet(ns.name, nameNode);
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
    if ("goog.inherits".equals(nameNode.getQualifiedName())) {
      Node childClassNode = nameNode.getNext();
      Node parentClassNode = childClassNode.getNext();
      if (childClassNode != null && parentClassNode != null) {
        JsName childClassName = getName(childClassNode.getQualifiedName(), true);
        JsName parentClassName = getName(parentClassNode.getQualifiedName(), true);
        if (childClassName != null && parentClassName != null) {
          refNodes.add(new InheritanceDefiningNode(childClassName, parentClassName, n, parent));
        }
      }
    } else {
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null && ns.onlyAffectsClassDef) {
        JsName name = getName(ns.name, false);
        if (name != null) {
          refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
        }
      }
    }
  }
}