
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

//7P6971363CCA002

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	String ru4noDopalvane;
	int firstSampleEndIndex;
	int secondSampleEndIndex;
	static String dopalniSample;
	static String getMatchCode;
	String firstSample;
	String secondSample;
	int dopalniBroi;
	boolean dobavqneNaPaketaj;

	int scanTime;

	int harnessCounter;
	// promenlivi za vastanovqvane ot sebe si
	int OK_StatFIleByMonthLength;
	
	Scanner scan, scanStat;
	BufferedReader reader;
	static BufferedReader readerStat;
	FileReader read;
	// Deklaracia na promenliva za path kum Statistikata
	static FileReader readStat;

	static String pathToStatistic, USER_NUMBER;

	public static boolean print;

	int x, y;

	int comPortSet;

	int countValue;

	JButton reset, exit;

	JFrame frame;

	StringBuilder etiketKraen;

	static JTextField taileNumberField;
	JTextField piecesField, packField, userField;

	StringBuilder arrIni = null;

	StringBuilder packDataFileArray = null;

	File fileIni;

	File paketajFile;

	Scanner scanIni, scanEtiKraen;

	Scanner scanPaketaj;

	String comPort, etiket;

	int sleepValue;
	static JTextField fieldDoplni;
	private Scanner scanPaketajArr;
	private int daljinaPaketaj;
	private String[] paketajArr;
	Integer daljinaBroi;
	String getDoplani;

	public class Error {

		JFrame errFrame;

		JTextField errField;

		public void printErr() {
			print = false;
			errFrame = new JFrame("Error");
			errFrame.setLocationRelativeTo(null);
			errFrame.setSize(400, 80);
			errFrame.setVisible(true);
			errFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			errFrame.setResizable(false);
			errFrame.setLayout(null);

			errField = new JTextField();
			errField
					.setText("File C:/SEBN_SHIPPING_LABEL/Settings.ini not found!");
			errFrame.add(errField);
			errField.setBounds(0, 0, 400, 50);
			errField.setBackground(Color.RED);
			errField.setForeground(Color.yellow);

		}

	}

	GUI() {

		// Iniciailzacia na kraen etiket

		dobavqneNaPaketaj = false;
		File etiFile1 = new File("C:\\SEBN_SHIPPING_LABEL\\LCheck_KRAEN.txt");

		try {
			scanEtiKraen = new Scanner(etiFile1, "windows-1252");
		} catch (FileNotFoundException e1) {

			System.out.println("Nqma etiket");
		}

		etiketKraen = new StringBuilder();
		while (scanEtiKraen.hasNextLine()) {

			etiketKraen.append(scanEtiKraen.nextLine());

		}

		etiket = String.valueOf(etiketKraen);

		// Inicializacia na ini faila s nastroikite

		arrIni = new StringBuilder();

		packDataFileArray = new StringBuilder();

		fileIni = new File("C:\\SEBN_SHIPPING_LABEL\\Settings.ini");

		paketajFile = new File("C:\\SEBN_SHIPPING_LABEL\\PACK_DATA.txt");

		try {
			scanIni = new Scanner(fileIni);
		} catch (FileNotFoundException e1) {
			new Error().printErr();
			e1.printStackTrace();
		}

		while (scanIni.hasNextLine()) {

			arrIni.append(scanIni.nextLine());
		}
		scanIni.close();
		// Inicializacia na faila s paketaja
		try {
			scanPaketaj = new Scanner(paketajFile);
		} catch (FileNotFoundException e1) {
			new Error().printErr();
			e1.printStackTrace();
		}

		while (scanPaketaj.hasNextLine()) {
			daljinaPaketaj++;
			packDataFileArray.append(scanPaketaj.nextLine().trim());
		}
		scanPaketaj.close();
		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// izvli4ane na path kam statistikata /

		int startLength3 = arrIni.indexOf("01<");

		int endLength3 = arrIni.indexOf(">01");

		pathToStatistic = arrIni.substring(startLength3 + 3, endLength3).trim();

		// ////////////////////////////////////////////////////////////////////////////////////////////////
		// Izvli4ane na COM port nomer
		// izvli4ane na path kam statistikata /

		// Izvli4ane na sleep time za printirane

		int startLength4 = arrIni.indexOf("03<");

		int endLength4 = arrIni.indexOf(">03");

		String sleepTime = arrIni.substring(startLength4 + 3, endLength4)
				.trim();

		sleepValue = Integer.valueOf(sleepTime);

		// izvli4ane na promenliva za pozicia na ekrana y allow harness

		int startLength5 = arrIni.indexOf("04<");

		int endLength5 = arrIni.indexOf(">04");

		String posY = arrIni.substring(startLength5 + 3, endLength5).trim();

		int posYInt = Integer.valueOf(posY);

		// izvli4ane na promenliva za pozicia na ekrana x

		int startLength6 = arrIni.indexOf("05<");

		int endLength6 = arrIni.indexOf(">05");

		String posX = arrIni.substring(startLength6 + 3, endLength6).trim();

		int posXInt = Integer.valueOf(posX);

		// Izvli4ane na daljina na PCS: format na broikite

		int startLength8 = arrIni.indexOf("07<");

		int endLength8 = arrIni.indexOf(">07");

		String lengthPCS = arrIni.substring(startLength8 + 3, endLength8)
				.trim();

		daljinaBroi = Integer.valueOf(lengthPCS);

		// razre6enie za dplalvane

		int startLength9 = arrIni.indexOf("08<");

		int endLength9 = arrIni.indexOf(">08");

		ru4noDopalvane = arrIni.substring(startLength9 + 3, endLength9).trim();

		// izvli4ane na promenliva

		// izvli4ane na promenliva za pozicia na ekrana x

		int startLength7 = arrIni.indexOf("06<");

		int endLength7 = arrIni.indexOf(">06");

		String timeScan = arrIni.substring(startLength7 + 3, endLength7).trim();

		scanTime = Integer.valueOf(timeScan);

		// Timer za printirane -sleep timer

		sleepValue = Integer.valueOf(sleepTime);

		// vzemane na com port

		int startLength2 = arrIni.indexOf("02<");

		int endLength2 = arrIni.indexOf(">02");

		comPort = arrIni.substring(startLength2 + 3, endLength2).trim();

		// System.out.println(comPort);

		frame = new JFrame(" –¿≈Õ ≈“» ≈“");

		frame.setResizable(false);

		frame.setSize(580, 90);

		frame.setVisible(true);

		frame.setAlwaysOnTop(true);

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

		frame.setLayout(null);

		frame.setBounds(posYInt, posXInt, 605, 50);

		// //////////////////////////////////////////////////////////////////

		taileNumberField = new JTextField();

		taileNumberField.setFont(new Font("SansSerif", Font.BOLD, 10));

		frame.add(taileNumberField);

		taileNumberField.setBounds(0, 0, 155, 18);

		taileNumberField.setEditable(false);

		piecesField = new JTextField();

		piecesField.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 10));

		frame.add(piecesField);

		piecesField.setBounds(200, 0, 35, 18);

		piecesField.setEditable(false);

		reset = new JButton("1/2 œ¿ ≈“¿∆");

		reset.setFont(new Font("SansSerif", Font.BOLD, 10));

		reset.setBackground(Color.yellow);

		frame.add(reset);

		reset.setBounds(390, 0, 100, 18);

		reset.setMnemonic('P');

		reset.addActionListener(new Reset());

		JLabel broiki = new JLabel("¡ÓÈÍË=");

		broiki.setForeground(Color.RED);

		broiki.setFont(new Font("SansSerif", Font.BOLD, 10));

		frame.add(broiki);

		broiki.setBounds(156, 0, 60, 20);

		packField = new JTextField();

		packField.setEditable(false);

		frame.add(packField);

		packField.setBounds(235, 0, 80, 18);

		packField.setBackground(Color.cyan);

		packField.setFont(new Font("SansSerif", Font.BOLD, 11));

		// Pole koeto vizualizira user nomer
		userField = new JTextField();

		userField.setEditable(false);

		frame.add(userField);

		userField.setBounds(315, 0, 75, 18);

		userField.setBackground(Color.green);

		userField.setFont(new Font("SansSerif", Font.BOLD, 10));

		// Pole za vuvejdane na polovin paketaj
		fieldDoplni = new JTextField();

		fieldDoplni.setEditable(true);

		frame.add(fieldDoplni);

		fieldDoplni.setBounds(490, 0, 110, 18);

		fieldDoplni.setBackground(Color.green);

		fieldDoplni.setFont(new Font("SansSerif", Font.BOLD, 10));
		// scanirane na paketaj File za masiv ot String

		try {
			scanPaketajArr = new Scanner(paketajFile);
		} catch (FileNotFoundException e1) {
			new Error().printErr();
			e1.printStackTrace();
		}
		// Inicializacia na masiva za paketaj
		paketajArr = new String[daljinaPaketaj];
		int z = 0;
		while (scanPaketajArr.hasNextLine()) {
			paketajArr[z] = scanPaketajArr.nextLine().trim();
			z++;
		}

		// System.out.println( paketajArr[1]);

		final JButton dopalniButon = new JButton("Dopalni");
		frame.add(dopalniButon);
		dopalniButon.setBounds(350, 25, 100, 25);

		dopalniButon.addActionListener(new Dopalvane());
		// Pravi vazmojno vavejdaneto s enter
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW

		).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickButton");

		frame.getRootPane().getActionMap().put("clickButton",
				new AbstractAction() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					/**
		 	 * 
		 	 */

					public void actionPerformed(ActionEvent ae) {
						dopalniButon.doClick();
						System.out.println("button clicked");
					}
				});
	}

	public class Reset implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			print = true;

		}

	}

	public class Dopalvane implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			try {
				getDoplani = fieldDoplni.getText().trim();

				dopalniSample = getDoplani.substring(0, (getDoplani.length()
						- daljinaBroi - 1));

				dopalniBroi = Integer.valueOf(getDoplani.substring(getDoplani
						.length()
						- daljinaBroi - 1, getDoplani.length()));
				System.out.println(dopalniSample);
				System.out.println(dopalniBroi);

				// obhojdane na masiva za sravnenie na dopalni texta s paketaja

				for (int count = 0; count <= paketajArr.length - 1; count++) {
					int startPaketaj = paketajArr[count].trim().length() - 16;
					int endPaketaj = paketajArr[count].trim().length() - 1;
					String temp = paketajArr[count].substring(startPaketaj,
							endPaketaj).trim();

					// System.out.println( temp);
					if (temp.equals(dopalniSample)) {
						getMatchCode = paketajArr[count].substring(0,
								paketajArr[count].length() - 26).trim();
						dobavqneNaPaketaj = true;

					} else {
						// break;
					}

				}
			} catch (Exception e) {
				fieldDoplni.setBackground(Color.red);
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			if (dobavqneNaPaketaj != true) {
				fieldDoplni.setText("");
			}

			// System.out.println( getMatchCode);

		}

	}

}