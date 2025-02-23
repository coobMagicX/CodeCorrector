private void traverse(Node node) {
  HashSet<Node> visited = new HashSet<>();
  
  // Using a stack to explicitly manage traversal instead of recursion
  Stack<Node> stack = new Stack<>();
  stack.push(node);

  while (!stack.isEmpty()) {
    Node currentNode = stack.peek();

    if (visited.contains(currentNode)) {
      // If the node has been completely processed, pop from stack
      stack.pop();
      exitNode(currentNode);
    } else {
      // Mark this node as visited
      visited.add(currentNode);
      
      // Push all children of this node to stack for traversal
      Node child = currentNode.getFirstChild();
      while (child != null) {
        if (!visited.contains(child) && shouldVisit(child)) {
          stack.push(child);
        }
        child = child.getNext();
      }
      
      // Visit current node
      visit(currentNode);

      // Post visit check to see if we should retraverse this node
      if (shouldRetraverse(currentNode)) {
        visited.remove(currentNode); // Allow this node to be re-visited
      }
    }
  }
}
