public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    List<XYAnnotation> includedAnnotations = new ArrayList<>();
    boolean isDomainAxis = true;

    
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(domainIndex));
        if (domainIndex == 0 && this.annotations != null) {
            for (XYAnnotation annotation : this.annotations) {
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(rangeIndex));
        if (rangeIndex == 0 && this.annotations != null) {
            for (XYAnnotation annotation : this.annotations) {
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    
    for (XYDataset d : mappedDatasets) {
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (isDomainAxis) {
                result = Range.combine(result, r != null ? r.findDomainBounds(d) : DatasetUtilities.findDomainBounds(d));
            } else {
                result = Range.combine(result, r != null ? r.findRangeBounds(d) : DatasetUtilities.findRangeBounds(d));
            }
            if (r != null && r.getAnnotations() != null) {
                for (XYAnnotation a : r.getAnnotations()) {
                    if (a instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(a);
                    }
                }
            }
        }
    }

    
    for (XYAnnotationBoundsInfo xyabi : includedAnnotations) {
        if (xyabi.getIncludeInDataBounds()) {
            result = isDomainAxis ? 
                     Range.combine(result, xyabi.getXRange()) : 
                     Range.combine(result, xyabi.getYRange());
        }
    }

    return result;
}
