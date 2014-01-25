package ru.skytechdev.tskgviewerreborn;

import java.io.Serializable;

public class TSItem implements Serializable {
	private static final long serialVersionUID = 6603976961454354528L;
	public String value;
	public String url;
	public String imgurl;
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        TSItem other = (TSItem) obj;
        if (!value.equals(other.value) || (!url.equals(other.url)) || (!imgurl.equals(other.imgurl))) 
        	return false;
        
        return true;
    }	
}