package wordbook.file;

public class Word {
	private String word;
	private String description;
	
	// コンストラクタ
	public Word(String word, String description) {
		this.word = word;
		this.description = description;
	}
	
	// ゲッター
	public String getWord() {
		return word;
	}
	
	public String getDescription() {
		return description;
	}
}
