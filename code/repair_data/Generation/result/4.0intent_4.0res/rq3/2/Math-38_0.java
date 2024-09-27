private void prelim(double[] lowerBound, double[] upperBound) {
    printMethod(); // Debug method call

    final int n = currentBest.getDimension();
    final int npt = numberOfInterpolationPoints;
    final int ndim = bMatrix.getRowDimension();

    final double rhosq = initialTrustRegionRadius * initialTrustRegionRadius;
    final double recip = 1d / rhosq;
    final int np = n + 1;

    // Set XBASE to the initial vector of variables, and set the initial
    // elements of XPT, BMAT, HQ, PQ and ZMAT to zero.

    for (int j = 0; j < n; j++) {
        originShift.setEntry(j, currentBest.getEntry(j));
        for (int k = 0; k < npt; k++) {
            interpolationPoints.setEntry(k, j, ZERO);
        }
        for (int i = 0; i < ndim; i++) {
            bMatrix.setEntry(i, j, ZERO);
        }
    }
    for (int i = 0, max = n * np / 2; i < max; i++) {
        modelSecondDerivativesValues.setEntry(i, ZERO);
    }
    for (int k = 0; k < npt; k++) {
        modelSecondDerivativesParameters.setEntry(k, ZERO);
        for (int j = 0, max = npt - np; j < max; j++) {
            zMatrix.setEntry(k, j, ZERO);
        }
    }

    int ipt = 0;
    int jpt = 0;
    double fbeg = Double.NaN;
    do {
        final int nfm = getEvaluations();
        final int nfx = nfm - n;
        double stepa = 0;
        double stepb = 0;
        if (nfm <= 2 * n) {
            if (nfm >= 1 && nfm <= n) {
                stepa = initialTrustRegionRadius;
                if (upperBound[nfm - 1] == ZERO) {
                    stepa = -stepa;
                }
                interpolationPoints.setEntry(nfm - 1, nfm - 1, stepa);
            } else if (nfm > n) {
                stepa = interpolationPoints.getEntry(nfx - 1, nfx - 1);
                stepb = -initialTrustRegionRadius;
                if (lowerBound[nfx - 1] == ZERO) {
                    stepb = Math.min(TWO * initialTrustRegionRadius, upperBound[nfx - 1]);
                }
                if (upperBound[nfx - 1] == ZERO) {
                    stepb = Math.max(-TWO * initialTrustRegionRadius, lowerBound[nfx - 1]);
                }
                interpolationPoints.setEntry(nfm - 1, nfx - 1, stepb);
            }
        } else {
            final int tmp1 = (nfm - np) / n;
            jpt = nfm - tmp1 * n - n;
            ipt = jpt + tmp1;
            if (ipt > n) {
                final int tmp2 = jpt;
                jpt = ipt - n;
                ipt = tmp2;
            }
            interpolationPoints.setEntry(nfm - 1, ipt - 1, interpolationPoints.getEntry(ipt - 1, ipt - 1));
            interpolationPoints.setEntry(nfm - 1, jpt - 1, interpolationPoints.getEntry(jpt - 1, jpt - 1));
        }

        for (int j = 0; j < n; j++) {
            currentBest.setEntry(j, Math.min(Math.max(lowerBound[j],
                                                      originShift.getEntry(j) + interpolationPoints.getEntry(nfm - 1, j)),
                                             upperBound[j]));
        }

        final double objectiveValue = computeObjectiveValue(currentBest.toArray());
        final double f = isMinimize ? objectiveValue : -objectiveValue;
        fAtInterpolationPoints.setEntry(nfm - 1, f);

        if (nfm == 1) {
            fbeg = f;
            trustRegionCenterInterpolationPointIndex = 0;
        } else if (f < fAtInterpolationPoints.getEntry(trustRegionCenterInterpolationPointIndex)) {
            trustRegionCenterInterpolationPointIndex = nfm - 1;
        }
    } while (getEvaluations() < npt);
} // prelim