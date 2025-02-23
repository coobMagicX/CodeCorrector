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
        case Token.AND:
        case Token.OR:
            Node firstChild = val.getFirstChild();
            Node secondChild = firstChild.getNext();
            
            return isValidDefineValue(firstChild, defines) && isValidDefineValue(secondChild, defines);

        
        case Token.BITAND:
        case Token.BITOR:
        case Token.BITXOR:
            firstChild = val.getFirstChild();
            secondChild = firstChild.getNext();
            return isValidDefineValue(firstChild, defines) && isValidDefineValue(secondChild, defines);

        
        case Token.NOT:
        case Token.NEG:
        case Token