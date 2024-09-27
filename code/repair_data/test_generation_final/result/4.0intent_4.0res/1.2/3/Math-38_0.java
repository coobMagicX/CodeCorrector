private void prelim(double[] lowerBound, double[] upperBound) {
    printMethod(); // XXX

    final int n = currentBest.getDimension();
    final int npt = numberOfInterpolationPoints;
    final int ndim = bMatrix.getRowDimension();

    final double rhosq = initialTrustRegionRadius * initialTrustRegionRadius;
    final double recip = 1d / rhosq;
    final int np = n + 1;

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
        for (int j = 0; j < np; j++) { // Correct the max limit for ZMAT initialization
            zMatrix.setEntry(k, j, ZERO);
        }
    }

    int ipt = 0;
    int jpt = 0;
    double fbeg = Double.NaN;
    for (int nfm = 0; nfm < npt; nfm++) { // Start the loop directly to handle all points
        final int nfx = nfm - n;
        final int nfmm = nfm - 1;
        final int nfxm = nfx - 1;
        double stepa = 0;
        double stepb = 0;
        if (nfm <= 2 * n) {
            if (nfm >= 1 && nfm <= n) {
                stepa = initialTrustRegionRadius;
                if (upperDifference.getEntry(nfmm) == ZERO) {
                    stepa = -stepa;
                }
                interpolationPoints.setEntry(nfm, nfmm, stepa);
            } else if (nfm > n) {
                stepa = interpolationPoints.getEntry(nfx, nfxm);
                stepb = -initialTrustRegionRadius;
                if (lowerDifference.getEntry(nfxm) == ZERO) {
                    stepb = Math.min(TWO * initialTrustRegionRadius, upperDifference.getEntry(nfxm));
                }
                if (upperDifference.getEntry(nfxm) == ZERO) {
                    stepb = Math.max(-TWO * initialTrustRegionRadius, lowerDifference.getEntry(nfxm));
                }
                interpolationPoints.setEntry(nfm, nfxm, stepb);
            }
        } else {
            final int tmp1 = (nfm - np) / n;
            jpt = nfm - tmp1 * n - n;
            ipt = jpt + tmp1;
            if (ipt > n) {
                ipt = n;
            }
            final int iptMinus1 = ipt - 1;
            final int jptMinus1 = jpt - 1;
            interpolationPoints.setEntry(nfm, iptMinus1, interpolationPoints.getEntry(ipt, iptMinus1));
            interpolationPoints.setEntry(nfm, jptMinus1, interpolationPoints.getEntry(jpt, jptMinus1));
        }

        for (int j = 0; j < n; j++) {
            currentBest.setEntry(j, Math.min(Math.max(lowerBound[j], originShift.getEntry(j) + interpolationPoints.getEntry(nfm, j)), upperBound[j]));
        }

        final double objectiveValue = computeObjectiveValue(currentBest.toArray());
        final double f = isMinimize ? objectiveValue : -objectiveValue;
        fAtInterpolationPoints.setEntry(nfm, f);

        if (nfm == 0) {
            fbeg = f;
            trustRegionCenterInterpolationPointIndex = 0;
        } else if (f < fAtInterpolationPoints.getEntry(trustRegionCenterInterpolationPointIndex)) {
            trustRegionCenterInterpolationPointIndex = nfm;
        }
    }
} // prelim