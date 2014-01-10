package com.angelic.hscard.handlers;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool
{
	private ThreadPoolExecutor mExecutor;
	private static ThreadPool sInstance;
	private ThreadPool()
	{
		mExecutor = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		mExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}
	public static ThreadPool getInstance()
	{
		if(sInstance == null)
			sInstance = new ThreadPool();
		return sInstance;
	}
	public void destroy()
	{
		mExecutor.shutdown();
	}
	public int execute(final IThreadHandler handler)
	{
		Runnable r = new Runnable()
		{
			public void run()
			{
				try
				{
					handler.action();
				}
				catch (Throwable e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mExecutor.remove(this);
			}
		};
		mExecutor.execute(r);
		return 0;
	}
}
