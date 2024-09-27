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
    if (nameNode.isName() && nameNode.getString().equals("goog.inherits")) {
      // Handle the inheritance pattern specifically
      Node childCtor = nameNode.getNext();
      Node parentCtor = childCtor.getNext();
      NameInformation childNs = createNameInformation(t, childCtor, n);
      NameInformation parentNs = createNameInformation(t, parentCtor, n);
      if (childNs != null && parentNs != null) {
        // Record the inheritance relationship
        JsName childName = getName(childNs.name, true);
        JsName parentName = getName(parentNs.name, true);
        recordInheritance(childName, parentName, n);
      }
    } else {
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
}

private void recordInheritance(JsName childName, JsName parentName, Node node) {
  InheritanceRecordNode irn = new InheritanceRecordNode(childName, parentName, node);
  refNodes.add(irn);
}