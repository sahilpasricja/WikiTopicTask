package WikiTopicWordCount;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;


public class Solution {
    static final String KEY1 = "parse";
    static final String KEY2 = "text";
    static  final String DEFAULT_SEARCH_TERM = "pizza";

    TopicSource topicSource;


    public int getOccurrenceCount(String topic) throws Exception {
        topicSource = new TopicSource(topic,SearchURL.WIKI);
        InputStream is = getInputStream(topicSource);
        JSONObject jsonResponse = inputStreamToJsonObj(is);
        return countKeyword(topicSource,jsonResponse);

    }

    //Fetch topic data from wiki and store it as stream
    public InputStream getInputStream(TopicSource topicSource) throws Exception {
        String readurl = String.format(topicSource.searchUrl(), topicSource.topic());
        try{
            InputStream is = new URL(readurl).openStream();
            return is;
        }
        catch (Exception e){
            throw new Exception("Unable to establish connection.", e);
        }
    }

    // Convert Input Stream to Json Object and fetch for key : Text
    private JSONObject inputStreamToJsonObj(InputStream is) throws IOException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonResponse = readAll(rd);
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject jsonObjectText = json.getJSONObject(KEY1).getJSONObject(KEY2);
            return jsonObjectText;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            is.close();
        }
    }

    //Read BR and convert to string
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    //Calculate number of occurrences of topic in text returned from Wiki
    public int countKeyword(TopicSource topicSource,JSONObject jsonObject){
        return StringUtils.countMatches(jsonObject.toString(), topicSource.topic());
    }

    public static void main(String[] args) throws Exception
    {
        Solution solution = new Solution();
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the topic: ");
        String topic = reader.next();
        //String topic = (args.length == 1) ? args[0] : DEFAULT_SEARCH_TERM;
        System.out.printf("Calculating occurrences for topic: %s, in Wiki data \n",topic);
        System.out.println(solution.getOccurrenceCount(topic) + " occurrences of search topic: " + topic);
    }
}