package ru.skytechdev.tskgviewerreborn;

import android.graphics.Bitmap;

public class TSItemBitmap {
	public TSItemBitmap(TSItem item, Bitmap bitmap) {
		this.item = item;
		this.bitmap = bitmap;
	}
	public TSItem item;
	public Bitmap bitmap;
}
