protected final double doSolve() throws MathIllegalArgumentException {
    // Existing code remains same...
    
    } else {
        switch (method) {
        case ILLINOIS:
            f0 *= 0.5;
            break;
        case PEGASUS:
            f0 *= f1 / (f1 + fx);
            break;
        default:
            // Instead of "should never happen", you should handle this case
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BRACKETING_PARAMETERS, x0, x1, f0, f1);
        }
    }

    // Remaining existing code...
}
