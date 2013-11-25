package Src;

public class EntityBlock extends EntityRocket {

	public EntityBlock(int id) {
		super(id);
		this.Green = 1;
		this.Red = 0;
	}
	public EntityBlock(int id, float x, float y) {
		super(id, x, y);
		this.Green = 1;
		this.Red = 0;
	}

}
