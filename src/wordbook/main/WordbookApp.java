package wordbook.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

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
			
			JButton openBtn = new JButton("ファイル選択");
			JButton prevBtn = new JButton("前へ");
			JButton nextBtn = new JButton("次へ");
			
			bottomPanel.add(openBtn);
			bottomPanel.add(prevBtn);
			bottomPanel.add(nextBtn);
			
			frame.add(bottomPanel, BorderLayout.SOUTH);
			
			// フォルダからファイルを選択し、ウィンドウに表示する
			WordbookFileManager fileManager = new WordbookFileManager();
			
			// フォルダからファイルを選択する
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSONファイル(*.json)", "json");
			fileChooser.setFileFilter(filter);
			
			fileChooser.setCurrentDirectory(new File("."));
			int selected = fileChooser.showOpenDialog(frame);
			
			if(selected == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				String filePath = selectedFile.getAbsolutePath();
				
				try {
					// 選択したファイルからリストを取得して、ウィンドウに表示する
					wordList = fileManager.loadJson(filePath);
					updateDisplay(titleLabel, textArea);
					
				} catch (IOException e) {
					titleLabel.setText("選択したファイルは" + selectedFile.getName());
					textArea.setText("無効なファイルが選択されました。「ファイル選択」からファイルを選択し直してください。");
					e.printStackTrace();
				}
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
