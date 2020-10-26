package com.koltunm.skanner.machinelearning;

import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.services.FetcherForm;
import com.koltunm.skanner.util.Log;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class DataRowBuilder
{
    final private List<DataRow> rowList;
    final private HashMap<LocalDate, DataRow> map;
    private int groupingRange;

    public DataRowBuilder(CombinedData data, int groupingRange)
    {
        Log.i("Creating DataRowBuilder");
        this.rowList = new ArrayList<>();
        this.map = new HashMap<>();
        this.groupingRange = groupingRange;
        generateMap(data);
        populateList(data);
    }

    public List<DataRow> getRowList()
    {
        return rowList;
    }

    private void populateList(CombinedData data)
    {
        for (Map.Entry<LocalDate, DataRow> entry : map.entrySet())
        {
            rowList.add(entry.getValue());
        }

        rowList.sort(new Comparator<DataRow>()
        {
            @Override
            public int compare(DataRow dr1, DataRow dr2)
            {
                return dr1.getDate().compareTo(dr2.getDate());
            }
        });

        for (DataRow row : rowList)
        {
            DataRow nextRow = getNextRow(row.getDate().plusDays(1), 0);
            if (nextRow != null)
            {
                row.setChangePriceFuturePrice(nextRow.getClosePrice());
            }
        }
    }

    private DataRow getNextRow(LocalDate date, int count)
    {
        if (count == 3) return null;
        DataRow nextRow = map.get(date);
        if (nextRow == null)
        {
            return getNextRow(date.plusDays(1), ++count);
        }
        return nextRow;
    }

    private void generateMap(CombinedData data)
    {
        for (StockQuote quote : data.getStockQuoteList())
        {
            if (!map.containsKey(quote.getDate()))
            {
                map.put(quote.getDate(), new DataRow(quote));
            }
        }

        //Log.i(Arrays.toString(map.entrySet().toArray()));
        //Log.i(Arrays.toString(map.keySet().toArray()));

        LocalDate date = null;
        for (SentimentAnalyzable item : data.getSentimentAnalyzableList())
        {
            Log.i(String.format("Date: %s \tInstance: %s \ttype: %s%n", item.getDateTime().toLocalDate(), item.getClass().getName(), item.getSentimentType()));
            date = item.getDateTime().toLocalDate();
            for (int i = 0; i < groupingRange; i++)
            {
                addRecord(date.plusDays(i), item, 0);
            }
        }
    }

    private void addRecord(LocalDate date, SentimentAnalyzable item, int count)
    {
        Log.i(String.format("Adding record: [%d][%s][%s]", count, date, item.getSentimentHeading()));
        if (count == 3)
        {
            return;
        }
        else if (map.containsKey(date))
        {
            map.get(date).processItem(item);
        }
        else
        {
            addRecord(date.plusDays(1), item, ++count);
        }
    }
}
