package cn.tsinghua.simulink.test;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.border.BevelBorder;

public class ParameterTestUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4512747330948525628L;

	private JPanel contentPane;
	private JTextField textField;
	private JTextField value_text_field;

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
				        return f.isDirectory() || name.toLowerCase().endsWith(".mdl") || name.toLowerCase().endsWith(".slx");
					}
				});
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "Choose");
				File file = jfc.getSelectedFile();
				textField.setText(file.getAbsolutePath());
			}
		});
		btnNewButton.setBounds(326, 14, 87, 29);
		contentPane.add(btnNewButton);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(34, 57, 379, 27);
		contentPane.add(comboBox);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 99, 181, 167);
		contentPane.add(scrollPane);

		DefaultComboBoxModel<String> jList1Model = new DefaultComboBoxModel<String>(new String[] { "Value1", "Value2",
				"Value3", "Value4", "Value5", "Value6", "Value7", "Value8", "Value9", "Value10", "Value11" });
		JList<String> myJlist = new JList<String>();
		myJlist.setModel(jList1Model);
		scrollPane.setViewportView(myJlist);

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
		value_text_field.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();				
				if((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == '.'){
					// do nothing, use the default behavior of text field.
				}else{
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

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 10));
		btnDelete.setBounds(86, 78, 66, 21);
		panel.add(btnDelete);

		JButton button = new JButton("Add New Value");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
