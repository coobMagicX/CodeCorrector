private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }
    int size = elementPairs.size();
    
    // Ensure that the Separator correctly utilizes the custom format structures
    if (size > 0 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        // Ensure when calling toFormatter recursively, the correct sublist is used
        PeriodFormatter innerFormatter = toFormatter(elementPairs.subList(1, size), notPrinter, notParser);
        
        // Correct handling of the separator formatting with respective Finish method
        sep = sep.finish(innerFormatter.getPrinter(), innerFormatter.getParser());
        return new PeriodFormatter(sep, sep);
    } else {
        // If not dealing with separators, simply create a composite formatter
        Object[] comp = createComposite(elementPairs);
        if (notPrinter) {
            return new PeriodFormatter(null, (PeriodParser) comp[1]);
        } else if (notParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], null);
        } else {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        }
    }
}
