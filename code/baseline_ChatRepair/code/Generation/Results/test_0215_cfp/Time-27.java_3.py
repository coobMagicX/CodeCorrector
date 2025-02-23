private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        // Increment sublist starting index to bypass the separator for internal conversion
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        // Finish building the separator with the obtained formatter's printer and parser
        sep = sep.finish(f.getPrinter(), f.getParser());
        // Ensure the resulting formatter uses both, the separator as well as the components after the separator without repeating.
        return new PeriodFormatter(new CompositePeriodPrinter(new PeriodPrinter[]{sep, f.getPrinter()}),
                                   new CompositePeriodParser(new PeriodParser[]{sep, f.getParser()}));
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
