private void prelim(double[] lowerBound,
                    double[] upperBound) {
    printMethod(); // XXX

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

    // Begin the initialization procedure. NF becomes one more than the number
    // of function values so far. The coordinates of the displacement of the
    // next initial interpolation point from XBASE are set in XPT(NF+1,.).

    int ipt = 0;
    int jpt = 0;
    double fbeg = Double.NaN;
    do {
        final int nfm = getEvaluations();
        final int nfx = nfm - n;
        final int nfmm = nfm - 1;
        final int nfxm = nfx - 1;
        double stepa = 0;
        double stepb = 0;
        if (nfm <= 2 * n) {
            if (nfm >= 1 &&
                nfm <= n) {
                stepa = initialTrustRegionRadius;
                if (upperDifference.getEntry(nfmm) == ZERO) {
                    stepa = -stepa;
                    throw new PathIsExploredException(); // XXX
                }
                interpolationPoints.setEntry(nfm, nfmm, stepa);
            } else if (nfm > n) {
                stepa = interpolationPoints.getEntry(nfx, nfxm);
                stepb = -initialTrustRegionRadius;
                if (lowerDifference.getEntry(nfxm) == ZERO) {
                    stepb = Math.min(TWO * initialTrustRegionRadius, upperDifference.getEntry(nfxm));
                    throw new PathIsExploredException(); // XXX
                }
                if (upperDifference.getEntry(nfxm) == ZERO) {
                    stepb = Math.max(-TWO * initialTrustRegionRadius, lowerDifference.getEntry(nfxm));
                    throw new PathIsExploredException(); // XXX
                }
                interpolationPoints.setEntry(nfm, nfxm, stepb);
            }
        } else {
            final int tmp1 = (nfm - np) / n;
            jpt = nfm - tmp1 * n - n;
            ipt = jpt + tmp1;
            if (ipt > n) {
                final int tmp2 = jpt;
                jpt = ipt - n;
                ipt = tmp2;
                throw new PathIsExploredException(); // XXX
            }
            final int iptMinus1 = ipt;
            final int jptMinus1 = jpt;
            interpolationPoints.setEntry(nfm, iptMinus1, interpolationPoints.getEntry(ipt, iptMinus1));
            interpolationPoints.setEntry(nfm, jptMinus1, interpolationPoints.getEntry(jpt, jptMinus1));
        }

        // Calculate the next value of F. The least function value so far and
        // its index are required.

        for (int j = 0; j < n; j++) {
            currentBest.setEntry(j, Math.min(Math.max(lowerBound[j],
                                                      originShift.getEntry(j) + interpolationPoints.getEntry(nfm, j)),
                                             upperBound[j]));
            if (interpolationPoints.getEntry(nfm, j) == lowerDifference.getEntry(j)) {
                currentBest.setEntry(j, lowerBound[j]);
            }
            if (interpolationPoints.getEntry(nfm, j) == upperDifference.getEntry(j)) {
                currentBest.setEntry(j, upperBound[j]);
            }
        }

        final double objectiveValue = computeObjectiveValue(currentBest.toArray());
        final double f = isMinimize ? objectiveValue : -objectiveValue;
        final int numEval = getEvaluations(); // nfm + 1
        fAtInterpolationPoints.setEntry(nfm, f);

        if (numEval == 1) {
            fbeg = f;
            trustRegionCenterInterpolationPointIndex = 0;
        } else if (f < fAtInterpolationPoints.getEntry(trustRegionCenterInterpolationPointIndex)) {
            trustRegionCenterInterpolationPointIndex = nfm;
        }

        // Set the nonzero initial elements of BMAT and the quadratic model in the
        // cases when NF is at most 2*N+1. If NF exceeds N+1, then the positions
        // of the NF-th and (NF-N)-th interpolation points may be switched, in
        // order that the function value at the first of them contributes to the
        // off-diagonal second derivative terms of the initial quadratic model.

        if (numEval <= 2 * n + 1) {
            if (numEval >= 2 &&
                numEval <= n + 1) {
                gradientAtTrustRegionCenter.setEntry(nfmm, (f - fbeg) / stepa);
                if (npt < numEval + n) {
                    final double oneOverStepA = ONE / stepa;
                    bMatrix.setEntry(0, nfmm, -oneOverStepA);
                    bMatrix.setEntry(nfm, nfmm, oneOverStepA);
                    bMatrix.setEntry(npt + nfmm, nfmm, -HALF * rhosq);
                    throw new PathIsExploredException(); // XXX
                }
            } else if (numEval >= n + 2) {
                final int ih = nfx * (nfx + 1) / 2 - 1;
                final double tmp = (f - fbeg) / stepb;
                final double diff = stepb - stepa;
                modelSecondDerivativesValues.setEntry(ih, TWO * (tmp - gradientAtTrustRegionCenter.getEntry(nfxm)) / diff);
                gradientAtTrustRegionCenter.setEntry(nfxm, (gradientAtTrustRegionCenter.getEntry(nfxm) * stepb - tmp * stepa) / diff);
                if (stepa * stepb < ZERO) {
                    if (f < fAtInterpolationPoints.getEntry(nfm - n)) {
                        fAtInterpolationPoints.setEntry(nfm, fAtInterpolationPoints.getEntry(nfm - n));
                        fAtInterpolationPoints.setEntry(nfm - n, f);
                        if (trustRegionCenterInterpolationPointIndex == nfm) {
                            trustRegionCenterInterpolationPointIndex = nfm - n;
                        }
                        interpolationPoints.setEntry(nfm - n, nfxm, stepb);
                        interpolationPoints.setEntry(nfm, nfxm, stepa);
                    }
                }
                bMatrix.setEntry(0, nfxm, -(stepa + stepb) / (stepa * stepb));
                bMatrix.setEntry(nfm, nfxm, -HALF / interpolationPoints.getEntry(nfm - n, nfxm));
                bMatrix.setEntry(nfm - n, nfxm,
                              -bMatrix.getEntry(0, nfxm) - bMatrix.getEntry(nfm, nfxm));
                zMatrix.setEntry(0, nfxm, Math.sqrt(TWO) / (stepa * stepb));
                zMatrix.setEntry(nfm, nfxm, Math.sqrt(HALF) / rhosq);
                zMatrix.setEntry(nfm - n, nfxm,
                              -zMatrix.getEntry(0, nfxm) - zMatrix.getEntry(nfm, nfxm));
            }

            // Set the off-diagonal second derivatives of the Lagrange functions and
            // the initial quadratic model.

        } else {
            zMatrix.setEntry(0, nfxm, recip);
            zMatrix.setEntry(nfm, nfxm, recip);
            zMatrix.setEntry(ipt, nfxm, -recip);
            zMatrix.setEntry(jpt, nfxm, -recip);

            final int ih = ipt * (ipt - 1) / 2 + jpt - 1;
            final double tmp = interpolationPoints.getEntry(nfm, ipt - 1) * interpolationPoints.getEntry(nfm, jpt - 1);
            modelSecondDerivativesValues.setEntry(ih, (fbeg - fAtInterpolationPoints.getEntry(ipt) - fAtInterpolationPoints.getEntry(jpt) + f) / tmp);
            throw new PathIsExploredException(); // XXX
        }
    } while (getEvaluations() < npt);
} // prelim

