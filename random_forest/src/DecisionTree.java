import java.util.*;

// This class builds a single decision tree for classification.
public class DecisionTree {
    private TreeNode root;
    private int maxFeatures;
    private Random rand;
    private double[] importances;

    public DecisionTree(int maxFeatures, int totalFeatures) {
        this.maxFeatures = maxFeatures;
        this.rand = new Random();
        this.importances = new double[totalFeatures];
    }

    public void train(List<Node> data) {
        root = buildTree(data);
    }

    public int predict(double[] feats) {
        TreeNode node = root;
        while (!(node instanceof LeafTreeNode)) {
            DecisionTreeNode dtn = (DecisionTreeNode)node;
            if (feats[dtn.featureIndex] <= dtn.threshold) node=dtn.left;
            else node=dtn.right;
        }
        return ((LeafTreeNode)node).label;
    }

    public double[] getFeatureImportances() {
        return importances;
    }

    private TreeNode buildTree(List<Node> data) {
        if (data.isEmpty() || isPure(data)) {
            int lbl = majorityLabel(data);
            return new LeafTreeNode(lbl);
        }

        int nf = data.get(0).getNumFeatures();
        int[] selectedFeatures = selectFeatures(nf, maxFeatures);
        Split best = findBestSplit(data, selectedFeatures);

        if (best == null || best.gain <= 0) {
            int lbl = majorityLabel(data);
            return new LeafTreeNode(lbl);
        }

        importances[best.featureIndex] += best.gain;

        TreeNode left = buildTree(best.leftData);
        TreeNode right = buildTree(best.rightData);
        return new DecisionTreeNode(best.featureIndex, best.threshold, left, right);
    }

    private boolean isPure(List<Node> data) {
        if (data.isEmpty()) return true;
        int first = data.get(0).getLabel();
        for (Node n : data) {
            if (n.getLabel() != first) return false;
        }
        return true;
    }

    private int majorityLabel(List<Node> data) {
        int[] counts = new int[2];
        for (Node n:data) counts[n.getLabel()]++;
        return counts[0]>counts[1]?0:1;
    }

    private int[] selectFeatures(int total, int maxF) {
        int[] arr = new int[total];
        for (int i=0;i<total;i++) arr[i]=i;
        shuffle(arr);
        return Arrays.copyOfRange(arr,0,maxF);
    }

    private void shuffle(int[] arr) {
        for (int i=arr.length-1;i>0;i--) {
            int idx = rand.nextInt(i+1);
            int tmp=arr[idx];
            arr[idx]=arr[i];
            arr[i]=tmp;
        }
    }

    private Split findBestSplit(List<Node> data, int[] feats) {
        double baseImp = gini(data);
        Split best = null;
        for (int f: feats) {
            double[] thresholds = data.stream().mapToDouble(d->d.getFeature(f)).distinct().sorted().toArray();
            for (double t: thresholds) {
                List<Node> left=new ArrayList<>();
                List<Node> right=new ArrayList<>();
                for (Node d:data) {
                    if (d.getFeature(f)<=t) left.add(d);
                    else right.add(d);
                }
                if (left.isEmpty()||right.isEmpty()) continue;

                double newImp = weightedImpurity(left,right);
                double gain = baseImp - newImp;
                if (gain>0&&(best==null||gain>best.gain)) {
                    best=new Split(f,t,left,right,gain);
                }
            }
        }
        return best;
    }

    private double gini(List<Node> data) {
        int[] counts = new int[2];
        for (Node n:data) counts[n.getLabel()]++;
        double imp=1.0;
        int tot=data.size();
        for (int c: counts) {
            double p=(double)c/tot;
            imp-=p*p;
        }
        return imp;
    }

    private double weightedImpurity(List<Node> l, List<Node> r) {
        int tot=l.size()+r.size();
        double li=gini(l);
        double ri=gini(r);
        return (l.size()*li + r.size()*ri)/tot;
    }

    private abstract class TreeNode{}
    private class DecisionTreeNode extends TreeNode {
        int featureIndex;
        double threshold;
        TreeNode left,right;
        DecisionTreeNode(int f,double t,TreeNode L,TreeNode R) {
            featureIndex=f; threshold=t; left=L; right=R;
        }
    }
    private class LeafTreeNode extends TreeNode {
        int label;
        LeafTreeNode(int l) {label=l;}
    }

    private class Split {
        int featureIndex;
        double threshold;
        List<Node> leftData,rightData;
        double gain;
        Split(int fi,double thr,List<Node> ld,List<Node> rd,double g) {
            featureIndex=fi; threshold=thr; leftData=ld; rightData=rd; gain=g;
        }
    }
}
