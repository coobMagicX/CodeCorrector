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
        if (comp[0] instanceof PeriodPrinter && comp[1] instanceof PeriodParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        } else {
            // Ensure comp[0] and comp[1] are of the correct types for PeriodFormatter
            PeriodFormatter pfmt1 = new PeriodFormatterBuilder()
                .appendDays().appendSuffix(" day", " days").appendSeparator(", ")
                .appendHours().appendSuffix(" hour", " hours").appendSeparator(", ")
                .appendMinutes().appendSuffix(" minute", " minutes").appendSeparator(" and ")
                .appendSeconds().appendSuffix(" second", " seconds").toFormatter();

            PeriodFormatter pfmt2 = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" year", " years").appendSeparator(", ")
                .appendMonths().appendSuffix(" month", " months").appendSeparator(", ")
                .appendDays().appendSuffix(" day", " days").appendSeparator(" and ")
                .appendHours().appendSuffix(" hour", " hours").appendSeparator(", ")
                .appendMinutes().appendSuffix(" minute", " minutes").appendSeparator(", ")
                .appendSeconds().appendSuffix(" second", " seconds").toFormatter();

            return new PeriodFormatter[] {pfmt1, pfmt2};
        }
    }
}