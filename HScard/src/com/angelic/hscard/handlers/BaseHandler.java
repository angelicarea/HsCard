package com.angelic.hscard.handlers;

import java.io.IOException;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class BaseHandler implements IThreadHandler
{
	protected Object mResult;
	protected Handler mTarget;
	protected Context mContext;

	public BaseHandler(Context context, Handler target)
	{
		mContext = context;
		mTarget = target;
	}

	public void action()
	{
		try
		{
			doAction();
			onSuccess();
		} catch (IOException e)
		{
			onException(1, 0);
			e.printStackTrace();
		} catch (HttpException e)
		{
			onException(2, 0);
			e.printStackTrace();
		} catch (JSONException e)
		{
			onException(3, 0);
			e.printStackTrace();
		}

	}

	public void onException(int type, int spec)
	{
		if (mTarget != null)
			mTarget.sendEmptyMessage(type);
	}

	public void onSuccess()
	{
		Message msg = mTarget.obtainMessage(0);
		msg.obj = mResult;
		if (mTarget != null)
			mTarget.sendMessage(msg);
	}

	public abstract void doAction() throws IOException, HttpException, JSONException;
}
