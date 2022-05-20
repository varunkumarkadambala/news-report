package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.*;
import java.util.*;


public class Main {
    static final int connectTimeout = 5000;
    static final int readTimeout = 5000;
    static final String newsServiceURL = "http://localhost:8080/api/news";

    // Call News Service URL
    public static String callNewsService(String newsServiceURL) throws IOException {

        URL url = new URL(newsServiceURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(connectTimeout);
        con.setReadTimeout(readTimeout);

        String inputLine;
        StringBuffer response = new StringBuffer();

        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (IOException e){
            System.out.println("Failed execution at http get input stream with error " + e);
        }
        finally {
            System.out.println("Completed http input stream");
        }

        return String.valueOf(response);
    }

    // Parse JSON String to JSON Object
    public static JSONArray parseJson(String jsonString) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(jsonString);

        return json;
    }

    // Generate News Reports
    public static List<Map<String, String>> generateNewsReport(JSONArray json){

        List<Map<String, String>> reportList = new ArrayList<>();
        Iterator i = json.iterator();

        while (i.hasNext()) {
            JSONObject article = (JSONObject) i.next();
            String id = (String)article.get("id");
            String category = (String)article.get("category");
            String title = (String)article.get("title");
            JSONArray tags = (JSONArray) article.get("tags");
            Long weight = (Long) article.get("weight");

            // Ignore if Category is Social and Add the data to a list of Maps
            if(!category.equals("SOCIAL")){
                Map<String, String> mapObject = new HashMap<>();
                mapObject.put("id", id.substring(id.length()-10));
                mapObject.put("category", category);
                mapObject.put("title", title);
                mapObject.put("tags", String.valueOf(tags.size()));
                mapObject.put("weight", String.valueOf(weight));
                reportList.add(mapObject);
            }
        }

        // Sort the Report list based on Weights
        Comparator<Map<String, String>> sortByWeight = Comparator.comparing(x -> -1 * Integer.valueOf(x.get("weight")));
        reportList.sort(sortByWeight);

        return reportList;
    }

    // Generate File Output
    public static void generateFileOutput(List<Map<String, String>> reportList) throws IOException {
        FileWriter myWriter = new FileWriter("edited_articles.txt");
        for(Map<String, String> mapObject : reportList)
        {
            myWriter.write(mapObject.get("id"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("category"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("title"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("tags"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("weight"));
            myWriter.write("\n");

        }

        myWriter.close();
    }

    public static void main(String[] args) throws IOException, ParseException {

        String response = callNewsService(newsServiceURL);
        JSONArray newsList = parseJson(response);
        List<Map<String, String>> newsReportList = generateNewsReport(newsList);
        generateFileOutput(newsReportList);
        return;
    }

}
