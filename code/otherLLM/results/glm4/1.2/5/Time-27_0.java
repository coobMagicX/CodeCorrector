private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(1, size), notPrinter, notParser); // Adjusted the sublist start index
        sep = sep.finish(f.getPrinter(), f.getParser()); // Ensure finish is using the correct methods from f
        return new PeriodFormatter(sep, sep);
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