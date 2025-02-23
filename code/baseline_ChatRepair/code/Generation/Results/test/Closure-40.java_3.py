public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null) {
        recordSet(ns.name, n);
      } else {
        throw new RuntimeException("NameInformation is null for node: " + n);
      }
    } else if (NodeUtil.isFunctionDeclaration(n)) {
      Node nameNode = n.getFirstChild();
      if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null) {
          JsName nameInfo = getName(nameNode.getString(), true);
          if (nameInfo != null) {
            recordSet(nameInfo.name, nameNode);
          } else {
            throw new RuntimeException("JsName is null for name node: " + nameNode);
          }
        } else {
          throw new RuntimeException("NameInformation is null for node: " + n);
        }
      } else {
        throw new RuntimeException("Expected name node as first child of function declaration.");
      }
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null) {
        recordSet(ns.name, n);
      } else {
        throw new RuntimeException("NameInformation is null for object literal key: " + n);
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null) {
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null) {
        if (ns.isPrototype) {
          recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
        } else {
          recordSet(ns.name, nameNode);
        }
      } else {
        throw new RuntimeException("NameInformation is null for assigned node: " + nameNode);
      }
    } else {
      throw new RuntimeException("Expected child node for assignment.");
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null && nameNode.isName()) {
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null && ns.onlyAffectsClassDef) {
        JsName name = getName(ns.name, false);
        if (name != null) {
          refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
        } else {
          throw new RuntimeException("JsName is null for defining class function node: " + nameNode);
        }
      }
    } else if (nameNode != null) {
      throw new RuntimeException("Expected name node for function call.");
    }
  }
}
