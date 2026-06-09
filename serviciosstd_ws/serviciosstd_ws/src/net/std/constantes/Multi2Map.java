package net.std.constantes;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;


public class Multi2Map {
	public static Map<String,String> procesar(MultivaluedMap<String, String> multi) {
		if(multi == null) return null;
		
		Map<String,String> map = new HashMap<String,String>();
		Iterator<String> it = multi.keySet().iterator();
		while(it.hasNext()){
			String theKey = (String)it.next();
			map.put(theKey, multi.getFirst(theKey));
		}
		return map;
	}
}
