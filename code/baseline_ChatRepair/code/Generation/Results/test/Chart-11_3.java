public static boolean equal(GeneralPath p1, GeneralPath p2) {
    if (p1 == null) {
        return p2 == null;
    }
    if (p2 == null) {
        return false;
    }
    
    if (p1.getWindingRule() != p2.getWindingRule()) {
        return false;
    }

    
    PathIterator iterator1 = p1.getPathIterator(null);
    PathIterator iterator2 = p2.getPathIterator(null);

    
    double[] coords1 = new double[6];
    double[] coords2 = new double[6];

    
    while (!iterator1.isDone() || !iterator2.isDone()) {
        if (iterator1.isDone() != iterator2.isDone()) {
            return false;  
        }
        
        int type1 = iterator1.currentSegment(coords1);
        int type2 = iterator2.currentSegment(coords2);
        
        if (type1 != type2) {
            return false; 
        }

        
        if (!Arrays.equals(coords1, coords2)) {
            return false; 
        }

        
        iterator1.next();
        iterator2