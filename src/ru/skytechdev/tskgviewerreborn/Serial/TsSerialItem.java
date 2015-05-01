package ru.skytechdev.tskgviewerreborn.Serial;

import java.io.Serializable;

public class TsSerialItem implements Serializable {
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
        TsSerialItem other = (TsSerialItem) obj;
        if (!value.equals(other.value) || (!url.equals(other.url)) || (!imgurl.equals(other.imgurl))) 
        	return false;
        
        return true;
    }	
}