package bookshelf.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import bookshelf.BookFactory;
import bookshelf.Database;
import bookshelf.TypeFactory;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BookShelfUI extends JFrame {
	Database data;
	JTextField searchText;
	JLabel searchLabel, logoLabel, backGroundLabel, binLabel, pageLabel;
	JButton addFolder, addImageadd, right, left;
	JPanel panel1, panel2, panelAdd, panelText, panelButton, panelAll,
			panelChange;
	// JScrollPane scrollPane;
	Image logo, backGround, addImage, addType, delete;
	boolean direction = true;
	String newFolder;
	int numFol = 0;
	boolean isRemoveState;
	int currentPage = 1;
	int havePage = 0;

	public BookShelfUI() throws IOException {
		super("Book-Shelf");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initcomponents();
	}

	public void initcomponents() throws IOException {
		this.setLocation(230, 20);
		this.setLayout(new FlowLayout());

		getRootPane().setBorder(
				BorderFactory.createMatteBorder(4, 4, 4, 4, Color.orange));
		logo = ImageIO.read(new File("Picture//Logo.png"));
		addImage = ImageIO.read(new File("add.png"));
		addType = ImageIO.read(new File("Picture//AddBook.png"));
		delete = ImageIO.read(new File("Picture//bin.png"));
		backGround = ImageIO.read(new File("backG.jpg"));

		addFolder = new JButton();
		addImageadd = new JButton();
		right = new JButton("RIGHT");
		left = new JButton("LEFT");

		logoLabel = new JLabel(new ImageIcon(logo.getScaledInstance(110, 90,
				Image.SCALE_DEFAULT)));

		addFolder.setIcon(new ImageIcon(addImage.getScaledInstance(110, 90,
				Image.SCALE_DEFAULT)));
		addImageadd.setIcon(new ImageIcon(addType.getScaledInstance(100, 80,
				Image.SCALE_DEFAULT)));
		binLabel = new JLabel(new ImageIcon(delete.getScaledInstance(110, 90,
				Image.SCALE_DEFAULT)));

		panel1 = new JPanel();
		panel2 = new JPanel();
		panelAdd = new JPanel();
		panelText = new JPanel();
		panelButton = new JPanel();
		panelChange = new JPanel();
		panelAll = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backGround, 0, 0, null);
			};
		};
		// scrollPane = new JScrollPane(panelButton);

		searchLabel = new JLabel("SEARCH : ");
		searchLabel.setFont(new Font("Apple Casual", 1, 20));
		searchLabel.setForeground(Color.WHITE);
		searchText = new JTextField(15);
		searchText.addFocusListener(new FocusListener() {
			String promptText = "What you need ?";

			@Override
			public void focusLost(FocusEvent e) {
				if (searchText.getText().isEmpty()) {
					searchText.setText(promptText);
					searchText.setForeground(Color.gray);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (searchText.getText().equals(promptText)) {
					searchText.setText("");
					searchText.setForeground(Color.BLACK);
				}
			}
		});
		panelText.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelText.setBackground(Color.BLACK);
		panelText.add(logoLabel);
		panelText.add(searchLabel);
		panelText.add(searchText);

		data = new Database();
		for (String type : TypeFactory.getInstances().getTypeList())
			addNewFolder(type);

		addFolder.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 0));
		addImageadd.setBorder(BorderFactory.createEmptyBorder(0, 20, 60, 0));
		binLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));

		panelAdd.add(addFolder);
		panelAdd.add(addImageadd);
		panelAdd.add(binLabel);
		panelAdd.setLayout(new BoxLayout(panelAdd, BoxLayout.Y_AXIS));
		// panelAdd.setLayout(new FlowLayout());
		panelAdd.setBackground(Color.BLACK);
		addImageadd.setBorderPainted(false);
		addImageadd.setContentAreaFilled(false);
		addFolder.setBorderPainted(false);
		addFolder.setContentAreaFilled(false);

		addImageadd.addActionListener((e) -> addType("", "", "", ""));
		addFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFolder = JOptionPane.showInputDialog("Folder Name");
				if (newFolder == null)
					return;
				if (isAlphaNumeric(newFolder) && !newFolder.isEmpty() && !TypeFactory.getInstances().getTypeList()
						.stream().filter((s) -> s.equalsIgnoreCase(newFolder)).findFirst().isPresent()) {

					addNewFolder(newFolder);
					TypeFactory.getInstances().addType(newFolder);
					data.close();
				}
			}
		});

		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel1.setBorder(new EmptyBorder(0, 30, 30, 30));
		panel2.setBorder(new EmptyBorder(0, 30, 30, 30));
		// panelButton.setLayout(new FlowLayout());
		// panelButton.add(panel1, BorderLayout.CENTER);
		// panelButton.add(panel2, BorderLayout.CENTER);
		panelButton.setLayout(new GridLayout(4, 2));
		// scrollPane.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,
		// Color.BLACK));
		panelButton.setPreferredSize(new Dimension(600, 500));

		// panelText.setOpaque(false);
		panelAdd.setOpaque(false);
		panel1.setOpaque(false);
		panel2.setOpaque(false);
		panelButton.setOpaque(false);
		// scrollPane.getViewport().setOpaque(false);
		// scrollPane.setOpaque(false);

		right.addActionListener(changePage());
		left.addActionListener(changePage());
		panelChange.add(left);
		pageLabel = new JLabel("Page : " + currentPage);
		panelChange.add(pageLabel);
		panelChange.add(right);

		panelAll.setLayout(new BorderLayout());
		panelAll.add(panelText, BorderLayout.NORTH);
		panelAll.add(panelButton, BorderLayout.CENTER);
		panelAll.add(panelAdd, BorderLayout.EAST);
		panelAll.add(panelChange, BorderLayout.SOUTH);
		panelAll.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 20));

		this.add(panelAll);
		this.pack();
	}

	public void addNewFolder(String newFolder) {
		if (numFol >= 8)
			return;
		JButton newButton = new JButton(newFolder);
		newButton.setFont(new Font("Rockwell", 0, 20));
		try {
			Image img = ImageIO.read(new File("Picture//folder.png"))
					.getScaledInstance(120, 120, Image.SCALE_DEFAULT);
			newButton.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newButton.setHorizontalTextPosition(SwingConstants.CENTER);
		// if (direction)
		// panel1.add(newButton);
		// else
		// panel2.add(newButton);
		direction = !direction;
		panelButton.add(newButton);
		newButton.setBorderPainted(false);
		newButton.setContentAreaFilled(false);
		newButton.addActionListener(pressButton());
		numFol++;
	}

	public ActionListener pressButton() {
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				FolderPageUI folder = new FolderPageUI(button.getText());
				folder.run();
			}
		};
		return action;
	}

	public void addType(String aName, String aType, String aLocation, String aDescription) {
		String[] typeArr = {};
		typeArr = TypeFactory.getInstances().getTypeList().toArray(typeArr);
		JComboBox<String> cBox = new JComboBox<>(typeArr);
		JButton browse = new JButton("BROWSE");
		JTextField textName = new JTextField(20);
		JTextField textLoc = new JTextField(10);
		JTextArea textDesc = new JTextArea(5, 20);
		textName.setText(aName);
		textDesc.setText(aDescription);
		textLoc.setText(aLocation);
		JScrollPane scrollPane = new JScrollPane(textDesc);
		textDesc.setLineWrap(true);
		browse.addActionListener((e) -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter(
					"PDF or read file", "pdf", "txt", "doc", "docx", "ppt",
					"pptx"));
			int result = chooser.showOpenDialog(BookShelfUI.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				textLoc.setText(file.getPath());
				textName.setText(file.getName());
			}
		});
		JPanel panel = new JPanel();
		JPanel panelName = new JPanel();
		JPanel panelLoca = new JPanel();
		JPanel panelDes = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panelName.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLoca.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDes.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(cBox);
		panelName.add(new JLabel("Name:"));
		panelName.add(textName);
		panelLoca.add(new JLabel("Location:"));
		panelLoca.add(textLoc);
		panelLoca.add(browse);
		panelDes.add(new JLabel("Description:"));
		panelDes.add(scrollPane);
		panel.add(panelName);
		panel.add(panelLoca);
		panel.add(panelDes);

		int choose = JOptionPane.showConfirmDialog(null, panel, "Add a Book",
				JOptionPane.OK_CANCEL_OPTION);

		if (JOptionPane.OK_OPTION == choose) {
			String name = textName.getText();
			String type = cBox.getSelectedItem().toString();
			String location = textLoc.getText();
			String description = textDesc.getText();

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"You forget to input a name !");
				addType(name, type, location, description);
			} else if (location.trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Invalid Path File");
				addType(name, type, location, description);
			} else if (description.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"You forget to input a description !");
				addType(name, type, location, description);
			} else {
				BookFactory.getInstances().add(name, type, location, description);
				data.close();
			}
		}
	}

	public boolean isAlphaNumeric(String s) {
		for (char c : s.toCharArray())
			if (!(Character.isLetter(c) || Character.isDigit(c)))
				return false;
		return true;
	}

	public ActionListener changePage() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				isRemoveState = false;
				numFol = 0;
				JButton pressButton = (JButton) e.getSource();
				if (pressButton == right) {
					left.setEnabled(true);
					currentPage++;
					updateFrame();
				} else if (pressButton == left) {
					right.setEnabled(true);
					currentPage--;
					updateFrame();
				}
			}
		};
	}

	public void updateFrame() {
		havePage = (int) Math.ceil(data.getTypeList().size() / 8.0);
		pageLabel.setText("Page : " + currentPage);
		panelButton.removeAll();
		panelButton.repaint();
		boolean lastPage = currentPage * 8 + 7 >= data.getTypeList().size() - 1;
		System.out.println(currentPage);
		if (!lastPage) {
			for (String nameType : data.getTypeList().subList(currentPage * 8,
					currentPage * 8 + 7)) {
				System.out.println(nameType);
				addNewFolder(nameType);
			}
		} else {
			for (String nameType : data.getTypeList().subList(
					(currentPage - 1) * 8, data.getTypeList().size())) {
				System.out.println(nameType);
				addNewFolder(nameType);
			}
		}

		if (currentPage <= 1) {
			left.setEnabled(false);
		}
		if (currentPage + 1 > havePage) {
			right.setEnabled(false);
		}

		this.pack();
	}

	public void run() {
		this.setVisible(true);
	}
}