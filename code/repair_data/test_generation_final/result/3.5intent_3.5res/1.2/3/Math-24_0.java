        if (fu <= fx) {
            if (u < x) {
                b = x;
            } else {
                a = x;
            }
            v = w;
            fv = fw;
            w = x;
            fw = fx;
            x = u;
            fx = fu;
        } else {
            if (u < x) {
                a = u;
            } else {
                b = u;
            }
            UnivariatePointValuePair temp = best(new UnivariatePointValuePair(u, isMinim ? fu : -fu), new UnivariatePointValuePair(x, isMinim ? fx : -fx), isMinim);
            x = temp.getPoint();
            fx = temp.getValue();
            v = w;
            fv = fw;
            w = u;
            fw = fu;
        }