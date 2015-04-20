

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class Holiday implements Serializable{
	private String Destination,Accommodation;
	private int Nights;
	private double Price;
	
	public Holiday () {
		// TODO Auto-generated constructor stub

	}
	public Holiday (String des,String Acc, int numNight,double pric) {
		// TODO Auto-generated constructor stub
		Destination=des;
		Accommodation=Acc;
		Nights=numNight;
		Price=pric;
	}
	public void setDestination(String des){
		Destination=des;		
	}
	public void setAccommodation(String acc){
		Accommodation=acc;		
	}
	public void setPrice(double p){
		Price=p;		
	}
	public void setNumNight(int num){
		Nights=num;
	}
	public String GetDestination(){
		return Destination;
		}
	public String GetAccommodation(){
		return Accommodation;
		}
	public double GetPrice(){
		return Price;
		}
	public int GetNights(){
		return Nights;
	}}