package me.nickperry.youtubedlgui;

import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class Main {
	private JFrame frmYoutubedl;
	private JLabel urlLabel;
	private JTextField urlField;
	private JLabel dlLabel;
	private JTextField dlField;
	private JLabel playlistLabel;
	private JToggleButton playlistButton;
	private JLabel formatLabel;
	private JButton submitButton;
	private JPanel panel;
	private JList<String> formatList;

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final Main window = new Main();
					window.frmYoutubedl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		this.initialize();
	}

	private void initialize() {
		(this.frmYoutubedl = new JFrame()).setTitle("Youtube-DL GUI");
		this.frmYoutubedl.setBounds(100, 100, 393, 354);
		this.frmYoutubedl.setResizable(false);
		this.frmYoutubedl.setDefaultCloseOperation(3);
		this.frmYoutubedl.getContentPane().setLayout(null);
		(this.panel = new JPanel()).setBounds(10, 11, 357, 293);
		this.frmYoutubedl.getContentPane().add(this.panel);
		this.panel.setLayout((LayoutManager) new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
		this.dlLabel = new JLabel("Download Path:");
		this.panel.add(this.dlLabel, "2, 2");
		this.dlLabel.setHorizontalAlignment(0);
		this.dlField = new JTextField();
		this.panel.add(this.dlField, "4, 2, fill, fill");
		this.dlField.setColumns(10);
		this.urlLabel = new JLabel("URL:");
		this.panel.add(this.urlLabel, "2, 4");
		this.urlLabel.setHorizontalAlignment(0);
		this.urlField = new JTextField();
		this.panel.add(this.urlField, "4, 4");
		this.urlField.setColumns(10);
		this.playlistLabel = new JLabel("Playlist Download:");
		this.panel.add(this.playlistLabel, "2, 6");
		this.playlistLabel.setHorizontalAlignment(0);
		this.playlistButton = new JToggleButton("Disabled");
		this.panel.add(this.playlistButton, "4, 6");
		this.formatLabel = new JLabel("Format(s):");
		this.panel.add(this.formatLabel, "2, 8");
		final String[] listData = { "3gp", "aac", "flv", "m4a", "mp3", "mp4", "ogg", "wav", "webm" };
		(this.formatList = new JList<String>()).setListData(listData);
		this.panel.add(this.formatList, "4, 8, 1, 2, fill, fill");
		final JLabel copyrightLabel = new JLabel("�Nick Perry 2020");
		this.panel.add(copyrightLabel, "2, 12");
		this.submitButton = new JButton("Submit");
		this.panel.add(this.submitButton, "4, 12, right, default");
		this.submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String url = Main.this.urlField.getText();
				final String dir = Main.this.dlField.getText().equals("") ? System.getProperty("user.home")
						: Main.this.dlField.getText();
				if (url.isBlank()) {
					JOptionPane.showMessageDialog(Main.this.frmYoutubedl, "URL Cannot be blank!", "Required Field!", 0);
					return;
				}
				try {
					new URL(url);
					final List<String> format = Main.this.formatList.getSelectedValuesList();
					if (format.isEmpty()) {
						JOptionPane.showMessageDialog(Main.this.frmYoutubedl, "Select at least 1 Format!",
								"Required Field!", 0);
						return;
					}
					StringBuilder sb = new StringBuilder("cmd /c cd ").append(dir).append(" && yt-dlp");
					for (final String form : format) {
						if (!Main.this.playlistButton.isSelected()) {
							sb.append(" --no-playlist");
						}
						if (isAudio(form)) {
							sb.append(" -x --audio-format ").append(form);
						} else {
							sb.append(" -f ").append(form);
						}
					}
					sb.append(" ").append(url).append(" && exit");
					Process process = Runtime.getRuntime().exec(sb.toString());
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					while (reader.readLine() != null) {}
					reader.close();
					JOptionPane.showMessageDialog(Main.this.frmYoutubedl, "Download Completed!", "Download Completed!",
							-1);
				} catch (MalformedURLException e3) {
					JOptionPane.showMessageDialog(Main.this.frmYoutubedl, "URL is Invalid!", "Invalid URL!", 0);
				} catch (IOException e4) {

				}
			}
		});
		this.playlistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Main.this.playlistButton.setText(Main.this.playlistButton.isSelected() ? "Enabled" : "Disabled");
			}
		});
		this.dlField.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(1);
				final int option = fileChooser.showOpenDialog(Main.this.frmYoutubedl);
				if (option == 0) {
					final File file = fileChooser.getSelectedFile();
					Main.this.dlField.setText(file.getAbsolutePath());
				} else if (option == 1) {
					Main.this.dlField.setText("");
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
			}

			@Override
			public void mouseExited(final MouseEvent e) {
			}
		});
	}

	private static boolean isAudio(final String in) {
		switch (in) {
		case "aac":
		case "flac":
		case "mp3":
		case "m4a":
		case "opus":
		case "vorbis":
		case "wav":
			return true;
		default:
			return false;
		}
	}
}
