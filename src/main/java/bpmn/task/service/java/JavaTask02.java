package bpmn.task.service.java;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaTask02 implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String echoName(String name){
		
		System.out.println("Recieved name: " + name);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
}
