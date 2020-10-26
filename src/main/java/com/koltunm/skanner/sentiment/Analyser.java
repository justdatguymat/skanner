package com.koltunm.skanner.sentiment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.util.Properties;

public class Analyser
{
    private StanfordCoreNLP pipeline;

    public Analyser()
    {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(properties);
    }

    public OverallScore getSentimentScore(String text)
    {
        OverallScore overallScore = new OverallScore();
        if (text != null && text.length() > 0)
        {
            Annotation annotation = pipeline.process(text);
            for(CoreMap sentence: annotation.get(CoreAnnotations.SentencesAnnotation.class))
            {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                SimpleMatrix matrix = RNNCoreAnnotations.getPredictions(tree);
                SentenceScore sentenceScore = new SentenceScore(matrix);
                overallScore.mergeScore(sentenceScore);
                /*
                String type = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                int score = RNNCoreAnnotations.getPredictedClass(tree);

                System.out.printf("SENTENCE : %s%n", sentence);
                System.out.println("Type: " + type);
                System.out.println("Score: " + score + "\n");
                System.out.println("***THIS SCORE***\n" + sentenceScore);
                System.out.println("***OVERALL SCORE***\n" + overallScore);
                 */
            }
        }
        return overallScore;
    }
}
