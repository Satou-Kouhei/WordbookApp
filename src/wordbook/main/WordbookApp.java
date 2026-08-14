package wordbook.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import wordbook.file.Word;
import wordbook.file.WordbookFileManager;

public class WordbookApp {
	private static int currentIdx;			// ファイルから何行目を読み込むか
	private static List<Word> wordList;		// 読み込むデータのリスト

	public static void main(String[] args) {
		// アプリ用のウィンドウを表示させる
		SwingUtilities.invokeLater(() -> {
			// 表示する画面の設定
			JFrame frame = new JFrame("Wordbook App");
			frame.setSize(400, 400);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocation(100 , 100);
			frame.setLayout(new BorderLayout());
			
			// 画面内部のパーツを配置
			// 1.見出し（上部）
			JLabel titleLabel = new JLabel("見出し", SwingConstants.CENTER);
			frame.add(titleLabel, BorderLayout.NORTH);
			
			// 2.解説等（中央）
			JTextArea textArea = new JTextArea();
			textArea.setLineWrap(true);
			textArea.setEditable(false);
			frame.add(textArea, BorderLayout.CENTER);
			
			// 3.ページ切り替えボタン（下部）
			JPanel bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout());
			
			JButton prevBtn = new JButton("前へ");
			JButton nextBtn = new JButton("次へ");
			
			bottomPanel.add(prevBtn);
			bottomPanel.add(nextBtn);
			
			frame.add(bottomPanel, BorderLayout.SOUTH);
			
			// JSONファイルを読み込んで、ウィンドウに見出しと解説をセット
			WordbookFileManager fileManager = new WordbookFileManager();
			try {
				// ファイル読み込み
				wordList = fileManager.loadJson();
				
				// 表示する見出しと解説をセットする
				updateDisplay(titleLabel, textArea);
				
			} catch (IOException e) {
				titleLabel.setText("エラー");
				textArea.setText("JSONファイルの読み取り失敗");
				e.printStackTrace();
			}
			
			// ページ移動の動作
			// 1.前へ
			prevBtn.addActionListener(e -> {
				if(wordList != null && currentIdx > 0) {
					currentIdx--;
					updateDisplay(titleLabel, textArea);
				}
			});
			
			// 2.次へ
			nextBtn.addActionListener(e -> {
				if(wordList != null && currentIdx < wordList.size() - 1) {
					currentIdx++;
					updateDisplay(titleLabel, textArea);
				}
			});
			
			// 画面を表示
			frame.setVisible(true);
		});
	}
	
	/**
	 * currentIdxから見出しと解説をセットするメソッド
	 * @param label
	 * @param area
	 */
	private static void updateDisplay(JLabel label, JTextArea area) {
		// wordListがなければ早期リターンする
		if(wordList == null || wordList.isEmpty()) return;
		
		// 見出しと解説をセットする
		Word currentWord = wordList.get(currentIdx);
		label.setText(currentWord.getWord());
		area.setText(currentWord.getDescription());
	}

}
