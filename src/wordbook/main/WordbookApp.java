package wordbook.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class WordbookApp {

	public static void main(String[] args) {
		// TODO アプリ用のウィンドウを表示させる
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
			JLabel textArea = new JLabel("解説", SwingConstants.CENTER);
			frame.add(textArea, BorderLayout.CENTER);
			
			// 3.ページ切り替え（下部）
			JPanel bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout());
			
			JButton prevBtn = new JButton("前へ");
			JButton nextBtn = new JButton("次へ");
			
			bottomPanel.add(prevBtn);
			bottomPanel.add(nextBtn);
			
			frame.add(bottomPanel, BorderLayout.SOUTH);
			
			// 画面を表示
			frame.setVisible(true);
		});
		
	}

}
