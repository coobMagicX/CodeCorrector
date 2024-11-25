private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    
    // Parameter validation: Ensure that the list is not empty
    if (size == 0) {
        throw new IllegalArgumentException("Input list cannot be empty");
    }
    
    // PeriodParser/PeriodPrinter creation: Verify correct creation of instances
    Object[] comp = createComposite(elementPairs);
    
    PeriodPrinter printer;
    PeriodParser parser;
    if (!notParser) {
        parser = (PeriodParser) comp[1];
        if (size >= 2 && elementPairs.get(0) instanceof Separator) {
            // Separator handling: Ensure correct separator logic
            Separator sep = (Separator) elementPairs.get(0);
            printer = sep.finish((PeriodPrinter) comp[0], parser);
        } else {
            printer = (PeriodPrinter) comp[0];
        }
    } else if (!notPrinter) {
        printer = (PeriodPrinter) comp[0];
        if (size >= 2 && elementPairs.get(1) instanceof Separator) {
            // Separator handling: Ensure correct separator logic
            Separator sep = (Separator) elementPairs.get(1);
            parser = sep.finish(printer, (PeriodParser) comp[1]);
        } else {
            parser = (PeriodParser) comp[1];
        }
    } else {  // Both notPrinter and notParser are true
        printer = null;
        parser = null;
    }
    
    return new PeriodFormatter(printer, parser);
}