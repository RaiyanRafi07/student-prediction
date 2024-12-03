public class DataPoint {
    double[] features;
    int label; // 0 for failing, 1 for passing

    public DataPoint(double[] features, int label) {
        this.features = features;
        this.label = label;
    }
}
