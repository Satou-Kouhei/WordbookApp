package wordbook.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import wordbook.file.Word;
import wordbook.file.WordbookFileManager;

public class WordbookApp {
	private static int currentIdx;			// ファイルから何行目を読み込むか
	private static List<Word> wordList;		// 読み込むデータのリスト
	private static File selectedFile;		// 読み込むファイル
	private static String filePath;			// ファイルパスの文字列

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
			JButton addBtn = new JButton("単語追加");
			
			bottomPanel.add(openBtn);
			bottomPanel.add(prevBtn);
			bottomPanel.add(nextBtn);
			bottomPanel.add(addBtn);
			
			frame.add(bottomPanel, BorderLayout.SOUTH);
			
			// フォルダからファイルを選択し、ウィンドウに表示する
			WordbookFileManager fileManager = new WordbookFileManager();
			
			// フォルダからファイルを選択する
			selectedFile = chooseFile(frame);
			filePath = selectedFile.getAbsolutePath();
			
				try {
					// 選択したファイルからリストを取得して、ウィンドウに表示する
					wordList = fileManager.loadJson(filePath);
					updateDisplay(titleLabel, textArea);
					
				} catch (IOException e) {
					titleLabel.setText("選択したファイルは" + selectedFile.getName());
					textArea.setText("無効なファイルが選択されました。「ファイル選択」からファイルを選択し直してください。");
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
			
			//読み込むファイルを選択しなおす
			openBtn.addActionListener(e -> {
				selectedFile = chooseFile(frame);
				filePath = selectedFile.getAbsolutePath();
				currentIdx = 0;
				
				try {
					wordList = fileManager.loadJson(filePath);
					updateDisplay(titleLabel, textArea);
					System.out.println("other file");
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				
			});
			
			// 新しい単語を追加する
			addBtn.addActionListener(showDiarog -> {
				// パネルを作ってダイアログを表示する
				// パネルを作る
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
				
				// ダイアログを作成する
				JDialog diarog = new JDialog();
				diarog.setSize(400, 400);
				diarog.add(inputPanel);
				diarog.setModal(true);
				
				// ラベルと入力欄を作る
				inputPanel.add(new JLabel("単語", SwingConstants.CENTER));
				JTextField newWord = new JTextField(20);
				inputPanel.add(newWord);
				
				inputPanel.add(new JLabel("解説", SwingConstants.CENTER));
				JTextArea newDescription = new JTextArea(5, 20);
				newDescription.setLineWrap(true);
				inputPanel.add(newDescription);
				
				// 追加ボタンを作る
				JButton addWord = new JButton("追加");
				inputPanel.add(addWord);
				
				// 追加したい単語をファイルに書き込む
				addWord.addActionListener(add -> {
					// 新しい単語と解説を取得
					String wordText = newWord.getText();
					String descriptionText = newDescription.getText();
					
					// Word型のデータにして、ファイルの末尾に追加
					Word newData = new Word(wordText, descriptionText);
					wordList.add(newData);
					for(Word word : wordList) {
						System.out.println(word.getWord() + " : " + word.getDescription());
					}
					
					// JSONファイルに書き込み
					try {
						// 書き込みしたら、updateDisplayしてダイアログを消す
						fileManager.saveJson(filePath, wordList);
						updateDisplay(titleLabel, textArea);
						
						diarog.dispose();
						
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					
				});
				
				// ダイアログを表示する
				diarog.setVisible(true);
				
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
	
	/**
	 * フォルダからファイルを選択するメソッド
	 * @param frame
	 * @return filePath
	 */
	private static File chooseFile(JFrame frame) {
		// ファイル選択ダイアログを表示
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSONファイル(*.json)", "json");
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File("."));
		int selected = fileChooser.showOpenDialog(frame);
		
		// ファイルが選択されたら、パスを取得する
		if(selected == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			
		}
		
		return selectedFile;
	}

}
