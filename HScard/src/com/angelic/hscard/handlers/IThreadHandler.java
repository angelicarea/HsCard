package com.angelic.hscard.handlers;

import org.json.JSONException;

public interface IThreadHandler
{
	public void action() throws JSONException;
	public void onException(int type, int spec);
	public void onSuccess();
}
