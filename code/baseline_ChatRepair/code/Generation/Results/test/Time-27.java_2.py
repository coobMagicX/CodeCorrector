private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }

    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        // If there is a Separator as the first element, apply it correctly with a subsequent formatter
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        // Fix: should use the finished separator, and construct PeriodFormatter correctly:
        sep = sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep.getPrinter(), sep.getParser());
    }

    // Build the PeriodFormatter based on the elements excluding the condition when the list starts with Separator
    if (notPrinter) {
        return new PeriodFormatter(null, buildParser(elementPairs));
    } else if (notParser) {
        return new PeriodFormatter(buildPrinter(elementPairs), null);
    } else {
        return new PeriodFormatter(buildPrinter(elementPairs), buildParser(elementPairs));
    }
}

private static PeriodPrinter buildPrinter(List<Object> elementPairs) {
    // Implement logic to build a PeriodPrinter from a list of elements
    // Placeholder for actual printer building logic
    return new PeriodPrinterImpl();
}

private static PeriodParser buildParser(List<Object> elementPairs) {
    // Implement logic to build a PeriodParser from a list of elements
    // Placeholder for actual parser building logic
    return new PeriodParserImpl();
}
