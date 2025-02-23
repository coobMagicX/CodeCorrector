private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0 instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        // Fix: Adjust sublist index to skip over Separator when recursing.
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        // Retrieve both printer and parser from Separator after finishing.
        if (!notPrinter && !notParser) {
            sep = sep.finish(f.getPrinter(), f.getParser());
        }
        return new PeriodFormatter(sep, sep);
    } else if (size > 1) {
        Object[] comp = createComposite(elementPairs);
        if (notPrinter) {
            return new PeriodFormatter(null, (PeriodParser) comp[1]);
        } else if (notParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], null);
        } else {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        }
    } else {
        // In case there's only one or zero elements, handle appropriately:
        // Typically handle as a trivial case or error
        throw new IllegalArgumentException("Invalid period format specification or inadequate elements for formatter.");
    }
}
