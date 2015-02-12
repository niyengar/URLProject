/**
 * Context class acts as a wrapper for passing around objects.
 * @author Navneet
 */

package com.self.URLProjectPackage;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContextWrapper {
	
	private HashMap<String,Integer> statisticMap ;
	
	public ContextWrapper()
	{
		statisticMap = new LinkedHashMap<String,Integer>();
	}

	public HashMap<String, Integer> getStatisticMap() {
		return statisticMap;
	}

	public void setStatisticMap(HashMap<String, Integer> statisticMap) {
		this.statisticMap = statisticMap;
	}
	
	

}
