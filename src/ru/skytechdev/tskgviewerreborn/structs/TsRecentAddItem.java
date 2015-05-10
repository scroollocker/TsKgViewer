package ru.skytechdev.tskgviewerreborn.structs;

import java.io.Serializable;

public class TsRecentAddItem implements Serializable{
	private static final long serialVersionUID = 1499029218515060062L;

	public String caption;
	public String link;
	public String date;
	public String comment;
	public boolean isFavorite;
}
