package game.essential;

import java.util.Timer;

@SuppressWarnings({ "static-access" })
public abstract class GameLoop implements Runnable {
	
	public boolean running = false;
	public int targetFps = 60;
	
	private Timer timer;
	
	private long lastFrame = System.nanoTime();
	public void run()
	{
		long nowTime;
		running = true;
		while (running)
		{
			nowTime = System.nanoTime();
			update(nowTime - lastFrame);
			sync(nowTime);
			lastFrame = nowTime;
		}
	}
	
	
	
	protected void sync(long frameStart) {
        long timeLeft = 0;
        final long TARGET_FRAME_TIME = 1000000000 / targetFps;
        do {
			timeLeft = (frameStart + TARGET_FRAME_TIME) - System.nanoTime();
			if (timeLeft > 10000000) {  //10 ms
				try {
					Thread.currentThread().sleep(2);
				} catch (InterruptedException e) {}
			} else {
				Thread.currentThread().yield();
			}
		} while (timeLeft > 0);
	}
	
	
	public void stop()
	{
		if (timer != null) {
			timer.cancel();
		}
		running = false;
	}

	
	/**
	 * To be overridden; This function gets called [targetFps] times per second.
	 * @param delta In nano seconds.
	 */
	protected abstract void update(long delta);
}
