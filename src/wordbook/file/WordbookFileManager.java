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
	public List<Word> loadJson(String filePath) throws IOException {
		// 読み込むファイルを指定
		String json = Files.readString(Paths.get(filePath));
		
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
	
	public void saveJson(String pathString, List<Word> wordList) throws IOException {
		// wordListを受け取って、JSONファイルに書き込む
		// StringBuilderを使って文字列を結合する
		StringBuilder updateWordList = new StringBuilder();
		
		// 追加する文字列を作っていく
		// JSONのはじまりの"["
		updateWordList.append("[").append(System.lineSeparator());
		
		// 1行ずつ内容を取得し、結合する
		for(int i = 0; i < wordList.size(); i++) {
			Word data = wordList.get(i);
			updateWordList
				.append("	{")
				.append("\"word\": ")
				.append("\"").append(data.getWord()).append( "\", ")
				.append("\"description\": ")
				.append("\"").append(data.getDescription()).append("\"")
				.append("}");
			
			if(i < wordList.size() - 1) {
				// カンマをつける
				updateWordList.append(",").append(System.lineSeparator());
			} else {
				// 最後はカンマをつけない
				updateWordList.append(System.lineSeparator());
			}
		}
		
		// JSONの終わりの"]"
		updateWordList.append("]");
		
		
		// ファイルを上書きする
		Path filePath = Paths.get(pathString);
		Files.writeString(filePath, updateWordList.toString());
	}
	
	public String createNewFile(String folderPath, String inputFileName) throws IOException {
		// 作成するファイル名を受け取って、新ファイルを作成する
		Path fullPath = null;
		String newFileName = "";
		
		if(folderPath.isEmpty()) {
			// folderPathが空（デフォルトのフォルダを選択）
			Path currentPath = Paths.get(".");		// 注意：これは相対パス
			newFileName = inputFileName + ".json";
			fullPath = currentPath.resolve(newFileName);
		} else {
			// folderPathが空ではないとき（フォルダを選択するとき）
			Path selectedPath = Paths.get(folderPath);
			newFileName = inputFileName + ".json";
			fullPath = selectedPath.resolve(newFileName);
		}
		
		// ファイル作成
		if(Files.exists(fullPath)) {
			System.out.println("同名のファイルは作成できません。");
		} else {
			Files.createFile(fullPath);
		}
		
		String pathString = fullPath.toAbsolutePath().toString();
		return pathString;
	}
}
