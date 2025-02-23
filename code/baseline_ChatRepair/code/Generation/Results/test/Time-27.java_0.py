private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    // We start checking if the first element is Separator from index 0
    for (int i = 0; i < size; i++) {
        if (elementPairs.get(i) instanceof Separator) {
            Separator sep = (Separator) elementPairs.get(i);
            if (i + 1 < size) {
                // Create formatter from the rest of the list after this Separator
                PeriodFormatter f = toFormatter(elementPairs.subList(i + 2, size), notPrinter, notParser);
                sep = sep.finish(f.getPrinter(), f.getParser());
                return new PeriodFormatter(sep, sep);
            }
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
