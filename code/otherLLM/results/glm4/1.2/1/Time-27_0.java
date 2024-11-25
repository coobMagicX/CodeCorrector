private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        
        // Ensure that the recursive call to toFormatter properly handles sublists
        PeriodFormatter f = toFormatter(elementPairs.subList(1, size), notPrinter, notParser); // Adjusted sublist start index
        
        // Apply the separator's finish method correctly, ensuring it has a valid Printer and Parser
        sep = sep.finish(f.getPrinter(), f.getParser());
        
        // Create the PeriodFormatter with the correct separator and its subformatter
        return new PeriodFormatter(sep, sep);
    } else {
        Object[] comp = createComposite(elementPairs); // Assuming this method is defined elsewhere

        if (notPrinter) {
            return new PeriodFormatter(null, (PeriodParser) comp[1]);
        } else if (notParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], null);
        } else {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        }
    }
}