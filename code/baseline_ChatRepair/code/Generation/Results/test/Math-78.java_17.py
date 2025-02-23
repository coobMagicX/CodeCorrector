...
            // check events occurrence
            if (g0Positive ^ (gb >= 0)) {
                // there is a sign change: an event is expected during this step
                increasing = gb >= ga;
                
                if ((ga >= 0) ^ (gb >= 0)) {  // Ensure the signs are different for ga and gb
                    final UnivariateRealFunction f = new UnivariateRealFunction() {
                        public double value(final double t) throws FunctionEvaluationException {
                            try {
                                interpolator.setInterpolatedTime(t);
                                return handler.g(t, interpolator.getInterpolatedState());
                            } catch (DerivativeException e) {
                                throw new FunctionEvaluationException(e, t);
                            } catch (EventException e) {
                                throw new FunctionEvaluationException(e, t);
                            }
                        }
                    };
                    final BrentSolver solver = new BrentSolver();
                    solver.setAbsoluteAccuracy(convergence);
                    solver.setMaximalIterationCount(maxIterationCount);
                    final double root = solver.solve(f, ta, tb);  // Removed the conditional to ensure ta < tb for solver
                    
                    if ((Math.abs(root - ta) <= convergence) &&
                        (Math.abs(root - previousEventTime) <= convergence)) {
                        // we have either found nothing or found (again ?) a past event, we simply ignore it
                        ta = tb;
                        ga = gb;
                    } else if (Double.isNaN(previousEventTime) ||
                              (Math.abs(previousEventTime - root) > convergence)) {
                        pendingEventTime = root;
                        if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
                            // we were already waiting for this event which was
                            // found during a previous call for a step that was
                            // rejected, this step must now be accepted since it
                            // properly ends exactly at the event occurrence
                            return false;
                        }
                        // either we were not waiting for the event or it has
                        // moved in such a way the step cannot be accepted
                        pendingEvent = true;
                        return true;
                    }
                }
            } else {
                // no sign change: there is no event for now
                ta = tb;
                ga = gb;
            }
...
