import java.util.*;
import java.util.concurrent.*;

public class RandomForest {
    private List<DecisionTree> trees;
    private int numTrees;
    private int maxFeatures;
    private int numTotalFeatures;
    private Random random;

    public RandomForest(int numTrees, int maxFeatures, int numTotalFeatures) {
        this.numTrees = numTrees;
        this.maxFeatures = maxFeatures;
        this.numTotalFeatures = numTotalFeatures;
        this.trees = Collections.synchronizedList(new ArrayList<>());
        this.random = new Random();
    }

    public void train(List<DataPoint> data) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < numTrees; i++) {
            executor.execute(() -> {
                List<DataPoint> bootstrapSample = bootstrapSample(data);

                DecisionTree tree = new DecisionTree(maxFeatures, numTotalFeatures);
                tree.train(bootstrapSample);
                trees.add(tree);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int predict(double[] features) {
        int[] votes = new int[2]; // Assuming binary classification
        for (DecisionTree tree : trees) {
            int prediction = tree.predict(features);
            votes[prediction]++;
        }
        return votes[0] > votes[1] ? 0 : 1;
    }

    public double evaluate(List<DataPoint> testData) {
        int correct = 0;
        for (DataPoint dp : testData) {
            int prediction = predict(dp.features);
            if (prediction == dp.label) {
                correct++;
            }
        }
        return (double) correct / testData.size();
    }

    public double[] getFeatureImportances(int numFeatures) {
        double[] importances = new double[numFeatures];
        for (DecisionTree tree : trees) {
            double[] treeImportances = tree.getFeatureImportances();
            for (int i = 0; i < numFeatures; i++) {
                importances[i] += treeImportances[i];
            }
        }
        // Normalize importances
        double total = Arrays.stream(importances).sum();
        if (total > 0) {
            for (int i = 0; i < numFeatures; i++) {
                importances[i] /= total;
            }
        }
        return importances;
    }

    private List<DataPoint> bootstrapSample(List<DataPoint> data) {
        List<DataPoint> sample = new ArrayList<>();
        int n = data.size();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(n);
            sample.add(data.get(index));
        }
        return sample;
    }
}
