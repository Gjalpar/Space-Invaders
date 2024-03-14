package SpaceInvaders;

public class Score implements Comparable<Score>{
	private String username;
	private int score;

	public Score(String username, int score){
		this.username = username;
		this.score = score;
	}

	@Override
	public int compareTo(Score point){
		if(score > Integer.parseInt(point.getScore()))
			return -1;

		if(score < Integer.parseInt(point.getScore()))
			return 1;

		return 0;
	}

	public String getScore(){
		String point = "";

		for (int i = 6; i >= 0; i--)
			point = point + (int)(score / Math.pow(10, i)) % 10;

		return point;
	}

	public String getUsername(){
		return username;
	}
}