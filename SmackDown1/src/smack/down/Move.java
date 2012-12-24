package smack.down;

public interface Move {
	public String getName();
	public String getDescription();
	public boolean isPlayable(Player player);
	public void play(Player player, Callback callback);
}
