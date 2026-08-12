package wordbook.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import wordbook.file.WordbookFileManager;

public class WordbookApp {

	public static void main(String[] args) {
		// TODO アプリ用のウィンドウを表示させる
		SwingUtilities.invokeLater(() -> {
			// 表示する画面の設定
			JFrame frame = new JFrame("Wordbook App");
			frame.setSize(400, 400);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocation(100 , 100);
			
			// 画面内部のパーツを配置
			WordbookFileManager wfm = new WordbookFileManager();
			wfm.loadFiles();
			wfm.saveFiles();
			wfm.loadFiles();
			
			// 画面を表示
			frame.setVisible(true);
		});
		
	}

}
