package world;

import java.util.Random;

public class CommonMonster extends CreatureAI{  
	private boolean exit = false;
	
    private World world;
    
    private int target = 1;
    
    private long attackTime;
    
    private long moveTime;
    
    Random ran;
    
    public static final int
   	LEFT_ACTION = 1,
   	RIGHT_ACTION = 2,
   	UP_ACTION = 3,
   	DOWN_ACTION = 4,
   	SLEEP = 5;
    
    private int action;
    
    private int direction;
    
    public CommonMonster(Creature creature, World world) {
        super(creature);
        this.world = world;
        ran = new Random();
        this.action = UP_ACTION;
        this.direction = UP_ACTION;
    }
    
    public boolean See() {
    	
    	return true;
    }
    
    private void move() {
    	target = world.seePlayer(x(), y(), creature.visionRadius());
    	if(target >= 0) {
    		int x = world.getPlayerX(target), y = world.getPlayerY(target);
    		if(x > creature.x()) {
    			if(creature.moveBy(1, 0)) {
    				this.direction = RIGHT_ACTION;
    				return;
    			}
    			else {
    				if(y > creature.y()) {
    					creature.moveBy(0, 1);
        				this.direction = DOWN_ACTION;
    				}
    				else {
    					creature.moveBy(0, -1);
    					this.direction = UP_ACTION;
    				}
    			}
    		}
    		else if(x < creature.x()) {
    			if(creature.moveBy(-1, 0)) {
    				this.direction = LEFT_ACTION;
    				return;
    			}
    			else {
    				if(y > creature.y()) {
    					creature.moveBy(0, 1);
    					this.direction = DOWN_ACTION;
    				}
    				else {
    					creature.moveBy(0, -1);
    					this.direction = UP_ACTION;
    				}
    			}
    		}
    		else{
    			if(y > creature.y()) {
					creature.moveBy(0, 1);
					this.direction = DOWN_ACTION;
				}
				else {
					creature.moveBy(0, -1);
					this.direction = UP_ACTION;
				}
    		}
    	}
    	else {
    		if(action != SLEEP)action = ran.nextInt(4) + 1;
    		for(int i = 0; i < 4; ++i) {
    			int mx = 0, my = 0;
	    		switch(action) {
	    		case LEFT_ACTION:{
	    			mx = -1;
	    			break;
	    		}
	    		case RIGHT_ACTION:{
	    			mx = 1;
	    			break;
	    		}
	    		case UP_ACTION:{
	    			my = -1;
	    			break;
	    		}
	    		case DOWN_ACTION:{
	    			my = 1;
	    			break;
	    		}
	    		default:{
	    			return;
	    		}
	    		}
	    		this.direction = this.action;
	    		if(creature.moveBy(mx, my)) {
	    			break;
	    		}
	    		else {
	    			action = action % 4 + 1;
	    		}
    		}
    		//action = SLEEP;
    	}
    	Graph(direction);
    }
    
    private void Graph(int direction) {
    	short glyph = creature.glyph();
    	switch(direction) {
    	case LEFT_ACTION:
    		if(glyph<7 || glyph>8) {
    			glyph = 7;
    		}
    		else {
    			glyph = (short) ((glyph % 2) + 7);
    		}
    		break;
    	case RIGHT_ACTION:
    		if(glyph<9) {
    			glyph = 9;
    		}
    		else {
    			glyph = (short) ((glyph % 2) + 9);
    		}
    		break;
    	case UP_ACTION:
    		if(glyph>6) {
    			glyph = 5;
    		}
    		else {
    			glyph = (short) ((glyph % 2) + 5);
    		}
    		break;
    	case DOWN_ACTION:
    		if(glyph>6) {
    			glyph = 5;
    		}
    		else {
    			glyph = (short) ((glyph % 2) + 5);
    		}
    		break;
    	}
    	creature.setGlyph(glyph);
    }
    
    public void hunt() {
    	long curtime = System.currentTimeMillis();
    	if(curtime - moveTime > creature.moveSpeed()) {
    		moveTime = curtime;
    		move();
    	}
    	if(target >= 0) {
    		int x = world.getPlayerX(target), y = world.getPlayerY(target);
	    	if(curtime - attackTime > creature.attackSpeed()) {
		    	if((x - creature.x())*(x - creature.x())+((y - creature.y())*(y - creature.y())) < 2) {
		    		attackTime = curtime;
		    		world.attackPlayer(target, creature.attackValue());
		    	}
	    	}
    	}
    }
    
    public void shutDown() {
    	exit = true;
    }

	@Override
	public void run() {
		while(!exit) {
			try {
				Thread.sleep(100);
				hunt();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beAttacked(int attackValue) {
		Creature.attack(creature, attackValue);
	}

	@Override
	public void setAttack(boolean b) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void setDirection(int direction) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void setJump(boolean k) {
		// TODO 自动生成的方法存根
		
	}
}
