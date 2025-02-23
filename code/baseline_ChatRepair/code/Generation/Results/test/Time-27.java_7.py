private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }

    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        // Ensure sublist starts properly after the separator.
        if (size > 2) {
            PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
            sep = sep.finish(f.getPrinter(), f.getParser());
        } else {
            // Handle the case where there might not be enough elements after the separator
            throw new IllegalArgumentException("Insufficient elements after separator to form a valid PeriodFormatter");
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
