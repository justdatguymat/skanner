package com.koltunm.skanner.machinelearning;

import com.koltunm.skanner.util.Log;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataSet
{
    private Instances data;
    private Instances trainSet;
    private Instances testSet;

    public DataSet(List<DataRow> rowList)
    {
        initInstanceObject();
        createDataSet(rowList);
    }

    public DataSet(String filePath)
    {
        data = loadFromFile(filePath);
    }

    private void initInstanceObject()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("date", "yyyy-MM-dd"));
        attributes.add(new Attribute("positives"));
        attributes.add(new Attribute("neutrals"));
        attributes.add(new Attribute("negatives"));
        attributes.add(new Attribute("total_tweets"));
        attributes.add(new Attribute("total_articles"));
        attributes.add(new Attribute("volume"));
        attributes.add(new Attribute("low_price"));
        attributes.add(new Attribute("high_price"));
        attributes.add(new Attribute("open_price"));
        attributes.add(new Attribute("close_price"));
        attributes.add(new Attribute("price_change"));
        data = new Instances("StockPrediction", attributes, 0);
        trainSet = new Instances("StockPrediction", attributes, 0);
        testSet = new Instances("StockPrediction", attributes, 0);
    }

    public void createDataSet(List<DataRow> rowList)
    {
        if (!data.isEmpty())
        {
            data.delete();
            initInstanceObject();
        }

        double[] values;
        for (DataRow row : rowList)
        {
            values = new double[data.numAttributes()];
            try
            {
                values[0] = data.attribute(0).parseDate(row.getDate().toString());
            }
            catch (ParseException e)
            {
                Log.e(String.format("Error while parsing Date [%s]", row.getDate().toString()), e);
                e.printStackTrace();
            }
            values[1] = row.getPositives();
            values[2] = row.getNeutrals();
            values[3] = row.getNegatives();
            values[4] = row.getTotalTweets();
            values[5] = row.getTotalArticles();
            values[6] = row.getVolume();
            values[7] = row.getLowPrice();
            values[8] = row.getHighPrice();
            values[9] = row.getOpenPrice();
            values[10] = row.getClosePrice();
            values[11] = row.getChangePrice();

            Log.i("Adding row: " + Arrays.toString(values));
            data.add(new DenseInstance(1.0, values));
        }
        Log.i(data);
    }

    public void splitData(int folds)
    {
        Random random = new Random(5);
        Instances randomData = new Instances(data);
        randomData.randomize(random);
        for (int i = 0; i < folds; i++)
        {
            trainSet = randomData.trainCV(folds, i, random);
            testSet = randomData.testCV(folds, i);
        }
    }

    public void writeToFile(String filePath)
    {
        try
        {
            FileWriter fw = new FileWriter(filePath);
            fw.write(data.toString());
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e(String.format("Failed to write to a file: %s", filePath.toString()), e);
        }
    }

    private Instances loadFromFile(String filePath)
    {
        Instances instances = null;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            instances = new Instances(reader);
        }
        catch (IOException e)
        {
            Log.e("Failed to load from file.", e);
            e.printStackTrace();
        }
        return instances;
    }

    public Instances getInstances()
    {
        return data;
    }

    public Instances getTrainSet()
    {
        return trainSet;
    }

    public void setTrainSet(Instances trainSet)
    {
        this.trainSet = trainSet;
    }

    public Instances getTestSet()
    {
        return testSet;
    }

    public void setTestSet(Instances testSet)
    {
        this.testSet = testSet;
    }

    public void removeAttributes()
    {
        int[] indices = new int[] {6, 7, 8, 9, 10};
        Remove filter = new Remove();
        try
        {
            filter.setAttributeIndicesArray(indices);
            filter.setInvertSelection(false);
            filter.setInputFormat(getInstances());
            data = Filter.useFilter(data, filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(String.format("Failed to remove attributes:%n%s", filter), e);
        }
    }

    public String toString()
    {
        return data.toSummaryString();
    }
}
