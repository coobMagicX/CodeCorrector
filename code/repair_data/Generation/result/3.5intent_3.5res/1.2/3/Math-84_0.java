        protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

            int numEvaluations = 0; // Track the number of evaluations

            while (numEvaluations < 150) { // Limit the number of evaluations

                incrementIterationsCounter();

                // save the original vertex
                final RealPointValuePair[] original = simplex;
                final RealPointValuePair best = original[0];

                // perform a reflection step
                final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
                numEvaluations++;

                if (comparator.compare(reflected, best) < 0) {

                    // compute the expanded simplex
                    final RealPointValuePair[] reflectedSimplex = simplex;
                    final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
                    numEvaluations++;

                    if (comparator.compare(reflected, expanded) <= 0) {
                        // accept the reflected simplex
                        simplex = reflectedSimplex;
                    }

                    continue; // Continue to the next iteration

                }

                // compute the contracted simplex
                final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
                numEvaluations++;

                if (comparator.compare(contracted, best) < 0) {
                    // accept the contracted simplex
                    simplex = original; // Set it back to original
                } else {
                    simplex = contracted; // Set it to contracted
                }

            }

            throw new OptimizationException("Exceeded maximum number of evaluations");

        }