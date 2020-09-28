
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;  
import org.json.JSONArray;  




public class YouTubeAPI
{
    public void searchAPI(String param) throws IOException 
    {

        String[] params=param.split("[\\s,;]+");

        //Tokenizing the input and forming the query string
        String inputParam="";
        for(int i=0;i<params.length;i++)
            if(i!=params.length-1)
                inputParam+=params[i]+"+";
            else inputParam+=params[i];

        URL urlForGetRequest = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyDHq24GLGIs637CIoKFdeg551lc1yghya8&maxResults=10&type=video&q="+inputParam);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) 
        {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) 
            {
                response.append(readLine);
            } in .close();
            //System.out.println("JSON String Result " + response.toString());
            
            //Extracting required details from JSON object
            try 
            {
                JSONObject jObject  = new JSONObject(response.toString());
                JSONObject pageinfo=jObject.getJSONObject("pageInfo");
                int pageno=pageinfo.getInt("resultsPerPage");
                String videoId="";
                String url="";
                String title="";
                JSONArray items=jObject.getJSONArray("items");
                
                for(int i=0;i<pageno;i++)
                {
                	JSONObject object = items.getJSONObject(i);
                	JSONObject id=object.getJSONObject("id");
                	System.out.print("VideoId:");
                	videoId=id.getString("videoId");
                	System.out.println(videoId);
                	
                	JSONObject snippet=object.getJSONObject("snippet");
                	System.out.print("Title:");
                	title=snippet.getString("title");
                	System.out.println(title);
                	
                	System.out.print("Url:");
                	url="http://www.youtube.com/watch?v="+videoId;
                	System.out.println(url);
                	System.out.println("*----------------------------------------------------*");
                	
                }
                
            } 
            catch (Exception e) 
            {
                System.out.println("JSON Exception "+e);
            }
            

        } 
        else 
        {
            System.out.println("GET NOT WORKED "+responseCode);
        }
        
    }
    public static void main(String args[])throws IOException
    {
    	YouTubeAPI api=new YouTubeAPI();
        Scanner in =new Scanner(System.in);
        System.out.println("Please enter keyword to search:");
        String s=in.nextLine();
        api.searchAPI(s);
    }
}