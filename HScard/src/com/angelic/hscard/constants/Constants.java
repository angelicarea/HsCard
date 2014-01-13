package com.angelic.hscard.constants;

import java.io.File;

import android.os.Environment;

public class Constants {
	
	public final static String SdCardPath = "HsCards";
	public final static String DATA_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "HsCards" + File.separator;
	public final static String CardPath = "card/";
	public final static String SuffixPng = ".png";
	public final static String SuffixGCard = "g-";
}
