import java.util.*;
import java.util.concurrent.*;

// This class builds a random forest from multiple decision trees.
public class RandomForest {
    private List<DecisionTree> trees;
    private int numTrees;
    private int maxFeatures;
    private int numTotalFeatures;
    private Random rand;

    public RandomForest(int numTrees, int maxFeatures, int totalF) {
        this.numTrees = numTrees;
        this.maxFeatures = maxFeatures;
        this.numTotalFeatures = totalF;
        this.trees = Collections.synchronizedList(new ArrayList<>());
        this.rand = new Random();
    }

    public void train(List<Node> data) {
        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i=0;i<numTrees;i++) {
            exec.execute(() -> {
                List<Node> sample=bootstrapSample(data);
                DecisionTree dt=new DecisionTree(maxFeatures,numTotalFeatures);
                dt.train(sample);
                trees.add(dt);
            });
        }
        exec.shutdown();
        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e){}
    }

    public int predict(double[] feats) {
        int[] votes=new int[2];
        for (DecisionTree t:trees) {
            int p=t.predict(feats);
            votes[p]++;
        }
        return votes[0]>votes[1]?0:1;
    }

    public double evaluate(List<Node> test) {
        int correct=0;
        for (Node n:test) {
            int pred=predict(nodeToArr(n));
            if (pred==n.getLabel()) correct++;
        }
        return (double)correct/test.size();
    }

    private List<Node> bootstrapSample(List<Node> data) {
        List<Node> samp=new ArrayList<>();
        int N=data.size();
        for (int i=0;i<N;i++) {
            int idx=rand.nextInt(N);
            samp.add(data.get(idx));
        }
        return samp;
    }

    private double[] nodeToArr(Node n) {
        double[] arr=new double[n.getNumFeatures()];
        for (int i=0;i<n.getNumFeatures();i++) arr[i]=n.getFeature(i);
        return arr;
    }
}
