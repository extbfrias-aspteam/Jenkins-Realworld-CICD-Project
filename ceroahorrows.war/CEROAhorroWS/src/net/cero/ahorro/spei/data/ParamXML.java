/**
 * 
 */
package net.cero.ahorro.spei.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Jorge Gabriel
 *
 */
public class ParamXML  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<String>	params;
	
	public ParamXML(){
		params = new ArrayList<String>();
	}
	
	public ParamXML(String param){
		addParam(param);
	}

	public void addParam(String param){
		if(params == null){
			params = new ArrayList<String>();
		}
		params.add(param);
	}

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}

}
