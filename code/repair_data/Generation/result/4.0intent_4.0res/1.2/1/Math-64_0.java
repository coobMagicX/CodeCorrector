protected VectorialPointValuePair doOptimize()
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    // arrays shared with the other private methods
    solvedCols  = Math.min(rows, cols);
    diagR       = new double[cols];
    jacNorm     = new double[cols];
    beta        = new double[cols];
    permutation = new int[cols];
    lmDir       = new double[cols];

    // local point
    double   delta   = 0;
    double   xNorm   = 0;
    double[] diag    = new double[cols];
    double[] oldX    = new double[cols];
    double[] oldRes  = new double[rows];
    double[] work1   = new double[cols];
    double[] work2   = new double[cols];
    double[] work3   = new double[cols];

    // evaluate the function at the starting point and calculate its norm
    updateResidualsAndCost();

    lmPar = 0;
    boolean firstIteration = true;
    VectorialPointValuePair current = new VectorialPointValuePair(point, objective);
    while (true) {
        incrementIterationsCounter();
        updateJacobian();
        qrDecomposition();
        qTy(residuals);

        for (int k = 0; k < solvedCols; ++k) {
            int pk = permutation[k];
            jacobian[k][pk] = diagR[pk];
        }

        if (firstIteration) {
            xNorm = scaleAccordingToJacobian(diag);
            delta = (xNorm == 0) ? initialStepBoundFactor : (initialStepBoundFactor * xNorm);
        }

        double maxCosine = checkOrthogonality();
        if (maxCosine <= orthoTolerance) {
            return current;
        }

        rescaleDiag(diag);

        for (double ratio = 0; ratio < 1.0e-4;) {
            saveState(oldX, oldRes);
            double previousCost = cost;
            swapResiduals(oldRes);

            determineLMParameter(residuals, delta, diag, work1, work2, work3);
            double lmNorm = computeNewPointAndNorm(oldX, diag);

            if (firstIteration) {
                delta = Math.min(delta, lmNorm);
            }

            updateResidualsAndCost();
            current = new VectorialPointValuePair(point, objective);

            double actRed = computeActualReduction(previousCost);
            double preRed = computePredictedReduction(work1, previousCost);
            double dirDer = -preRed;
            ratio = updateRatio(actRed, preRed);

            updateStepBound(delta, lmNorm, ratio, actRed, dirDer, previousCost);

            if (ratio >= 1.0e-4) {
                firstIteration = false;
                xNorm = updateNorms(diag);
            } else {
                resetToPreviousState(cost, oldX, oldRes);
            }

            if (isConverged(delta, xNorm, actRed, preRed, maxCosine)) {
                return current;
            }

            checkForSmallValues(delta, xNorm, actRed, preRed, maxCosine);
        }
    }
}