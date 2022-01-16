package world;

import java.io.Serializable;

public class GameData implements Serializable {
	private int[][][] data;
	private int[][][] mdata;
	private int index;
	
	public GameData(int[][][] data) {
		this(data, 0);
	}
	
	public GameData(int height, int weight) {
		this(height, weight, 0);
	}
	
	public GameData(int[][][] data, int index) {
		this.data = data;
		this.index = index;
	}
	
	public GameData(int height, int weight, int index) {
		this.index = index;
		this.data = new int[height][weight][2];
		this.mdata = new int[height][weight][2];
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < weight; ++j) {
				data[i][j][0] = 0;
				mdata[i][j][0] = 0;
				data[i][j][1] = 0;
				data[i][j][1] = 0;
			}
		}
	}
	
	void setData(int[][][] data) {
		if(this.data.length == data.length) {
			for(int i = 0; i < this.data.length; ++i) {
				if(this.data[i].length != data[i].length) {
					return;
				}
				for(int j = 0; j < this.data[i].length; ++j) {
					if(this.data[i][j].length != data[i][j].length) {
						return;
					}
					this.mdata[i][j][0] = data[i][j][0];
					this.mdata[i][j][1] = data[i][j][1];
				}
			}
		}
		else {
			return;
		}
		data = mdata;
	}
	
	public int[][][] getData() {
		return data;
	}
	public int getIndex() {
		return index;
	}
}
