package cn.tsinghua.simulink.test;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.conqat.lib.simulink.builder.SimulinkModelBuildingException;

import Extractor.LoadFile;

public class ParameterTestUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4512747330948525628L;

	private JPanel contentPane;
	private JComboBox<String> comboBox;
	private JTextField textField;
	private JTextField value_text_field;
	private DefaultComboBoxModel<Double> listModel = new DefaultComboBoxModel<Double>(
//			new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8", "Value9", "Value10", "Value11" }
	);

	private String selected_input_port = null;

	private int selected_index = -1;

	private Map<String, Set<Double>> classificationTree = new HashMap<>();

	private Random random = new Random();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ParameterTestUI frame = new ParameterTestUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ParameterTestUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setTitle("Simulink Test Generation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(34, 15, 295, 27);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Browse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "*.mdl;*.slx";
					}

					@Override
					public boolean accept(File f) {
						String name = f.getName();
						return f.isDirectory() || name.toLowerCase().endsWith(".mdl")
								|| name.toLowerCase().endsWith(".slx");
					}
				});
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "Choose");
				File file = jfc.getSelectedFile();
				textField.setText(file.getAbsolutePath());
				try {
					classificationTree.clear();
					comboBox.removeAllItems();
					Set<String> in_ports = LoadFile.LoadFromFile(file);
					for (String in_port : in_ports) {
						System.out.println("in_port:" + in_port);
						TreeSet<Double> set = new TreeSet<Double>();
						set.add(new Double(random.nextInt()));
						set.add(new Double(random.nextInt()));
						set.add(new Double(random.nextInt()));
						classificationTree.put(in_port, set);
						comboBox.addItem(in_port);
					}
				} catch (SimulinkModelBuildingException | IOException e1) {
					JOptionPane.showMessageDialog(null, "Error when parsing file!");
//					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(326, 14, 87, 29);
		contentPane.add(btnNewButton);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(34, 57, 379, 27);
		contentPane.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				selected_input_port = (String) e.getItem();
//				System.out.println("e.getItem():" + e.getItem());
				listModel.removeAllElements();
				Set<Double> values = classificationTree.get(selected_input_port);
				for (Double d : values) {
					listModel.addElement(d);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 99, 181, 167);
		contentPane.add(scrollPane);

		JList<Double> myJlist = new JList<Double>();
		myJlist.setModel(listModel);
		scrollPane.setViewportView(myJlist);
//		myJlist.addFocusListener(new FocusListener() {
//			@Override
//			public void focusLost(FocusEvent e) {
//			}
//			@Override
//			public void focusGained(FocusEvent e) {
//			}
//		});
		myJlist.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
//					System.out.println("e.getSource():" + e.getSource());
//					System.out.println("e.getFirstIndex():" + e.getFirstIndex());
//					System.out.println("e.getLastIndex():" + e.getLastIndex());
					Double val = myJlist.getSelectedValue();
//					System.out.println("selected index:" + val);
					textField.setText(val + "");
					selected_index = myJlist.getSelectedIndex();
				}
//				System.out.println(e);
			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(246, 99, 167, 139);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Value Setting");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel.setBounds(15, 15, 117, 21);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Value");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(15, 42, 52, 21);
		panel.add(lblNewLabel_1);

		value_text_field = new JTextField();
		value_text_field.setBounds(69, 42, 83, 21);
		value_text_field.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == '.') {
					// do nothing, use the default behavior of text field.
				} else {
					e.consume();
				}
			}
		});
		panel.add(value_text_field);
		value_text_field.setColumns(10);

		JButton btnModify = new JButton("Modify");
		btnModify.setFont(new Font("Arial", Font.PLAIN, 10));
		btnModify.setBounds(15, 78, 66, 21);
		panel.add(btnModify);
		btnModify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected_input_port != null) {
					if (selected_index > -1) {
						try {
							double d = Double.parseDouble(textField.getText());
							Set<Double> vals = classificationTree.get(selected_input_port);
							if (!vals.contains(d)) {
								vals.remove(listModel.getElementAt(selected_index));
								listModel.removeElementAt(selected_index);
								listModel.insertElementAt(d, selected_index);
								vals.add(d);
							} else {
								JOptionPane.showMessageDialog(null, "The modified double value has already been existed!");
							}
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Error double value!");
						}
					} else {
						JOptionPane.showMessageDialog(null, "No value selected!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "No input port selected yet!");
				}
			}
		});
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 10));
		btnDelete.setBounds(86, 78, 66, 21);
		panel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected_input_port != null) {
					int selected_index_copy = selected_index;
					selected_index = -1;
					if (selected_index_copy > -1) {
						Set<Double> vals = classificationTree.get(selected_input_port);
						vals.remove(listModel.getElementAt(selected_index_copy));
						listModel.removeElementAt(selected_index_copy);
					} else {
						JOptionPane.showMessageDialog(null, "No value selected!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "No input port selected yet!");
				}
			}
		});

		JButton button = new JButton("Add New Value");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected_input_port != null) {
					Set<Double> vals = classificationTree.get(selected_input_port);
					try {
						double d = Double.parseDouble(textField.getText());
						if (!vals.contains(d)) {
							vals.add(d);
							listModel.addElement(d);
						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error double value!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "No input port selected yet!");
				}
			}
		});
		button.setFont(new Font("Arial", Font.PLAIN, 10));
		button.setBounds(15, 103, 137, 21);
		panel.add(button);

		JButton btnNewButton_1 = new JButton("Start Test Generation");
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 12));
		btnNewButton_1.setBounds(246, 253, 167, 29);
		contentPane.add(btnNewButton_1);
	}
}
