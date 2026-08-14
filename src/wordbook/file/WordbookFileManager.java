package wordbook.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class WordbookFileManager {
	// 読み取り用メソッド
	public void loadFiles() {
		Path file = Paths.get("demo.json");
		try {
			List<String> text = Files.readAllLines(file);
			System.out.println(text);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public void saveFiles() {
		Path file = Paths.get("demo.json");
		String separator = System.lineSeparator();
		try {
			Files.writeString(file, separator + "追加テキスト", StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	// JSONを読み込む
	public List<Word> loadJson() throws IOException {
		// 読み込むファイルを指定 // TODO refactor: ファイル名を引数から指定するように変更予定。
		String json = Files.readString(Paths.get("demo.json"));
		
		// 読み込むdataの入れ物を作る
		List<Word> wordList = new ArrayList<>();
		
		
		// 1行ずつ見出しと解説に分ける
		int index = 0;
		while((index = json.indexOf("\"word\": " ,index)) != -1) {
			// 見出しword)を取得する
			int wordStart = json.indexOf("\"", index + 7) + 1;
			int wordEnd = json.indexOf("\"", wordStart);
			String word = json.substring(wordStart, wordEnd);
			
			// 解説(description)を取得する
			int indexDescription = json.indexOf("\"description\": ", wordEnd);
			int descriptionStart = json.indexOf("\"", indexDescription + 13) + 1;
			int descriptionEnd = json.indexOf("\"", descriptionStart);
			String description = json.substring(descriptionStart, descriptionEnd);
			
			// wordListに追加する
			wordList.add(new Word(word, description));
			
			// 次の行へ
			index = descriptionEnd;
		}
		
		return wordList;
	}
}
