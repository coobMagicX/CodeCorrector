private static boolean isReduceableFunctionExpression(Node n) {
  return !NodeUtil.isFunctionExpression(n) || 
         (NodeUtil.hasPropertyGetter(n) && NodeUtil.getPropertyGettersCount(n) == 1);
}

public boolean hasPropertyGetter(Node n) {
    int count = 0;
    for (Node child : n.getChildNodes()) {
        if (child.getType() == Node.PROP_GETTER) {
            count++;
        }
    }
    return count > 0;
}

public int getPropertyGettersCount(Node n) {
    int count = 0;
    for (Node child : n.getChildNodes()) {
        if (child.getType() == Node.PROP_GETTER) {
            count++;
        }
    }
    return count;
}