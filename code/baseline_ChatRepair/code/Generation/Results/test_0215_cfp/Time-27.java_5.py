private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();

    // Create composite objects earlier before processing special cases
    Object[] comp = createComposite(elementPairs);

    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        // Ensure we pass the correct sublist, skipping the separator itself
        PeriodFormatter f = toFormatter(elementPairs.subList(1, size), notPrinter, notParser);
        // Finish the separator with already processed formatter components
        sep = sep.finish(f.getPrinter(), f.getParser());
        // Include separator correctly in the resulting formatter
        return new PeriodFormatter(sep, sep);
    }

    if (notPrinter) {
        return new PeriodFormatter(null, (PeriodParser) comp[1]);
    } else if (notParser) {
        return new PeriodFormatter((PeriodPrinter) comp[0], null);
    } else {
        return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
    }
}
