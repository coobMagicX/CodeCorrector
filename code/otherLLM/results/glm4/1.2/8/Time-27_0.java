private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        
        // Assuming finish method sets the separator's printer and parser
        sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep); // Removed duplicate separator reference
    }
    
    Object[] comp = createComposite(elementPairs);
    if (notPrinter) {
        return new PeriodFormatter(null, (PeriodParser) comp[1]);
    } else if (notParser) {
        return new PeriodFormatter((PeriodPrinter) comp[0], null);
    } else {
        // Since the methods provided do not involve a 'parse' operation and we are only 
        // modifying the code to prevent duplicate references to separators, there is no
        // need for the parse method here. We should ensure that the PeriodFormatter's 
        // constructor does not require a parser when notParser is true.
        return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
    }
}