
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;


public class CustomerForm    {
	JFrame Userform;
	private JComboBox AccommodationCombox,DestinationComBox, NumOfNight,yearsCombox,monthsCombox,daysCombox;
	private JLabel lbDestination,lbAccommodation,lblNoOfNight,lbdaytravel,lbNumberTravelar, lblPrice ,lblVat,lblTotal,label_1, lblLocation, lblDateOfTravel,lblDateOfBooking, lblNumberOfPeople, lblHolidayDays , lblAccommodationType;
	private JPanel panel,TicketInfoPanel,UserPanelbooking;
	private   FileInputStream HolidaysFile;
	private ObjectInputStream inputFileHolidays;
	ArrayList<Holiday> ArrayHolidays;
	private Calendar cal;
	private JButton BtnIncrease,Btnwithdraw,  btnLogout;
	int number_of_traveler=0,DaysInsideHighSeason=0,NumberOfChild=0;
	private final String StartHighSeason="25/6",EndHighSeason="1/9";
	private JCheckBox chckbxTravelInsurance,chckbxAirportTransfer,chckbxExtraLaggeg;
	private JTabbedPane UserTabbedPanel;
	private JTextField usernametxt,AddDescTxtBox,AddAccomTxtBox,FnameAddManager,surnameAddManager,EmailAddManager,usernameAddMananger;
	private JPasswordField managerPswTxt,PswManger,confirmPswManger;
	
	public CustomerForm(){
		initialize();	
		
	}
	void initialize(){		
		Userform = new JFrame();		
		Userform.setBackground(new Color(255, 69, 0));
		Userform.setTitle("Booking Holiday");
		Userform.setBounds(100, 100, 879, 550);
		Userform.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Userform.getContentPane().setLayout(null);
		//Userform.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//Userform.setUndecorated(true);
		Userform.setResizable(false);
		setPanel();
		setComBox();
		SetLabels();
		set_arraylist_holidays();
		AddtoDestinationComBox();
		SetYears();
		setButton();
		setTicketPanel();
		UserTabbedPanel = new JTabbedPane(JTabbedPane.LEFT);
		UserTabbedPanel.setBounds(12, 12, 850, 470);
		Userform.getContentPane().add(UserTabbedPanel);
		UserTabbedPanel.addTab(null,new ImageIcon("bookingbutton.png"), UserPanelbooking,"Booking a Fight");
	
	
		Userform.setVisible(true);
		}
	
