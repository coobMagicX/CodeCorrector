static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    
    case Token.ADD:
    case Token.SUB:
    case Token.MUL:
    case Token.DIV:
    case Token.MOD:
      Node leftChild = val.getFirstChild();
      Node rightChild = leftChild.getNext(); 
      return isValidDefineValue(leftChild, defines) && isValidDefineValue(rightChild, defines);

    
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      leftChild = val.getFirstChild();
      rightChild = leftChild.getNext(); 
      return isValidDefineValue(leftChild, defines) && isValidDefineValue(rightChild, defines);

    
    case Token.NOT:
    case Token.BITNOT: 