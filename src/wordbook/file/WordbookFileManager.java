package wordbook.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class WordbookFileManager {
	// 読み取り用メソッド
	public void loadFiles() {
		Path file = Paths.get("demo.txt");
		try {
			List<String> text = Files.readAllLines(file);
			System.out.println(text);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public void saveFiles() {
		Path file = Paths.get("demo.txt");
		String separator = System.lineSeparator();
		try {
			Files.writeString(file, separator + "追加テキスト", StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
