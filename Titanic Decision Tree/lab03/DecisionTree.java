package edu.caltech.cs2.lab03;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private final DecisionTreeNode root;

    public DecisionTree(DecisionTreeNode root) {
        this.root = root;
    }

    public String predict(Dataset.Datapoint point) {
        return predictHelper(point, this.root);
    }
    private String predictHelper(Dataset.Datapoint point, DecisionTreeNode currentNode){
        if (currentNode == null){
            return "No";
        }
          if (currentNode.isLeaf()){
              OutcomeNode thisNode = (OutcomeNode)currentNode;
               return thisNode.outcome;
           }
          else {
              AttributeNode thisNode = (AttributeNode)currentNode;
              String attribute = thisNode.attribute;
              String feature = point.attributes.get(attribute);
              currentNode = thisNode.children.get(feature);
              return predictHelper(point, currentNode);
           }
    }

    public static DecisionTree id3(Dataset dataset, List<String> attributes) {
        if (!dataset.pointsHaveSameOutcome().isEmpty()){
            return new DecisionTree(new OutcomeNode(dataset.pointsHaveSameOutcome()));
        }
       if (attributes.isEmpty()){
           return new DecisionTree(new OutcomeNode(dataset.getMostCommonOutcome()));

        }

        AttributeNode lowestEntropyAttributeNode =  new AttributeNode(dataset.getAttributeWithMinEntropy(attributes));
//        DecisionTree toReturn = new DecisionTree(lowestEntropyAttributeNode);


        List<String> featuresOfLEA = dataset.getFeaturesForAttribute(lowestEntropyAttributeNode.attribute);
        for (int i = 0; i < featuresOfLEA.size(); i++){
            String feature = featuresOfLEA.get(i);
            Dataset pointsWithFeature = dataset.getPointsWithFeature(feature);
            if (pointsWithFeature.isEmpty()){
                String outcome = dataset.getMostCommonOutcome();
                lowestEntropyAttributeNode.children.put(outcome, new OutcomeNode(outcome));
            }
            else {

                ArrayList<String> attributesCopy = new ArrayList<>(attributes);
                attributesCopy.remove(lowestEntropyAttributeNode.attribute);
                lowestEntropyAttributeNode.children.put(feature, id3(pointsWithFeature, attributesCopy).root);
            }
        }
        return new DecisionTree(lowestEntropyAttributeNode);
    }
}
