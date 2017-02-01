


import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.text.SimpleDateFormat;
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


 
public class Main extends GUI{


      private static final long serialVersionUID = 1L;
      
//Promenlivi za inicializacia na failovete v STATISTIK i na to4no opredelen fail spored datata
        File file,actualStat    = null;
        
//Promenliva za preobrazuvane na USER vav format '000'       
        StringBuilder sNulite;
        
//Promenlivi za paketaja i TAILE number ot faila Paketaj.txt
        String  paketaj,taileNomerPack;

//Deklaracia na promenliva za sravnenie s failovete ot Statistikata
//za izteglqne na statistikata po data
        String dateName;  
	
        Timer timer = null;
 
 public Main()  {
	   
	  
    	
             timer = new Timer();
    	
               timer.schedule(new CrunchifyReminder(), 0,1 * scanTime);
              
 }
 //Class koito izpalnqva drug klas bezkraino
 class CrunchifyReminder  extends TimerTask {
	 
//Promenliva masiv za vzemane na STATISTIKATA
               String[] fileArr;
               
//Klu4 za printirane                   
               boolean key = false;
       
//Promenliva za vzemane na data v String
	           String DateToStr;
	           
 //Class izpalnqvan ot timera.Vur6i osnovnata rabota        
 public void run() {
	 
//Promenlivi za rabota  s COM port	 
	 Enumeration<?> portList = null;
	 OutputStream outputStream = null;
     CommPortIdentifier portId;
	 SerialPort serialPort = null;
	    
	 fieldDoplni.setBackground(Color.CYAN);
	    
//Izvli4ane  na path kum statistikata ot Setting faila    
                   file = new File(iniPath);
      // System.out.println(iniPath);            
 ///////Vzemane na sistemnata data za sravnenie sas Statistikata 
                   //i izvli4ane na faila-statistika po data
                   Date date= new Date();
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////   
                   
 //Formatirane na datata smo za da se vzeme meseca                  
	                SimpleDateFormat format = new SimpleDateFormat("MM");
	                DateToStr = format.format(date);
	   
//Obrazuvane na mostra-data za sravnenie v statistikata	   
	                dateName  = DateToStr+"OK.txt";
	               // System.out.println(dateName);  
 //Vzemane na imenata na failovete ot Statistikata	        
	                File[]  StatNames = file.listFiles();
	  
	  for (int ii = 0; ii < StatNames.length; ii++) {
		  
	      if (StatNames[ii].isFile()) {
	    	  
	       String fileName = StatNames[ii].getName();
	        
//Ako ima fail s takova ime se inicializacia put kam faila
//Pravi se sravnenie mejdu actualnia mesec vzet ot systemnoto vreme
//i failovete v statistikata za da se ibere fail sprqmo datata
	     if(dateName.equals(fileName)){
	    	 	 	    	  
//Promenliva put kum statistikata-faila za meseca	     
	     actualStat =new File(iniPath+"\\"+fileName);
	   //  System.out.println(actualStat);
	      }else{
	
	    }
	     
	    }

	    } 
	  
		 
	//	 JFrame errFrameStatFile = new JFrame("Error");
		// errFrameStatFile.setResizable(false);
		    
		 //errFrameStatFile.setSize(650,55);
	 
		 //errFrameStatFile.setVisible(true);
	       
		 //errFrameStatFile.setAlwaysOnTop(true);
	 
	//	 errFrameStatFile.setDefaultCloseOperation(EXIT_ON_CLOSE);
	 
	//	 errFrameStatFile.setLayout(null);
		 
		// JTextField errField1 = new  JTextField();
	     //errField1.setText("No matching Files in C://CSWIN//STATISTIK;example-02OK.txt");
	     //errFrameStatFile.add(errField1);
	      //errField1.setBounds(0,0,400,50);
	       //errField1.setBackground(Color.RED);
	        //errField1.setForeground(Color.yellow);	  
	  
	  
//Koda nadolu purvo:scanira cqlata statistika kato propuska praznite redove
//sled koeto updeitva edna promenliva broiach za da nameri broia na vsichki redove
//i da inicializira masiv ot tip String[].
//Vtoro:zapisva vseki red ot statistikata v masiva kato propuska praznite redove
//Treto:vzema neobhodimata informacia ot poslednite redove-za sravnenie za ednakvost i USER nomer
	 int Actual=0;
	  String ActualLength = null;
	 try {
		read = new FileReader(actualStat);
	} catch (Exception e1) {
		field.setFont(new Font("SansSerif",Font. BOLD,10));
		field.setBackground(Color.red);
		field.setText("Не намирам ОК файлове в статистиката");
	} 

	 reader = new BufferedReader(read);
	 
		 
		  
		   try {
			while ((ActualLength = reader.readLine()) != null) {
				    if(ActualLength.length() > 0) {
				    	Actual++;
				    	
				    }           
				}
		} catch (IOException e1) {
			 
			e1.printStackTrace();
		}	
		
		
		try {
			read.close();
		} catch (IOException e2) {
			field.setFont(new Font("SansSerif",Font. BOLD,10));
			field.setBackground(Color.red);
			field.setText("Немогадазатворяпотока–>файлOKstat.");
			e2.printStackTrace();
		}
		
		
		
	

	  
	  
	  
            //System.out.println(Actual);	  
	  
	  
	  
	  
//inicializacia na String[] za Statistikata                          
	   fileArr	=new String[Actual];
	   
	   
//zapisvane na statistikata v masiv	///////////////////////////////
		
		  String ActualString =null ;
		 try {
			readStat = new FileReader(actualStat);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		 readerStat = new BufferedReader(readStat);
		 
		 int	broiach=0; 
//prisvoiavane na statistikata kam masiv oy tip String			  
			   try {
				while ((ActualString = readerStat.readLine()) != null) {
					    if(ActualString.length() > 0) {
					    	
					    	
					    	
					    	 fileArr[broiach] =  ActualString.trim();      
					    broiach++;

					    }           					      
					}
				
				//vastanovqvane ot problem s daljinata na masiva
				//OT TUK PRI IZKLU4ENIE Array.OutOfBoundsExeption zapo4va na novo izpalnemieto na koda
			} catch (Exception e1) {
			
				  
				//Koda nadolu purvo:scanira cqlata statistika kato propuska praznite redove
				//sled koeto updeitva edna promenliva broiach za da nameri broia na vsichki redove
				//i da inicializira masiv ot tip String[].
				//Vtoro:zapisva vseki red ot statistikata v masiva kato propuska praznite redove
				//Treto:vzema neobhodimata informacia ot poslednite redove-za sravnenie za ednakvost i USER nomer
					  Actual=0;
					   ActualLength = null;
					 try {
						read = new FileReader(actualStat);
					} catch (FileNotFoundException e11) {

					} 

					 reader = new BufferedReader(read);
					 
						 
						  
						   try {
							while ((ActualLength = reader.readLine()) != null) {
								    if(ActualLength.length() > 0) {
								    	Actual++;
								    }           
								}
						} catch (IOException e11) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
						
						
						try {
							read.close();
						} catch (IOException e2) {
							field.setFont(new Font("SansSerif",Font. BOLD,10));
							field.setBackground(Color.red);
							field.setText("Немогадазатворяпотока–>файлOKstat.");
							e2.printStackTrace();
						}
						
                  //  System.out.println(Actual);	  
					  
					  
					  
					  
//inicializacia na String[] za Statistikata                          
					fileArr=new String[Actual];
					   					   
//zapisvane na statistikata v masiv	///////////////////////////////
					 ActualString =null ;
						 try {
							readStat = new FileReader(actualStat);
						} catch (FileNotFoundException e11) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 

						 readerStat = new BufferedReader(readStat);
						 
							broiach=0; 
							  
							   try {
								while ((ActualString = readerStat.readLine()) != null) {
									    if(ActualString.length() > 0) {
									    	
									    	
									    	
									    	 fileArr[broiach] =  ActualString.trim() ;      
									    broiach++;
									    
									    }           
									}
							} catch (IOException e11) {
						
	// TODO Auto-generated catch block
								e11.printStackTrace();
							}
							
							try {
								readStat.close();
							} catch (IOException e11) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}					
				
// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				readStat.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		
//update na promenlivi
			broiZaCorrect=1;
			counterBroi=1;
		
//Printirane na rezultata v masiva	      
		 //     for(int ih=0;ih<=fileArr.length-1;ih++){
	     //    }	     
		      
//Tuk se vzema end pozicia za mostra -taile nomer za sravnenie
//za ednakvost i se izvli4a USER nomer
		      
		  for(int ix=fileArr.length-1;ix>=1;ix--){    
			  
			 endIndex1 = fileArr[ix].indexOf(";");  
			 
			 endIndex2 = fileArr[ix-1].indexOf(";");
			 
			    sample1 = fileArr[ix].substring(0,endIndex1);
			 
			    sample2 = fileArr[ix-1].substring(0,endIndex2);
			    
			  
//Pravi se proverka za dnakvost na mastra ot poslednia red i takava ot predposlednia					
			if(sample1.equals(sample2)){
				
//Counter za broi komplekti				
			counterBroi++;
			
			broiZaCorrect++;
			
//Algoritm koito vzema USER nomera vinagi ot poslednia red na STATISTIK			
			 USER = fileArr[ix+counterBroi-2].substring(fileArr[ix+counterBroi-2].length()-5,fileArr[ix+counterBroi-2].length());	
				
			}else{
				if(ru4noDopalvane.equalsIgnoreCase("false")){
				fieldDoplni.setText("");	
				}
				USER = fileArr[ix+broiZaCorrect-1].substring(fileArr[ix+broiZaCorrect-1].length()-5,fileArr[ix+broiZaCorrect-1].length());	
				
				break;
			}
		  }
		//  System.out.println( sample1);	       
	
	//System.out.println(USER);
		      
	 fieldUser.setText("USER="+USER) ;  
       
     field.setText(" вариант =  "+sample1);
                  
     fieldPack.setText( paketaj);
                  
                  
                  
//Pravi se sravnenie s Paketaj faila i ako ima savpadneie se izvli4a taile nomer  i USER number           
          int start =   arrPaketaj.indexOf(sample1);      
          if(start !=-1){ 
        	  
        	   taileNomerPack =arrPaketaj.substring( start +sample1.length()+10,start+sample1.length()+25).trim();    
        	  paketaj  = arrPaketaj.substring( start +sample1.length()+6,start+sample1.length()+9).trim();
                 // System.out.println("Taile nomer = "+taileNomerPack);          
                 // System.out.println("Paketaj = "+paketaj);           
          }else{
//Prihvashtane na subitie na lipsvashta informacia za paketaj       	   
        	  try {
				Thread.sleep(1000);
				 field.setText("Bъведи пакeтаж"); 
				 Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
            }
 
//otpr4atvane na paketaja kato String         
          fieldPack.setText("Пакетаж="+ paketaj);      
          

       int value =    Integer.valueOf(paketaj);
       
 //Vzemane na procent ot deleneto na counter sprqmo paketaja     
//Inicializacia na promenliva s broia na samo komplektite sprqmo paketaja
//Ako ne se vzeme po tozi na4in broia se otpe4atvat vsi4ki ednakvi varianti
       
       
       
//Zapisvane v etiketa na informaciata za paketaj   
   String broiPackZaEti = String.valueOf(counterBroi);
     sNulite = new StringBuilder(); 
  sNulite.append(broiPackZaEti);
  
//Pravi PCS da bude v format xxx    
        while(sNulite.length()<=daljinaBroi){
	     sNulite.insert(0, "0");
      }
       
       fieldWK.setText(String.valueOf( sNulite));
       
//Uslovie za printirane na etiket       
     if( counterBroi==value){
    	print = true; 
    	 
      }
   
  // System.out.println(sample1);
   //Abstrakcia ot reda za zanulqvane
   if(sample1.equals("PRINTIRANKRAEN")){
	   fieldPack.setText("Пакетаж="); 
	   
	   field.setText("Вариант=");
	   
	   fieldUser.setText("USER=") ; 
	   
	   fieldWK.setText("");
	   
	   print=false;
	   
	   
	   
	   
	   
   }
   reset.setBackground(Color.yellow); 
   fieldDoplni.setEditable(true);
  
   if( USER.equals("DOPAL")){
	   fieldDoplni.setText("допълнен пакетаж");
	   fieldDoplni.setEditable(false);
	   print=false;
	   reset.setBackground(Color.red);  
	
   }
   
   
 //Aktualna data i 4as
	
   date = new Date();
            // SimpleDateFormat formatA = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
  	        SimpleDateFormat formatA = new SimpleDateFormat("dd.MM.yyyy");
  	 
  	      DateToStr = formatA.format(date);
  	       // System.out.println(DateToStr);	

 
/////////////////////////////////////////////////////////////////////////////////////
   
     String packing = String.valueOf(sNulite);                
     String ETI_Kraen = etiket.replace("@USER",USER);
		String ETI_Kraen1 = ETI_Kraen.replace("@Var1", taileNomerPack);	 
		String ETI_Kraen2 = ETI_Kraen1.replace("@COUNTER",packing);    
		String ETI_Kraen3 = ETI_Kraen2.replace("@DATE",DateToStr);   
    // System.out.println(scanTime);
 //PRINTIRANE NA KRAEN ETIKET                 
         if(print){
        	 
        	 System.out.println(ETI_Kraen3);
            	 //Zapisvane vav faila STATISTIC .Zapisva red za zanulqvane sled nepalen paketaj
                  //System.out.println(actualStat);  
        	 //Zapisvane vav faila STATISTIC .Zapisva red za zanulqvane sled nepalen paketaj
              //  System.out.println("okkkkkkkkkkkkkkkkk"+actualStat);  
   

 
            	 field.setText("ПРИНТИРАНЕ...");
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
 		                //if (portId.getName().equals("/dev/term/a")) {
 		                	
 		                    try {
 		                        serialPort = (SerialPort)
 		                        
 		                       
 		                            portId.open("SimpleWriteApp", 2000);
 		                    } catch (PortInUseException b) {
 		                       field.setFont(new Font("SansSerif",Font. BOLD,10));
 								field.setBackground(Color.red);
 								field.setText("СОМ портът не е наличен!");
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
 		                    	 field.setFont(new Font("SansSerif",Font. BOLD,10));
 								field.setBackground(Color.red);
 								field.setText("Не мога да отворя поток към ПРИНТЕРА!");	
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
 		                       field.setFont(new Font("SansSerif",Font. BOLD,10));
 								field.setBackground(Color.red);
 								field.setText("Невалидни параметри на СОМ порта!");
 								
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
 		                    } catch (IOException b) {}
 		                    //System.out.println(serialPort);    
 		                 
 		                   serialPort.close();   
 //Zapisvane vav faila STATISTIC .Zapisva red za zanulqvane sled nepalen paketaj
 		                 //  System.out.println("okkkkkkkkkkkkkkkkk"+actualStat);  
 		                  try {
 		                	 FileWriter write = new FileWriter( actualStat,true );

 		                	 PrintWriter  out = new  PrintWriter(write,true);
 		                	
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
                
        	    //PRINTIRANE NA REDOVE ZA DOPALVANE V STAISTIKATA EDNOKRATNO
           	 
           	 if(dobavqneNaPaketaj){
           		System.out.println("okkkkkkk"); 
           		try {
                	 FileWriter writeDopalni = new FileWriter( actualStat,true );

                	 PrintWriter  outA = new  PrintWriter(writeDopalni,true);
                	for(int ix =1;ix <=dopalniBroi;ix++){
                	 outA.printf("%s",getMatchCode+";TS1500-201 MZ;DOPALVANE_PAKETAJ;DOPAL");

                	 outA.println();
                	}
                	 writeDopalni.close();

                	 } catch (IOException e) {
                	 // TODO Auto-generated catch block
                	 e.printStackTrace();
                	 }		
                		fieldDoplni.setText("");
                		
                		fieldDoplni.setEditable(true);
           	 }
           	 dobavqneNaPaketaj=false; 
           	 
           
  }

 
 
  }
  
  public static void main(String args[]) {
        	  
           new Main();
          
  }
  
  }