	private void setAddHolidayPanel(){
		Panel AddHolidaysPanel = new Panel();
		UserTabbedPanel.addTab(null, new ImageIcon("addholidayicon.png"), AddHolidaysPanel, "Add Holiday");
		AddHolidaysPanel.setBackground(new Color(216, 191, 216));
		AddHolidaysPanel.setLayout(null);
		
		AddDescTxtBox = new JTextField();
		AddDescTxtBox.setBounds(319, 118, 200, 41);
		AddHolidaysPanel.add(AddDescTxtBox);
		AddDescTxtBox.setColumns(10);
		
		AddAccomTxtBox = new JTextField();
		AddAccomTxtBox.setColumns(10);
		AddAccomTxtBox.setBounds(319, 177, 200, 41);
		AddHolidaysPanel.add(AddAccomTxtBox);
		
		JLabel lblAddHolidaydest = new JLabel("Destination");
		lblAddHolidaydest.setBounds(124, 119, 114, 39);
		AddHolidaysPanel.add(lblAddHolidaydest);
		
		JLabel lblAddHolidayNum = new JLabel("No. of Night");
		lblAddHolidayNum.setBounds(124, 248, 94, 29);
		AddHolidaysPanel.add(lblAddHolidayNum);
		
		JLabel lblAddHolidayAccom = new JLabel("Accommodation Type: ");
		lblAddHolidayAccom.setOpaque(true);
		lblAddHolidayAccom.setBackground(new Color(240, 230, 140));
		lblAddHolidayAccom.setBounds(124, 177, 161, 41);
		AddHolidaysPanel.add(lblAddHolidayAccom);
		
		JLabel lblPriceForTwo = new JLabel("Price for two adult: ");
		lblPriceForTwo.setOpaque(true);
		lblPriceForTwo.setBackground(new Color(240, 230, 140));
		lblPriceForTwo.setBounds(123, 300, 161, 29);
		AddHolidaysPanel.add(lblPriceForTwo);
		
		final JSpinner AddnumNightspinner = new JSpinner();
		AddnumNightspinner.setBounds(319, 232, 117, 41);
		AddnumNightspinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		AddHolidaysPanel.add(AddnumNightspinner);
		
		final JSpinner AddPriceSpinner = new JSpinner();
		AddPriceSpinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(100)));
		AddPriceSpinner.setBounds(319, 288, 117, 41);
		AddHolidaysPanel.add(AddPriceSpinner);
		
		JButton btnAddHoliday = new JButton("Add Holiday");
		btnAddHoliday.setBounds(87, 358, 521, 61);
		AddHolidaysPanel.add(btnAddHoliday);
		btnAddHoliday.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(AddDescTxtBox.getText().equals("")||AddAccomTxtBox.getText().equals("")||Integer.parseInt(AddnumNightspinner.getValue().toString())==0||Double.parseDouble(AddPriceSpinner.getValue().toString())==0){
					JOptionPane.showMessageDialog(null, "Enter valid values\nCan't add holiday");
					return;
				}
				ArrayHolidays.add(new Holiday(AddDescTxtBox.getText(),AddAccomTxtBox.getText(),Integer.parseInt(AddnumNightspinner.getValue().toString()),Double.parseDouble(AddPriceSpinner.getValue().toString())));
				try {	
					FileOutputStream holidaysStream=new FileOutputStream("Holidays.dat");
					ObjectOutputStream holidaysOutput=new ObjectOutputStream(holidaysStream);
					for(int i=0;i<ArrayHolidays.size();i++)
						holidaysOutput.writeObject(ArrayHolidays.get(i));
					holidaysOutput.close();
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "ADDED");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Not Valid save "+e1);
				}
			}
		});
		
		JLabel lblAddingHolidaysPage = new JLabel("       Adding Holidays Page");
		lblAddingHolidaysPage.setBackground(new Color(255, 228, 196));
		lblAddingHolidaysPage.setForeground(new Color(0, 0, 0));
		lblAddingHolidaysPage.setOpaque(true);
		lblAddingHolidaysPage.setFont(new Font("Traditional Arabic", Font.PLAIN, 40));
		lblAddingHolidaysPage.setBounds(87, 13, 521, 58);
		AddHolidaysPanel.add(lblAddingHolidaysPage);
		
	}
	
	private void setPanel(){
		panel = new JPanel();
		panel.setBackground(new Color(224, 255, 255));
		panel.setBounds(10, 10, 400, 411);
		Userform.getContentPane().add(panel);
		Userform.getContentPane().setBackground(new Color(255, 222, 173));
		panel.setLayout(null);	
		
	}
	
	private void setComBox(){
		DestinationComBox= new JComboBox ();
		DestinationComBox.setBounds(170, 28, 217, 39);
		panel.add(DestinationComBox);		
		AccommodationCombox = new JComboBox();
		AccommodationCombox.setBounds(170, 96, 217, 39);
		 //because if we don't have holidays we can't choose this column 
		panel.add(AccommodationCombox);
		NumOfNight = new JComboBox();
		NumOfNight.setBounds(170, 164, 217, 39);
		//because if we don't have holidays we can't choose this column 
		panel.add(NumOfNight);
		yearsCombox = new JComboBox();
		yearsCombox.setBounds(170, 210, 65, 39);
		panel.add(yearsCombox);		 
		monthsCombox = new JComboBox();
		monthsCombox.setBounds(236, 210, 100, 39);
		panel.add(monthsCombox);
		daysCombox = new JComboBox();
		daysCombox.setBounds(337, 210, 50, 39);
		panel.add(daysCombox); 	
		DestinationComBox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
					Set_AccommodationCombox();
			}
		});
		AccommodationCombox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				Set_NumNightCombox();
			}
		});
		yearsCombox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				SetMonthes();
			}
		});
		monthsCombox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				SetDays();
			}
		});
	 }
 
	private void setButton(){
		BtnIncrease = new JButton();
		BtnIncrease.setBounds(313, 259, 75, 75);
		panel.add(BtnIncrease);
		BtnIncrease.setHorizontalAlignment(SwingConstants.CENTER);
		BtnIncrease.setIcon(new ImageIcon("addlogo.png"));
		BtnIncrease.setToolTipText("Add Traveler");
		Btnwithdraw = new JButton( );
		Btnwithdraw.setBounds(137, 259, 75, 75);
		Btnwithdraw.setToolTipText("Remove Traveler");
		panel.add(Btnwithdraw);		
		Btnwithdraw.setHorizontalAlignment(SwingConstants.CENTER);
		Btnwithdraw.setIcon(new ImageIcon("remove.png"));			
		lbNumberTravelar = new JLabel("0");
		lbNumberTravelar.setFont(new Font("Wide Latin", Font.BOLD, 26));
		lbNumberTravelar.setHorizontalAlignment(SwingConstants.CENTER);
		lbNumberTravelar.setBounds(223, 259, 75, 75);
		panel.add(lbNumberTravelar);
		BtnIncrease.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				number_of_traveler++;
				lbNumberTravelar.setText(number_of_traveler+"");
				SetTicketDetails();
			}
		});;
		Btnwithdraw.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setTicketPanel();
                if(number_of_traveler==0)
                	return;
				number_of_traveler--;
				lbNumberTravelar.setText(number_of_traveler+"");
				SetTicketDetails();
	 
			}
		});
	////////////Set a button for add/remove child 
		JButton AddChildbtn = new JButton("New button");		 
		AddChildbtn.setBounds(313, 337, 75, 75);
		AddChildbtn.setIcon(new ImageIcon("AddChild_oldery.png"));
		panel.add(AddChildbtn);
	
		JButton RemoveChildBtn = new JButton("New button");
		RemoveChildBtn.setBounds(137, 337,  75, 75);
		RemoveChildBtn.setIcon(new ImageIcon("RemoveChild_oldery.png"));
		panel.add(RemoveChildBtn);
		
		JLabel NumberOfChildlbl = new JLabel("0");
		NumberOfChildlbl.setHorizontalAlignment(SwingConstants.CENTER);
		NumberOfChildlbl.setFont(new Font("Wide Latin", Font.BOLD, 26));
		NumberOfChildlbl.setBackground(SystemColor.activeCaption);
		NumberOfChildlbl.setBounds(222, 337, 75, 75);
		panel.add(NumberOfChildlbl);
		AddChildbtn.addActionListener(new ActionListener() {
			////Add Child Button
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				NumberOfChild++;
				NumberOfChildlbl.setText(NumberOfChild+"");
				SetTicketDetails();
			}
		});
		RemoveChildBtn.addActionListener(new ActionListener() {
			//remove child button
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setTicketPanel();
                if(NumberOfChild==0)
                	return;
                NumberOfChild--;
				NumberOfChildlbl.setText(NumberOfChild+"");
				SetTicketDetails();
			}
		});
	}
	private void SetLabels(){
		lbDestination = new JLabel("Destination : ");
		lbDestination.setBounds(12, 28, 114, 39);
		panel.add(lbDestination);
		lbAccommodation = new JLabel("Accommodation Type: ");
		lbAccommodation.setBounds(12, 106, 150, 16);
		panel.add(lbAccommodation);
		lblNoOfNight = new JLabel("No. of Night: ");
		lblNoOfNight.setBounds(12, 176, 94, 16);
		panel.add(lblNoOfNight);
		lbdaytravel= new JLabel("Travel Day: ");
		lbdaytravel.setBounds(12, 220, 94, 16);
		panel.add(lbdaytravel);
	}
	private void set_arraylist_holidays(){
		ArrayHolidays=new ArrayList<Holiday>();
		try{
			HolidaysFile = new FileInputStream( "Holidays.dat" );
			  inputFileHolidays = new ObjectInputStream( HolidaysFile);
	 		while ( HolidaysFile.available()>0 ){
	 			Holiday l = (Holiday) inputFileHolidays.readObject();
				ArrayHolidays.add(l);
	// System.out.println(" : "+l.GetDestination()+"    "+l.GetAccommodation()+"    "+l.GeNights()+"      "+l.GetPrice());
			} 
 		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null,"Please Check Holidays File");
		}
	}
	  		private  void AddtoDestinationComBox(){
			for (int i=0;i<ArrayHolidays.size();i++){
				if(((DefaultComboBoxModel)DestinationComBox.getModel()).getIndexOf(ArrayHolidays.get(i).GetDestination()) == -1 ) //check if combobx contain a value that want to add
				 DestinationComBox.addItem(ArrayHolidays.get(i).GetDestination());
				 }
			}
	  		private void Set_AccommodationCombox(){
	  			AccommodationCombox.setEnabled(true);
	  			AccommodationCombox.removeAllItems();
  				for (int i=0; i< ArrayHolidays.size();i++){
  					if(((DefaultComboBoxModel)AccommodationCombox.getModel()).getIndexOf(ArrayHolidays.get(i).GetAccommodation()) == -1 &&  ArrayHolidays.get(i).GetDestination().equals(DestinationComBox.getSelectedItem())) 
  						AccommodationCombox.addItem(ArrayHolidays.get(i).GetAccommodation());
  					 		}
	  		}
	  		private void Set_NumNightCombox(){
	  			NumOfNight.setEnabled(true);
	  			NumOfNight.removeAllItems();
  				for (int i=0; i< ArrayHolidays.size();i++){
  					if(((DefaultComboBoxModel)NumOfNight.getModel()).getIndexOf(ArrayHolidays.get(i).GetNights()) == -1 &&(  ArrayHolidays.get(i).GetDestination().equals(DestinationComBox.getSelectedItem())&& ArrayHolidays.get(i).GetAccommodation().equals(AccommodationCombox.getSelectedItem())))
  						NumOfNight.addItem(ArrayHolidays.get(i).GetNights());
  					 		}
	  		}
	  		private void SetYears(){
	  		cal=Calendar.getInstance();
	  		int year=cal.get(Calendar.YEAR);
	  		for(int i=0;i<10;i++)
	  			yearsCombox.addItem(year++);
	  		}
	  		private void SetMonthes(){
	  			monthsCombox.removeAllItems();
	  			int month=cal.get(Calendar.MONTH); 
	  			//JOptionPane.showMessageDialog(null,month+"");
	  			 DateFormatSymbols dfs = new DateFormatSymbols();
	  	        String[] months = dfs.getMonths();
	  	      cal=Calendar.getInstance();
		  	  if(Integer.parseInt(yearsCombox.getSelectedItem().toString())==cal.get(Calendar.YEAR))
	  	       { for(int i=month;i<12;i++)
	 	  			monthsCombox.addItem(months[i]);
	  	       }
	  	       else { for(int i=0;i<12;i++)
	 	  			monthsCombox.addItem(months[i]);
	  	       }
	  	       
	  		}
	  		private void SetDays(){
	  			daysCombox.removeAllItems();
	  			int year=cal.get(Calendar.YEAR);
	  			int iDay = 1;
	  			// Create a calendar object and set year and month
	  			Calendar mycal = new GregorianCalendar(Integer.parseInt(yearsCombox.getSelectedItem().toString()),(12-monthsCombox.getItemCount())+monthsCombox.getSelectedIndex(), iDay);
	  			// Get the number of days in that month
	  			int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		      if(Integer.parseInt(yearsCombox.getSelectedItem().toString())==cal.get(Calendar.YEAR) && 0==monthsCombox.getSelectedIndex())
	  	       {/**/
	  	    	  for(int i=cal.get(Calendar.DAY_OF_MONTH);i<=daysInMonth;i++)
	 	  			daysCombox.addItem(i);
	  	       }
	  	       else {int mo=0;
	  	       cal=Calendar.getInstance();
	  	    	   if(Integer.parseInt(yearsCombox.getSelectedItem().toString())==cal.get(Calendar.YEAR))
		  	    	    mo=cal.get(Calendar.MONTH); 	
	   	  	    	    cal.set(Calendar.YEAR, year);
	  	       			cal.set(Calendar.MONTH, monthsCombox.getSelectedIndex()+mo);
	  	       			int numDays = cal.getActualMaximum(Calendar.DATE);
	  	       			for(int i=1;i<=daysInMonth;i++)
	  	       			daysCombox.addItem(i);
	  	       }  cal=Calendar.getInstance();
		  	    
	  		}
 
	  		private void setTicketPanel(){			 
	  			//JOptionPane.showMessageDialog(null,"FGFGF");
	  			if(TicketInfoPanel==null)
	  			TicketInfoPanel= new JPanel();
	  			TicketInfoPanel.setBackground(new Color(244, 164, 96));
	  			TicketInfoPanel.setBounds(408, 10, 297, 411);
	  			Userform.getContentPane().add(TicketInfoPanel);
	  			TicketInfoPanel.setLayout(null);
	  			if(lblPrice==null)
	  				lblPrice = new JLabel("Price: ");
	  			lblPrice.setBackground(new Color(240, 230, 140));
	  			lblPrice.setBounds(22, 286, 239, 27);
	  			TicketInfoPanel.add(lblPrice);	
	  			lblPrice.setOpaque(true);
	  			if(lblVat==null)

	  			lblVat  = new JLabel("VAT: ");
	  			lblVat.setBackground(new Color(240, 230, 140));
	  			lblVat.setBounds(22, 326, 239, 22);
	  			lblVat.setOpaque(true);
	  			TicketInfoPanel.add(lblVat);
	  			if(lblTotal==null)

	  			lblTotal= new JLabel("Total:");
	  			lblTotal.setBackground(new Color(240, 230, 140));
	  			lblTotal.setBounds(22, 371, 150, 27);
	  			lblTotal.setOpaque(true);
	  			TicketInfoPanel.add(lblTotal);
	  			
	  			 label_1= new JLabel("******************************");
	  			label_1.setForeground(new Color(255, 0, 0));
	  			label_1.setBounds(22, 348, 268, 22);
	  			TicketInfoPanel.add(label_1);
	  			if(lblLocation==null)

	  			lblLocation = new JLabel("Location : ");
	  			lblLocation.setBackground(new Color(240, 230, 140));
	  		 	lblLocation.setBounds(22, 26, 246, 27);
	  		 	lblLocation.setOpaque(true);
	  		 	TicketInfoPanel.add(lblLocation);
	  			if(lblDateOfTravel==null)

	  			lblDateOfTravel = new JLabel("Date of travel : ");
	  			lblDateOfTravel.setBackground(new Color(240, 230, 140));
	  			lblDateOfTravel.setBounds(22, 66, 246, 27);
	  			 lblDateOfTravel.setOpaque(true); 
	  			TicketInfoPanel.add(lblDateOfTravel);
	  			if(lblDateOfBooking==null)

	  			lblDateOfBooking = new JLabel("Date of Booking: ");
	  			lblDateOfBooking.setBackground(new Color(240, 230, 140));
	  			lblDateOfBooking.setBounds(22, 106, 246, 27);
	  			lblDateOfBooking.setOpaque(true);
	  			TicketInfoPanel.add(lblDateOfBooking);
	  			if(lblNumberOfPeople==null)

	  			lblNumberOfPeople = new JLabel("Number of People: ");
	  			lblNumberOfPeople.setBackground(new Color(240, 230, 140));
	  			lblNumberOfPeople.setBounds(22, 147, 246, 27);
	  			lblNumberOfPeople.setOpaque(true);
	  			TicketInfoPanel.add(lblNumberOfPeople);
	  			if(lblHolidayDays==null)

	  			lblHolidayDays = new JLabel("Holiday Days: ");
	  			lblHolidayDays.setBackground(new Color(240, 230, 140));
	  			lblHolidayDays.setBounds(22, 181, 246, 27);
	  			lblHolidayDays.setOpaque(true);
	  			TicketInfoPanel.add(lblHolidayDays);
	  			if(lblAccommodationType==null)

	  			lblAccommodationType = new JLabel("Accommodation Type: ");
	  			lblAccommodationType.setBackground(new Color(240, 230, 140));
	  			lblAccommodationType.setBounds(22, 220, 246, 27);
	  			lblAccommodationType.setOpaque(true);
	  			TicketInfoPanel.add(lblAccommodationType);	  			
	  			
	  			
	  			JButton btnBook = new JButton("Book");	  			
	  			btnBook.setBounds(206, 361, 84, 39);
	  			TicketInfoPanel.add(btnBook);
	  			btnBook.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (NumberOfChild+number_of_traveler<=0)return;
						FileWriter fw;
						try {
						fw = new FileWriter( "Invoice Ticket .txt" );
						PrintWriter outputFile = new PrintWriter( fw);
						outputFile.println(lblLocation.getText() );
						outputFile.println(lblAccommodationType.getText() );
						outputFile.println(lblDateOfBooking.getText() );
						outputFile.println(lblDateOfTravel.getText() );
						outputFile.println(lblHolidayDays.getText() );
			 			outputFile.println(lblNoOfNight.getText() );
						outputFile.println(lblNumberOfPeople.getText());
						outputFile.println(lblPrice.getText() );
						outputFile.println(lblVat.getText() );
						outputFile.println(lblTotal.getText() );
						outputFile.close();
						JOptionPane.showMessageDialog(null,"Booked Successfully");
							} 
						catch (IOException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e1);
						}
						
					}
				});
	  			}
