public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    double epsilon = 1e-10; 

    double diff = nOd - d