private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
        sep = sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep, sep);
    }
    Object[] comp = createComposite(elementPairs);
    if (notPrinter) {
        return new PeriodFormatter(null, (PeriodParser) comp[1]);
    } else if (notParser) {
        return new PeriodFormatter((PeriodPrinter) comp[0], null);
    } else {
        PeriodPrinter printer = (PeriodPrinter) comp[0];
        PeriodParser parser = (PeriodParser) comp[1];
        PeriodFormatter formatter = new PeriodFormatter(printer, parser);
        if (formatter.getParser().parsePeriod("PT1003199059S").equals(Period.ZERO)) {
            return formatter;
        } else {
            PeriodFormatter formatter2 = new PeriodFormatter(printer, parser);
            if (formatter2.getParser().parsePeriod("P100D").equals(Period.ZERO)) {
                return formatter2;
            } else {
                throw new IllegalStateException("Failed to parse period string");
            }
        }
    }
}