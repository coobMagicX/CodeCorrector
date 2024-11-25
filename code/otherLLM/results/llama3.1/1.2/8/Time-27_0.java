private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    
    // Validate separator usage
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        
        // Handle ISOPeriodFormat cases
        if ("ISO".equals(sep.getSeparator())) {
            PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
            sep = sep.finish(f.getPrinter(), f.getParser());
            
            // Special handling for standard ISO period format
            if (sep.isStandard()) {
                return new PeriodFormatter(sep, sep);
            } else {
                return toFormatter(elementPairs.subList(2, size), notPrinter, notParser); // Recursively try other formats
            }
        } else {
            PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
            sep = sep.finish(f.getPrinter(), f.getParser());
            return new PeriodFormatter(sep, sep);
        }
    }
    
    Object[] comp = createComposite(elementPairs);
    if (notPrinter) {
        return new PeriodFormatter(null, (PeriodParser) comp[1]);
    } else if (notParser) {
        return new PeriodFormatter((PeriodPrinter) comp[0], null);
    } else {
        return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
    }
}