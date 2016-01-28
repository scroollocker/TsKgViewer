package ru.skytechdev.tskgviewerreborn.structs;

import android.graphics.Bitmap;

public class TsBitmapItem {
	public TsBitmapItem(TsSerialItem item, Bitmap bitmap) {
		this.item = item;
		this.bitmap = bitmap;
	}
	public TsSerialItem item;
	public Bitmap bitmap;
}
