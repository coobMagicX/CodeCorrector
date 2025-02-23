private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }

    int size = elementPairs.size();
    if (size >= 1 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = null;
        if (size > 1) {
            // We start sublist immediately after separator to ensure all elements are considered.
            f = toFormatter(elementPairs.subList(1, size), notPrinter, notParser);
            sep = sep.finish(f.getPrinter(), f.getParser());
        }
        if (f == null) {
            throw new IllegalArgumentException("No PeriodFormatter elements found after Separator");
        }
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
