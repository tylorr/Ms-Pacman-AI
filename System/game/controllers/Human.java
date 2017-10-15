package game.controllers;

import game.models.Game;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Allows a human player to play the game using the arrow key of the keyboard.
 */
public final class Human extends KeyAdapter implements HeroController
{
    private int key;
    private int action;

    public void init() { }

    public void update(Game game,long dueTime)
    {
    	if (key == KeyEvent.VK_UP)
            action = 0;
    	else if (key == KeyEvent.VK_RIGHT)
            action = 1;
        else if (key == KeyEvent.VK_DOWN)
            action = 2;
        else if (key == KeyEvent.VK_LEFT)
            action = 3;
    }

    public void shutdown() { }

    public int getAction()
    {
        return action;
    }

    public void keyPressed(KeyEvent e)
    {
        key=e.getKeyCode();
    }
}