import java.io.Serializable;

import javax.swing.JOptionPane;


public class Customer implements Serializable{
	private String name,SurName,Email; 
	private int Age;
	public Customer(){}
	public Customer(String name,String srname, String email,int age){
		this.name=name;
		this.SurName=srname;
		this.Email=email;
		this.Age=age;	 
	 }
	public void setName(String n){
		name=n;		
	}
	public void setSurName(String n){
		SurName=n;		
	}
	public void setEmail(String n){
		Email=n;		
	}
	public void setAge(int age){
		Age=age;
	}
	public String GetName(){
		return name;
		}
	public String GetSurName(){
		return SurName;
		}
	public String GetEmail(){
		return Email;
		}
	public int GetAge(){
		return Age;
	}

}
