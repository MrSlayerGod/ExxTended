package org.dementhium.model.mask;

/**
 * 
 * @author 'Mystic Flow
 */
public class Graphics {
	
	public static Graphics create(int id) {
		return new Graphics(id);
	}
	
	public static Graphics create(int id, int delay) {
		return new Graphics(id, delay);
	}
	
	public static Graphics create(int id, int delay, int height) {
		return new Graphics(id, delay, height);
	}
	
    private final int delay;
    private final int height;
    private final int id;

    private Graphics(int id) {
        this(id, 0);
    }
 
    private Graphics(int id, int delay) {
        this(id, delay, 0);
    }

    public Graphics(int id, int delay, int height) {
        this.id = id;
        this.delay = delay;
        this.height = height;
    }

    public int getDelay() {
        return delay;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }
}
