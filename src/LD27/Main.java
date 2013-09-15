package LD27;

import game.essential.Drawable;
import game.essential.GameLoop;
import game.essential.Input;
import game.essential.Monitor;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import Menu.MainMenu;

public class Main extends GameLoop {

	Monitor display = new Monitor(WIDTH, HEIGHT);
	
	Drawable model = new MainMenu();
	
	public int highScore = -1;
	
	public Main()
	{
		
		Input.initialize(display);
		JFrame window = new JFrame("Ploppz' LD27");

		window.add(display);
		window.setSize(WIDTH + 6, HEIGHT + 27);
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		display.models.add(model);
	}

	private ArrayList<Integer> counters = new ArrayList<Integer>();
	private ArrayList<GameObject> tweenOuts =new ArrayList<GameObject>();
	private GameObject tweenIn = null;
	
	private void addTweenOut(GameObject o)
	{
		tweenOuts.add(o);
		counters.add(40);
	}
	private void setTweenIn(GameObject o)
	{
		/*if (tweenIn != null) {
			addTweenOut(tweenIn);
		}*/
		tweenIn = o;
	}
	
	public void endGame(Game game, int score)
	{
		MainMenu menu = new MainMenu(score);
		menu.pos.y = Main.HEIGHT;
		setTweenIn(menu);
		display.models.add(menu);
		
		addTweenOut(game);
		game.paused = true;
		
		if (score > highScore) {
			highScore = score;
		}
	}
	public void startGame(MainMenu menu)
	{
		Game game = new Game();
		
		setTweenIn(game);
		game.pos.y = Main.HEIGHT;
		display.models.add(game);
		
		addTweenOut(menu);
		menu.locked = true;
	}
	
	
	@Override
	protected void update(long delta) {
		double timeFactor = delta / targetFrameDuration;
		// TWEENING STUFF
		// Move if any tweening
		for (int i = tweenOuts.size() - 1; i >= 0; i --) {
			GameObject o = tweenOuts.get(i);
			// Make it go to upwards yes... follow tweenIn??
			if (tweenIn != null) o.pos.y = tweenIn.pos.y - Main.HEIGHT;
			// ++
			counters.set(i, counters.get(i) + 1);
			if (counters.get(i) > 200) {
				display.models.remove(o);
				tweenOuts.remove(o);
				counters.remove(i);
			}
		}
		if (tweenIn != null) {
			// Approach 0, 0
			tweenIn.vel.y -= tweenIn.pos.y / 20;
			tweenIn.vel.y *= 0.8;
			
			tweenIn.pos.y += tweenIn.vel.y * timeFactor;
		}
		//
		
		
		for (int i = 0; i < display.models.size(); i ++) {
			display.models.get(i).update(delta);
		}
		
		display.clear(Color.BLACK);
		display.render();
		
	}

	  //////////////////YY\\\\\\\\\\\\\\\\\\
	 /////////////////______\\\\\\\\\\\\\\\\\
	///////////////// STATIC \\\\\\\\\\\\\\\\\
	//\\\\\\\\\\\\\\\________/////////////////
	
	
	static private Main instance;
	static public Main getInstance()
	{
		return instance;
	}
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 300;
	public static final int frameRate = 60;
	/** NanoMilliseconds per frame */
	public static final long targetFrameDuration = 1000000000 / frameRate;
	
	public static void main(String[] args) {
		Main m = new Main();
		instance = m;
		
		m.targetFps = frameRate;
		m.run();
	}
}