private void update(
        double beta,
        double denom,
        int knew
) {
    printMethod(); // XXX

    final int n = currentBest.getDimension();
    final int npt = numberOfInterpolationPoints;
    final int nptm = npt - n - 1;

    // XXX Should probably be split into two arrays.
    final ArrayRealVector work = new ArrayRealVector(npt + n);

    double ztest = ZERO;
    for (int k = 0; k < npt; k++) {
        for (int j = 0; j < nptm; j++) {
            // Computing MAX
            ztest = Math.max(ztest, Math.abs(zMatrix.getEntry(k, j)));
        }
    }
    ztest *= 1e-20;

    // Apply the rotations that put zeros in the KNEW-th row of ZMAT.

    for (int j = 1; j < nptm; j++) {
        final double d1 = zMatrix.getEntry(knew, j);
        if (Math.abs(d1) > ztest) {
            // Computing 2nd power
            final double d2 = zMatrix.getEntry(knew, 0);
            // Computing 2nd power
            final double d3 = zMatrix.getEntry(knew, j);
            final double d4 = Math.sqrt(d2 * d2 + d3 * d3);
            final double d5 = zMatrix.getEntry(knew, 0) / d4;
            final double d6 = zMatrix.getEntry(knew, j) / d4;
            for (int i = 0; i < npt; i++) {
                final double d7 = d5 * zMatrix.getEntry(i, 0) + d6 * zMatrix.getEntry(i, j);
                zMatrix.setEntry(i, j, d5 * zMatrix.getEntry(i, j) - d6 * zMatrix.getEntry(i, 0));
                zMatrix.setEntry(i, 0, d7);
            }
        }
        zMatrix.setEntry(knew, j, ZERO);
    }

    // Put the first NPT components of the KNEW-th column of HLAG into W,
    // and calculate the parameters of the updating formula.

    for (int i = 0; i < npt; i++) {
        work.setEntry(i, zMatrix.getEntry(knew, 0) * zMatrix.getEntry(i, 0));
    }
    final double alpha = work.getEntry(knew);
    final double tau = lagrangeValuesAtNewPoint.getEntry(knew);
    lagrangeValuesAtNewPoint.setEntry(knew, lagrangeValuesAtNewPoint.getEntry(knew) - ONE);

    // Complete the updating of ZMAT.

    final double sqrtDenom = Math.sqrt(denom);
    final double d1 = tau / sqrtDenom;
    final double d2 = zMatrix.getEntry(knew, 0) / sqrtDenom;
    for (int i = 0; i < npt; i++) {
        zMatrix.setEntry(i, 0,
                      d1 * zMatrix.getEntry(i, 0) - d2 * lagrangeValuesAtNewPoint.getEntry(i));
    }

    // Finally, update the matrix BMAT.

    for (int j = 0; j < n; j++) {
        final int jp = npt + j;
        work.setEntry(jp, bMatrix.getEntry(knew, j));
        final double d3 = (alpha * lagrangeValuesAtNewPoint.getEntry(jp) - tau * work.getEntry(jp)) / denom;
        final double d4 = (-beta * work.getEntry(jp) - tau * lagrangeValuesAtNewPoint.getEntry(jp)) / denom;
        for (int i = 0; i <= jp; i++) {
            bMatrix.setEntry(i, j,
                          bMatrix.getEntry(i, j) + d3 * lagrangeValuesAtNewPoint.getEntry(i) + d4 * work.getEntry(i));
            if (i >= npt) {
                bMatrix.setEntry(jp, (i - npt), bMatrix.getEntry(i, j));
            }
        }
    }
} // update