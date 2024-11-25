private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    }

    // Repairing the recursive call to 'toFormatter' which creates an infinite loop.
    if (elementPairs == null || elementPairs.isEmpty()) {
        throw new IllegalArgumentException("Element pairs cannot be null or empty");
    }

    int size = elementPairs.size();
    if (size >= 2 && elementPairs.get(0) instanceof Separator) {
        Separator sep = (Separator) elementPairs.get(0);
        List<Object> subList = elementPairs.subList(1, size); // Corrected to start from index 1
        PeriodFormatter f = toFormatter(subList, notPrinter, notParser); // Corrected the recursive call

        if (f == null) {
            throw new IllegalStateException("The recursive formatter creation returned null");
        }

        sep = sep.finish(f.getPrinter(), f.getParser());
        return new PeriodFormatter(sep, sep);
    }

    Object[] comp = createComposite(elementPairs);

    if (notPrinter && notParser) {
        throw new IllegalStateException("Builder has created neither a printer nor a parser");
    } else if (notPrinter) {
        // Correcting the cast to PeriodPrinter for the Printer part
        return new PeriodFormatter(null, (PeriodParser) comp[1]);
    } else if (notParser) {
        // Correcting the cast to PeriodParser for the Parser part
        return new PeriodFormatter((PeriodPrinter) comp[0], null);
    } else {
        // Ensuring correct casting and not throwing ClassCastException by checking for proper types before casting.
        if (comp[0] instanceof PeriodPrinter && comp[1] instanceof PeriodParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        } else {
            throw new IllegalArgumentException("Composite elements must be instances of PeriodPrinter and PeriodParser");
        }
    }
}