private void CalculateDate(){	
	String HighSeason[][]=new String[2][];
		   HighSeason[0]=StartHighSeason.split("/");
		   HighSeason[1]=EndHighSeason.split("/");
	int monthselected=0;
      	cal=Calendar.getInstance();
   	if(Integer.parseInt(yearsCombox.getSelectedItem().toString())==cal.get(Calendar.YEAR))
   		monthselected=cal.get(Calendar.MONTH); 	
   		monthselected+=monthsCombox.getSelectedIndex()+1;
 	int dayselected=Integer.parseInt(daysCombox.getSelectedItem().toString());
		LocalDate dateselected=LocalDate.of(Integer.parseInt(yearsCombox.getSelectedItem().toString()),monthselected, Integer.parseInt(daysCombox.getSelectedItem().toString()));
		LocalDate dateStartHighSeason=LocalDate.of(Integer.parseInt(yearsCombox.getSelectedItem().toString()),Integer.parseInt(HighSeason[0][1]), Integer.parseInt(HighSeason[0][0]));
		LocalDate dateEndHighSeason=LocalDate.of(Integer.parseInt(yearsCombox.getSelectedItem().toString()),Integer.parseInt(HighSeason[1][1]), Integer.parseInt(HighSeason[1][0]));
	if(dateselected.isAfter(dateEndHighSeason))
		DaysInsideHighSeason=0;
	else{
		dateStartHighSeason.minusDays(Integer.parseInt(daysCombox.getSelectedItem().toString()));
		if(dateselected.isBefore(dateStartHighSeason))
			DaysInsideHighSeason=0;
		dateStartHighSeason.plusDays(Integer.parseInt(daysCombox.getSelectedItem().toString()));		
		}
	 if(dateselected.isBefore(dateStartHighSeason)&&dateStartHighSeason.isBefore(addNumNightToDate(dateselected)))/// Here to know if date selected is before hieh season & after the start of hieh season
	 	{		
		DaysInsideHighSeason=(int)ChronoUnit.DAYS.between((addNumNightToDate(dateselected)), dateStartHighSeason);
	 	}
	else if(dateselected.isBefore(dateEndHighSeason)&&dateEndHighSeason.isBefore(addNumNightToDate(dateselected)))
			{
		DaysInsideHighSeason=(int)ChronoUnit.DAYS.between(dateEndHighSeason, dateselected);
			}
	else if(dateselected.isAfter(dateStartHighSeason)&&dateselected.isBefore(dateEndHighSeason))
			{
		DaysInsideHighSeason=Integer.parseInt(NumOfNight.getSelectedItem().toString());
			}
}
private LocalDate addNumNightToDate(LocalDate selecDate){
	LocalDate dateselectedAfterAdd=selecDate.plusDays(Integer.parseInt(NumOfNight.getSelectedItem().toString()));
	return dateselectedAfterAdd;	
}
 
	private double CalculateCost(){	
		 CalculateDate();
		 double price=GetTheNormalPrice()/2;
		 int numNigth=Integer.parseInt(NumOfNight.getSelectedItem().toString());
		 double nightPrice=price/ numNigth;
		 price=((nightPrice*DaysInsideHighSeason*2)+((numNigth-DaysInsideHighSeason)*nightPrice))*number_of_traveler;
		 price+=((((nightPrice*DaysInsideHighSeason*2)+((numNigth-DaysInsideHighSeason)*nightPrice)))*NumberOfChild/2);
			 if(chckbxAirportTransfer.isSelected())
				 price+=(10*(number_of_traveler+NumberOfChild));
			 if(chckbxExtraLaggeg.isSelected())
				 price+=(20*(number_of_traveler+NumberOfChild));
			 if(chckbxTravelInsurance.isSelected())
				 price+=(25*(number_of_traveler+NumberOfChild));				 
		return price;// because the price for tow adult
	}
	private double costAfterVAT(){
		double price=CalculateCost();
		  double Precantage=20.0/100;
	 	price*=Precantage;
		return price;
	}
	private double GetTheNormalPrice(){
	double price=0;
	for (int i=0; i< ArrayHolidays.size();i++){
			if(ArrayHolidays.get(i).GetDestination().equals(DestinationComBox.getSelectedItem())&& ArrayHolidays.get(i).GetAccommodation().equals(AccommodationCombox.getSelectedItem())&&ArrayHolidays.get(i).GetNights()==Integer.parseInt(NumOfNight.getSelectedItem().toString()))
			price=ArrayHolidays.get(i).GetPrice();
			}	
	return price;
	
	}
	private void SetTicketDetails(){
		setTicketPanel();
		if(CalculateCost()==0)return;
		DecimalFormat df = new DecimalFormat("#.00"); 
		lblLocation.setText("Location: "+DestinationComBox.getSelectedItem());
		lblAccommodationType.setText("Accomodation Type: "+AccommodationCombox.getSelectedItem());
		lblDateOfBooking.setText("Date Of Booking"+LocalDate.now()+"   "+LocalTime.now().getHour()+":"+LocalTime.now().getMinute()+":"+LocalTime.now().getSecond());
		lblDateOfTravel.setText("Date of Travel:  "+yearsCombox.getSelectedItem()+'/'+monthsCombox.getSelectedItem()+'/'+daysCombox.getSelectedItem());
		lblNumberOfPeople.setText("Number of People: "+(number_of_traveler+"  Adult      "+NumberOfChild+"  Child/Old"));
		lblHolidayDays.setText("Hoilday Long : "+NumOfNight.getSelectedItem());
		lblPrice.setText("Price:   "+df.format(CalculateCost()));
		lblVat.setText("Vat#:  "+df.format(costAfterVAT()));
		lblTotal.setText("Total:  "+df.format((costAfterVAT()+CalculateCost())));			
	}
	private void applySearch(String s)
	{		
	String selectedHoliday[]=s.split(" 	");	
	DestinationComBox.setSelectedItem(selectedHoliday[0]);	
	AccommodationCombox.setSelectedItem(selectedHoliday[1]);
	NumOfNight.setSelectedItem(Integer.parseInt(selectedHoliday[2]));
	UserTabbedPanel.setSelectedIndex(0);
	}
} 