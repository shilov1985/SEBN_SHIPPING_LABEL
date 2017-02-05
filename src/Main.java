import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class Main extends GUI {

	private static final long serialVersionUID = 1L;

	File iniFile, OK_StatFileByMonth = null;

	StringBuilder fixedPCS_Variable;

	String packValueFromPACK_DATAfile, repeatedVariantPack;

	String pathToFoundOKstatFileByMonth;

	String OK_StatFileFullNameByMonth;
	Enumeration<?> portList = null;
	OutputStream outputStream = null;
	CommPortIdentifier portId;
	SerialPort serialPort = null;

	Main() {

		Timer scanStatistiTtimer = new Timer();

		scanStatistiTtimer.schedule(new CrunchifyReminder(), 0, scanTime);

	}

	// Infinite run class
	class CrunchifyReminder extends TimerTask {

		String[] OK_StatFileByMonthStringArray;

		String DateToString;

		public void run() {

			fieldDoplni.setBackground(Color.CYAN);

			iniFile = new File(pathToStatistic);

			Date date = new Date();

			// Here takes only month
			SimpleDateFormat format = new SimpleDateFormat("MM");

			// initialization of "DateToString" with only the month
			DateToString = format.format(date);

			// Here forms sample for searching ok.txt files in statistic
			// folder.We search by month + OK files type.
			OK_StatFileFullNameByMonth = DateToString + "OK.txt";

			// Here check by this method, if OK.txt statistic file have
			// founded,and if OK.txt file
			// have founded (by month),return the full path to this file.
			pathToFoundOKstatFileByMonth = getPathToOK_txtFile(pathToStatistic,
					OK_StatFileFullNameByMonth);
			OK_StatFileByMonth = new File(pathToFoundOKstatFileByMonth);

			// Here implement method which return the length of some text file
			OK_StatFIleByMonthLength = getLengthOfTextFile(pathToFoundOKstatFileByMonth);

			try {

				// Here initialize the array for the OK.txt file
				OK_StatFileByMonthStringArray = turnTextFileToArray(pathToFoundOKstatFileByMonth);

			} catch (Exception e1) {

				// Here run the code above again because some time have
				// exceptions
				System.out.println("Regenerate");
				// Here forms sample for searching ok.txt files in statistic
				// folder.We search by month + OK files type.
				OK_StatFileFullNameByMonth = DateToString + "OK.txt";

				// Here check by this method, if OK.txt statistic file have
				// founded,and if OK.txt file
				// have founded (by month),return the full path to this file.
				pathToFoundOKstatFileByMonth = getPathToOK_txtFile(
						pathToStatistic, OK_StatFileFullNameByMonth);

				OK_StatFileByMonth = new File(pathToFoundOKstatFileByMonth);

				// Here implement method which return the length of some text
				// file
				OK_StatFIleByMonthLength = getLengthOfTextFile(pathToFoundOKstatFileByMonth);

				// Here initialize the array for the OK.txt file
				OK_StatFileByMonthStringArray = turnTextFileToArray(pathToFoundOKstatFileByMonth);

			}

			harnessCounter = 1;

			// Iterate over "OK_StatFileByMonthStringArray" and check how many
			// rows are repeated
			// Count variants.

			for (int rowsCounter = OK_StatFileByMonthStringArray.length - 1; rowsCounter >= 1; rowsCounter--) {

				firstSampleEndIndex = OK_StatFileByMonthStringArray[rowsCounter]
						.indexOf(";");

				secondSampleEndIndex = OK_StatFileByMonthStringArray[rowsCounter - 1]
						.indexOf(";");

				firstSample = OK_StatFileByMonthStringArray[rowsCounter]
						.substring(0, firstSampleEndIndex);

				secondSample = OK_StatFileByMonthStringArray[rowsCounter - 1]
						.substring(0, secondSampleEndIndex);

				// Check do we have repeated variants(rows).
				if (firstSample.equals(secondSample)) {

					// Harness counter.This variable count all repeated variants
					harnessCounter++;

					// Here takes the user number from the last row ONLY
					USER_NUMBER = OK_StatFileByMonthStringArray[rowsCounter
							+ harnessCounter - 2].substring(
							OK_StatFileByMonthStringArray[rowsCounter
									+ harnessCounter - 2].length() - 5,
							OK_StatFileByMonthStringArray[rowsCounter
									+ harnessCounter - 2].length());

				} else {
					if (ru4noDopalvane.equalsIgnoreCase("false")) {
						fieldDoplni.setText("");

					}
					// Here have exception if have only one row,
					// no repeated rows
					USER_NUMBER = OK_StatFileByMonthStringArray[rowsCounter
							+ harnessCounter - 1].substring(
							OK_StatFileByMonthStringArray[rowsCounter
									+ harnessCounter - 1].length() - 5,
							OK_StatFileByMonthStringArray[rowsCounter
									+ harnessCounter - 1].length());

					break;
				}
			}

			userField.setText("USER=" + USER_NUMBER);

			taileNumberField.setText(" вариант =  " + firstSample);

			packField.setText(packValueFromPACK_DATAfile);

			// Here check do we have information about pack of the variant
			int start = packDataFileArray.indexOf(firstSample);

			if (start != -1) {

				// Here takes the pack from "packValueFromPACK_DATAfile".
				repeatedVariantPack = packDataFileArray.substring(
						start + firstSample.length() + 10,
						start + firstSample.length() + 25).trim();
				packValueFromPACK_DATAfile = packDataFileArray.substring(
						start + firstSample.length() + 6,
						start + firstSample.length() + 9).trim();

			} else {

				// If have no information about pack for some variant
				// catch exception here.
				try {
					Thread.sleep(1000);
					taileNumberField.setText("Bъведи пакeтаж");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			packField.setText("Пакетаж=" + packValueFromPACK_DATAfile);

			int packValueFromPACK_DATAfileInteger = Integer
					.valueOf(packValueFromPACK_DATAfile);

			fixedPCS_Variable = new StringBuilder();
			fixedPCS_Variable.append(String.valueOf(harnessCounter));

			// Here implement method to format PCS to be printed on
			// shipping label
			fixedPCS_Variable = formatPSC(fixedPCS_Variable, daljinaBroi);

			// Add PSC info to the frame
			piecesField.setText(String.valueOf(fixedPCS_Variable));

			// Condition for automatic print shipping label
			if (harnessCounter == packValueFromPACK_DATAfileInteger) {

				print = true;

			}

			// Here check if we have shield row start to count from zero.
			if (firstSample.equals("PRINTIRANKRAEN")) {
				packField.setText("Пакетаж=");

				taileNumberField.setText("Вариант=");

				userField.setText("USER=");

				piecesField.setText("");

				print = false;

			}

			fieldDoplni.setEditable(true);

			if (USER_NUMBER.equals("DOPAL")) {

				fieldDoplni.setText("допълнен пакетаж");
				fieldDoplni.setEditable(false);
				print = false;
				reset.setBackground(Color.red);

			}

			date = new Date();

			SimpleDateFormat formatA = new SimpleDateFormat("dd.MM.yyyy");

			DateToString = formatA.format(date);

			String packing = String.valueOf(fixedPCS_Variable);
			String ETI_Kraen = etiket.replace("@USER", USER_NUMBER);
			String ETI_Kraen1 = ETI_Kraen.replace("@Var1", repeatedVariantPack);
			String ETI_Kraen2 = ETI_Kraen1.replace("@COUNTER", packing);
			String ETI_Kraen3 = ETI_Kraen2.replace("@DATE", DateToString);

			
			
			//Code below print the shipping label
			if (print) {

			
				

				taileNumberField.setText("ПРИНТИРАНЕ...");
			//Print latency 
				try {
					Thread.sleep(sleepValue);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				portList = CommPortIdentifier.getPortIdentifiers();

				while (portList.hasMoreElements()) {
					portId = (CommPortIdentifier) portList.nextElement();
					if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						if (portId.getName().equals(comPort)) {
							// if (portId.getName().equals("/dev/term/a")) {

							try {
								serialPort = (SerialPort)

								portId.open("SimpleWriteApp", 2000);
							} catch (PortInUseException b) {
								taileNumberField.setFont(new Font("SansSerif",
										Font.BOLD, 10));
								taileNumberField.setBackground(Color.red);
								taileNumberField
										.setText("СОМ портът не е наличен!");
								try {
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							try {
								outputStream = serialPort.getOutputStream();
							} catch (IOException b) {
								taileNumberField.setFont(new Font("SansSerif",
										Font.BOLD, 10));
								taileNumberField.setBackground(Color.red);
								taileNumberField
										.setText("Не мога да отворя поток към ПРИНТЕРА!");
								try {
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							try {
								serialPort.setSerialPortParams(9600,
										SerialPort.DATABITS_8,
										SerialPort.STOPBITS_2,
										SerialPort.PARITY_NONE);
							} catch (UnsupportedCommOperationException b) {
								taileNumberField.setFont(new Font("SansSerif",
										Font.BOLD, 10));
								taileNumberField.setBackground(Color.red);
								taileNumberField
										.setText("Невалидни параметри на СОМ порта!");

								try {
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							try {
								outputStream.write(ETI_Kraen3.getBytes());
								outputStream.flush();
							} catch (IOException b) {
							}
						
                             serialPort.close();
							
							//After print write shield row in statistic file
                            //to start count from zero
							try {
								FileWriter write = new FileWriter(
										OK_StatFileByMonth, true);

								PrintWriter out = new PrintWriter(write, true);
								
                             //This is the shield row
								out.printf("%s","PRINTIRANKRAEN;TS1500-201 MZ;SEBNBGMVSHMEZDRA;,,,,,");

								out.println();

								write.close();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}

				}

				fieldDoplni.setText("");

			}

			print = false;

		
           //The code below add harness(increase) at statistic file
			if (dobavqneNaPaketaj) {
			
				try {
					FileWriter writeDopalni = new FileWriter(
							OK_StatFileByMonth, true);

					PrintWriter printToFile = new PrintWriter(writeDopalni, true);
					for (int ix = 1; ix <= dopalniBroi; ix++) {
						printToFile.printf("%s", getMatchCode
								+ ";TS1500-201 MZ;DOPALVANE_PAKETAJ;DOPAL");

						printToFile.println();
					}
					writeDopalni.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fieldDoplni.setText("");

				fieldDoplni.setEditable(true);
			}
			dobavqneNaPaketaj = false;

		}

	}

	public static void main(String args[]) {

		new Main();

	}

	public static StringBuilder formatPSC(StringBuilder stringPCS,
			int lengthForFormat) {
		StringBuilder formattedPCS = stringPCS;

		// Here format PCS to wish length
		while (formattedPCS.length() <= lengthForFormat) {
			formattedPCS.insert(0, "0");
		}

		return formattedPCS;
	}

	public static String getPathToOK_txtFile(String pathToStatisticFolder,
			String OKfileNameByMonth) {

		// Here takes the files names in STATISIC folder.
		File staistictFile = new File(pathToStatisticFolder);
		File[] StatNames = staistictFile.listFiles();
		String pathToOKfilesByMonth = null;

		// Here check if OK.txt statistic file have founded,and if OK.txt file
		// have founded (by month),return the full path to this file.
		for (int nameCounter = 0; nameCounter < StatNames.length; nameCounter++) {

			if (StatNames[nameCounter].isFile()) {

				String fileName = StatNames[nameCounter].getName();

				// Ako ima fail s takova ime se inicializacia put kam faila
				// Pravi se sravnenie mejdu actualnia mesec vzet ot
				// systemnoto vreme
				// i failovete v statistikata za da se ibere fail sprqmo
				// datata
				if (OKfileNameByMonth.equals(fileName)) {

					// Promenliva put kum statistikata-faila za meseca
					pathToOKfilesByMonth = pathToStatisticFolder + "\\"
							+ fileName;

				}

			}

		}
		return pathToOKfilesByMonth;

	}

	// This method return the length of some text file
	public static int getLengthOfTextFile(String pathToTextFile) {

		FileReader readTextFile = null;
		BufferedReader readTextFileBuferred;
		int lengthTextFile = 0;

		try {
			readTextFile = new FileReader(pathToTextFile);
		} catch (Exception e1) {
			taileNumberField.setFont(new Font("SansSerif", Font.BOLD, 10));
			taileNumberField.setBackground(Color.red);
			taileNumberField.setText("Не намирам ОК файлове в статистиката");
		}

		readTextFileBuferred = new BufferedReader(readTextFile);
		String row = null;

		try {
			while ((row = readTextFileBuferred.readLine()) != null) {
				if (row.length() > 0) {
					lengthTextFile++;

				}
			}
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		try {

			readTextFile.close();
			readTextFileBuferred.close();

		} catch (IOException e2) {

			taileNumberField.setFont(new Font("SansSerif", Font.BOLD, 10));
			taileNumberField.setBackground(Color.red);
			taileNumberField.setText("Немогадазатворяпотока–>файлOKstat.");
			e2.printStackTrace();
		}

		return lengthTextFile;
	}

	// This is a method which put the all rows from txt. file
	// into array of type String and return this array
	public static String[] turnTextFileToArray(String pathToTextFile) {

		int textFileLength = getLengthOfTextFile(pathToTextFile);

		// Here initialize the array for the OK.txt file
		String[] OK_StatFileByMonthArray = new String[textFileLength];
		File textFile = new File(pathToTextFile);
		String ActualString = null;

		try {
			readStat = new FileReader(textFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		readerStat = new BufferedReader(readStat);

		int broiach = 0;
		// prisvoiavane na statistikata kam masiv oy tip String

		try {
			while ((ActualString = readerStat.readLine()) != null) {
				if (ActualString.length() > 0) {

					OK_StatFileByMonthArray[broiach] = ActualString.trim();
					broiach++;

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return OK_StatFileByMonthArray;
	}
}