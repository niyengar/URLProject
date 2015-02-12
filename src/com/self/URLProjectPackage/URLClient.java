/**
 * Main class which collects the required responses for requests and 
 * carries out further processing on the response.
 * @author Navneet
 */

package com.self.URLProjectPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



/**
 * This class is written to get the content from the given url's.
 * @author Navneet
 *
 */
public class URLClient {
	
	
	public static void main(String args[])
	{
		final URLClient newObject = new URLClient();
		final ContextWrapper wrapper = new ContextWrapper();
		HashMap<String, Integer> statisticMap = wrapper.getStatisticMap();
		System.out.println("Please enter 5 urls in csv format");
		long startTime = System.currentTimeMillis();
		String newString = new String();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
		{
			try
			{
			newString = br.readLine();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(newString==null||newString.equals(""))
		{
			System.out.println("Please check URL Entry-either null or empty");
			return;
		}
		else
		{
			newString = newString.trim();
			String URLArray[] = newString.split(",");
			for(int i =0;i<URLArray.length;i++)
			{
			try {
				long urlStartTime = System.currentTimeMillis();
				newObject.getResponse(URLArray[i].trim(),statisticMap);
				long urlEndTime = System.currentTimeMillis();
				System.out.println("The time for Url "+i+" "+"to process is "+(urlEndTime-urlStartTime)+"  milliseconds");
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			wrapper.setStatisticMap(new URLUtility().sortMapByValue(statisticMap));
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total Time Taken: "+(endTime-startTime));
	}
	
	public void getResponse(String url, HashMap<String,Integer>statisticMap) throws Exception
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try
		{
			final HttpGet httpGet = new HttpGet(url);
			System.out.println("Executing request"+httpGet.getRequestLine());
			
			ResponseHandler<String> responseHandler = new ResponseHandler<String>(){

				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if(status>=200 && status<300)
					{
					HttpEntity entity  = response.getEntity();
					if(entity!=null)
					{
						return EntityUtils.toString(entity);
					}
					else
					{
						return null;
					}
					}
					else
					{
						throw new ClientProtocolException(httpGet.getRequestLine()+" Unexpected Status"+status);
					}
				}
				
			};
			
			
			String responseBody = httpClient.execute(httpGet, responseHandler);
			parseResponse(responseBody.toString(),statisticMap);
		}
		finally
		{
			httpClient.close();
		}
		
	}
	
	public void parseResponse(String line,HashMap<String,Integer>statisticMap)
	{
		String newline;
		try(BufferedReader br = new BufferedReader(new StringReader(line)))
		{
			while((newline=br.readLine())!=null)
			{
				newline = newline.trim();
				String tempBuffer[] = newline.split("\\s+");
				
				for(String key:tempBuffer)
				{
					if(!key.equals((String)("")))
					{
					if(statisticMap.containsKey(key))
					{
						statisticMap.put(key, statisticMap.get(key)+1);
					}
					else
					{
						statisticMap.put(key, 1);
					}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Size of Map is "+statisticMap.size());
		
	}
	
	
	
	
	

}
