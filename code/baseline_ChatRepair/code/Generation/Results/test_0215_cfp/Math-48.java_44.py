protected final double doSolve() {
    // Initial setup and accuracy configurations as before.

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        // Regula Falsi Method should be more robust to ensure intervals update meaningfully.
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            if (method == REGULA_FALSI) {
                // Avoid division by very small number and ensure method progresses by using a small δ
                final double delta = 1e-10 * (x1 - x0);
                if (Math.abs(fx - f1) < delta) {
                    x1 = x0 + (x1 - x0) / 2;
                }
            }
        }

        x1 = x;
        f1 = fx;

        // Check for function accuracy or iteration bounds, and update as needed.
        // Code continues to handle allowed solution sides and accuracy checks.
        
        if (FastMath.abs(f1) <= ftol) {
            // Handle root approximation accuracy satisfactions.
            // Choose return based on allowed sides.
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Handle absolute and relative tolerances.
        }
    }
}
