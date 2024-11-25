private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(1, size), notPrinter, notParser); // Corrected the sublist start index
        sep = sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep, sep);
    }
    
    Object[] comp = createComposite(elementPairs);
    if (notPrinter) {
        PeriodParser parser = (PeriodParser) comp[1];
        if (parser == null) {
            throw new IllegalStateException("No parser provided for printer only formatter");
        }
        return new PeriodFormatter(null, parser);
    } else if (notParser) {
        PeriodPrinter printer = (PeriodPrinter) comp[0];
        if (printer == null) {
            throw new IllegalStateException("No printer provided for parser only formatter");
        }
        return new PeriodFormatter(printer, null);
    } else {
        PeriodPrinter printer = (PeriodPrinter) comp[0];
        PeriodParser parser = (PeriodParser) comp[1];
        if (printer == null || parser == null) {
            throw new IllegalStateException("Both a printer and a parser must be provided");
        }
        return new PeriodFormatter(printer, parser);
    }
}