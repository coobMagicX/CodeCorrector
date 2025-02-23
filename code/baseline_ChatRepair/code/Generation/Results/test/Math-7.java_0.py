            boolean needReset = currentEvent.reset(eventT, eventY);
            if (needReset) {
                // Copy state changes and recomputed derivatives
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;  // Indicate that a reset has occurred

                // Accept the step for all remaining events due to state change
                for (final EventState remaining : occuringEvents) {
                    remaining.stepAccepted(eventT, eventY);
                }
                // Break the loop to avoid re-evaluating with old states
                return eventT;
            }
