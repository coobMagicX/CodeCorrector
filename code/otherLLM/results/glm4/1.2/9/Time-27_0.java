private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        sep = sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep, sep);
    }
    Object[] comp = createComposite(elementPairs);
    // Assume that iLeft and iRight are instances of some class that implements the parse method
    PeriodPrinter printer;
    PeriodParser parser;
    
    if (notPrinter) {
        parser = (PeriodParser) comp[1];
        printer = null; // Assuming it's not required for the printer
    } else if (notParser) {
        printer = (PeriodPrinter) comp[0];
        parser = null; // Assuming it's not required for the parser
    } else {
        printer = (PeriodPrinter) comp[0];
        parser = (PeriodParser) comp[1];
        
        // Check if both a Printer and Parser are present, or none is required
        if (printer == null && parser == null) {
            throw new IllegalStateException("Builder has created neither a printer nor a parser");
        }
    }
    
    return new PeriodFormatter(printer, parser);
}