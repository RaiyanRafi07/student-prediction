import java.util.*;

public class DecisionTree {
    private Node root;
    private int maxFeatures;
    private Random random;
    private double[] featureImportances;

    public DecisionTree(int maxFeatures, int numTotalFeatures) {
        this.maxFeatures = maxFeatures;
        this.random = new Random();
        this.featureImportances = new double[numTotalFeatures];
    }

    public void train(List<DataPoint> data) {
        this.root = buildTree(data);
    }

    public int predict(double[] features) {
        Node node = root;
        while (!(node instanceof LeafNode)) {
            DecisionNode decisionNode = (DecisionNode) node;
            if (features[decisionNode.featureIndex] <= decisionNode.threshold) {
                node = decisionNode.left;
            } else {
                node = decisionNode.right;
            }
        }
        return ((LeafNode) node).label;
    }

    public double[] getFeatureImportances() {
        return featureImportances;
    }

    // Helper methods
    private Node buildTree(List<DataPoint> data) {
        if (data.isEmpty() || isPure(data)) {
            int label = getMajorityLabel(data);
            return new LeafNode(label);
        }

        int numFeatures = data.get(0).features.length;
        int[] featureIndices = getRandomFeatures(numFeatures, maxFeatures);

        Split bestSplit = findBestSplit(data, featureIndices);

        if (bestSplit == null || bestSplit.gain == 0.0) {
            int label = getMajorityLabel(data);
            return new LeafNode(label);
        }

        featureImportances[bestSplit.featureIndex] += bestSplit.gain;

        Node left = buildTree(bestSplit.leftData);
        Node right = buildTree(bestSplit.rightData);

        return new DecisionNode(bestSplit.featureIndex, bestSplit.threshold, left, right);
    }

    private boolean isPure(List<DataPoint> data) {
        int firstLabel = data.get(0).label;
        for (DataPoint dp : data) {
            if (dp.label != firstLabel) {
                return false;
            }
        }
        return true;
    }

    private int getMajorityLabel(List<DataPoint> data) {
        int[] labelCounts = new int[2]; // Assuming binary classification (0 and 1)
        for (DataPoint dp : data) {
            labelCounts[dp.label]++;
        }
        return labelCounts[0] > labelCounts[1] ? 0 : 1;
    }

    private int[] getRandomFeatures(int totalFeatures, int maxFeatures) {
        int[] featureIndices = new int[totalFeatures];
        for (int i = 0; i < totalFeatures; i++) {
            featureIndices[i] = i;
        }
        shuffleArray(featureIndices);
        return Arrays.copyOfRange(featureIndices, 0, maxFeatures);
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Swap
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private Split findBestSplit(List<DataPoint> data, int[] featureIndices) {
        Split bestSplit = null;
        double bestImpurity = calculateGini(data);

        for (int featureIndex : featureIndices) {
            double[] thresholds = data.stream()
                .mapToDouble(dp -> dp.features[featureIndex])
                .distinct()
                .sorted()
                .toArray();

            for (double threshold : thresholds) {
                List<DataPoint> left = new ArrayList<>();
                List<DataPoint> right = new ArrayList<>();
                for (DataPoint dp : data) {
                    if (dp.features[featureIndex] <= threshold) {
                        left.add(dp);
                    } else {
                        right.add(dp);
                    }
                }

                if (left.isEmpty() || right.isEmpty()) continue;

                double impurity = calculateWeightedImpurity(left, right);
                double gain = bestImpurity - impurity;

                if (gain > 0 && (bestSplit == null || gain > bestSplit.gain)) {
                    bestSplit = new Split(featureIndex, threshold, left, right, gain);
                }
            }
        }

        return bestSplit;
    }

    private double calculateWeightedImpurity(List<DataPoint> left, List<DataPoint> right) {
        int total = left.size() + right.size();
        double leftImpurity = calculateGini(left);
        double rightImpurity = calculateGini(right);
        return (left.size() * leftImpurity + right.size() * rightImpurity) / total;
    }

    private double calculateGini(List<DataPoint> data) {
        int[] labelCounts = new int[2];
        for (DataPoint dp : data) {
            labelCounts[dp.label]++;
        }
        double impurity = 1.0;
        int total = data.size();
        for (int count : labelCounts) {
            double prob = (double) count / total;
            impurity -= prob * prob;
        }
        return impurity;
    }

    // Inner classes
    private abstract class Node { }

    private class DecisionNode extends Node {
        int featureIndex;
        double threshold;
        Node left, right;

        public DecisionNode(int featureIndex, double threshold, Node left, Node right) {
            this.featureIndex = featureIndex;
            this.threshold = threshold;
            this.left = left;
            this.right = right;
        }
    }

    private class LeafNode extends Node {
        int label;

        public LeafNode(int label) {
            this.label = label;
        }
    }

    private class Split {
        int featureIndex;
        double threshold;
        List<DataPoint> leftData, rightData;
        double gain;

        public Split(int featureIndex, double threshold, List<DataPoint> leftData, List<DataPoint> rightData, double gain) {
            this.featureIndex = featureIndex;
            this.threshold = threshold;
            this.leftData = leftData;
            this.rightData = rightData;
            this.gain = gain;
        }
    }
}
