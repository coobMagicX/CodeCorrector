public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
    
    // Ensure that the renaming is done only once
    if (!renamer.isVisited(root)) {
      reportCodeChange("Duplicate declaration after renaming names");
    }
  }

  removeDuplicateDeclarations(root);

  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}

// You may need to add a method in the MakeDeclaredNamesUnique class if it does not exist
class MakeDeclaredNamesUnique {
  // Assuming there is an instance variable to keep track of visited nodes
  private Set<Node> visitedNodes = new HashSet<>();

  public void traverseRoots(Node externs, Node root) {
    traverse(root);
  }

  private void traverse(Node node) {
    if (node == null || visitedNodes.contains(node)) {
      return;
    }
    
    // Mark the current node as visited
    visitedNodes.add(node);

    // Traverse children
    for (Node child : node.children()) {
      traverse(child);
    }
    
    // Perform renaming logic here
    // ...
  }

  public boolean isVisited(Node node) {
    return visitedNodes.contains(node);
  }
}