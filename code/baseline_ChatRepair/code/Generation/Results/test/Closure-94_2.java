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
            
            return isValidDefineValue(val.getFirstChild(), defines) && 
                   isValidDefineValue(val.getFirstChild().getNext(), defines);

        
        case Token.BITAND:
        case Token.BITOR:
        case Token.BITXOR:
            
            return isValidDefineValue(val.getFirstChild(), defines) && 
                   isValidDefineValue(val.getFirstChild().getNext(), defines);

        
        case Token.NOT:
        case Token.NEG:
        case Token.BITNOT:
            
            return isValidDefineValue(val.getFirstChild(), defines);

        