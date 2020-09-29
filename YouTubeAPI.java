
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
	private String part;
	private String key;
	private int maxResults;
	private String type;
	private String url;
	public YouTubeAPI(String snippet,String key,int maxResult,String type,String url)
	{
		this.setPart(snippet);
		this.setKey(key);
		this.setMaxResults(maxResult);
		this.setType(type);
		this.setUrl(url);
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    public void searchAPI(String param) throws IOException 
    {

        String[] params=param.split("[\\s,;]+");

        //Tokenizing the input and forming the query string
        String inputParam="";
        for(int i=0;i<params.length;i++)
            if(i!=params.length-1)
                inputParam+=params[i]+"+";
            else inputParam+=params[i];
        

        URL urlForGetRequest = new URL(getUrl()+"?part="+getPart()+"&key="+getKey()+"&maxResults="+getMaxResults()+"&type="+getType()+"&q="+inputParam);
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
                System.out.println(pageno);
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
    	YouTubeAPI api=new YouTubeAPI("snippet","AIzaSyDHq24GLGIs637CIoKFdeg551lc1yghya8",10,"video","https://www.googleapis.com/youtube/v3/search");
        Scanner in =new Scanner(System.in);
        System.out.println("Please enter keyword to search:");
        String s=in.nextLine();
        api.searchAPI(s);
    }
	
	
}