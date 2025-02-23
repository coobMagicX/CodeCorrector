import java.util.HashSet;
import java.util.Stack;

private void traverse(Node node) {
  HashSet<Node> visited = new HashSet<Node>();
  
  // Using a stack to explicitly manage the traversal instead of recursion
  Stack<Node> stack = new Stack<Node>();
  stack.push(node);

  while (!stack.isEmpty()) {
    Node currentNode = stack.peek();
    
    if (visited.contains(currentNode) && !shouldRetraverse(currentNode)) {
      stack.pop();
      exitNode(currentNode);
    } else {    
      // Either it's unvisited node, or it should be retraversed
      if (!visited.contains(currentNode)) {
        // First time visit
        visited.add(currentNode);
        
        // Push all children of this node to stack for future traversal
        Node child = currentNode.getFirstChild();
        while (child != null) {
          if (shouldVisit(child)) {
            stack.push(child);
          }
          child = child.getNext();
        }
      }

      // Visit the current node
      visit(currentNode);
    }
  }
}
