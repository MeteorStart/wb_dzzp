package com.qiyang.wb_dzzp.allcontrol;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GPIO_Operation {

	public static final String GPIO_FILE_NAME = "/sys/class/gpio/gpio";
	public static final String GPIO_DIRECTION_OUT = "out"; 
	public static final String GPIO_DIRECTION_IN = "in"; 
	public static final String GPIO_VALUE_LOW = "0"; 
	public static final String GPIO_VALUE_HIGH = "1"; 

	public static final String CONTROL_FEN = "102";
	public static final String CONTROL_LED = "103";
	public static final String CONTROL_DOOR = "179";
	public static final String CONTROL_WATER_SENSOR = "178";
	
	private String gpioNumber;

	
	public GPIO_Operation(String gpioNumber)
	{
		this.gpioNumber = gpioNumber;
	}
	
	public boolean SetGPIOOutputData(String value)
	{
		if(!gpioSetDirection(GPIO_DIRECTION_OUT))
			return false;

		if(!gpioSetValues(value))
			return false;	

		return true;	
	}
	public boolean gpioSetDirection(String info)
	{
		//System.out.println("gpioSetDirection"+GPIO_FILE_NAME + this.gpioNumber + "/direction");
		char []buff = info.toCharArray(); 
		if(writeToFile(GPIO_FILE_NAME + this.gpioNumber + "/direction", buff))
		{
			return true;
		}
		return false;	
	}
	

	public boolean gpioSetValues(String gpio_value)
	{
		char []buff = gpio_value.toCharArray(); 
		//System.out.println("gpioSetValues"+GPIO_FILE_NAME + this.gpioNumber + "/value");
		if(writeToFile(GPIO_FILE_NAME + this.gpioNumber + "/value", buff))
		{
			return true;
		}
		return false;
	}

//	public String gpioGetValues()
//	{
//		String gpioData;
//		if(!gpioSetDirection(GPIO_DIRECTION_IN))
//			return null;
//		if(null != (gpioData = readToFile(GPIO_FILE_NAME + this.gpioNumber + "/value")))
//		{
//			return gpioData;
//		}
//		return null;
//	}

	public String gpioGetValues()
	{
		String gpioData;
		if(null != (gpioData = readToFile(GPIO_FILE_NAME + this.gpioNumber + "/value")))
		{
			return gpioData;
		}
		return null;
	}
	private boolean writeToFile(String fileName, char[] buf)
	{
		FileWriter fw = null;
		try {
			System.out.println("fileName:"+fileName);
			for (int i = 0; i < buf.length; i++) {
				System.out.println(buf[i]);
			}
			fw = new FileWriter(fileName);
			fw.write(buf); 
			fw.flush();   
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//System.out.println("exception:"+e);
			e.printStackTrace();
			return false;
		} finally {
	        try {
	        	if(fw != null)
	        		fw.close(); 
	        } catch (IOException e) {
	        e.printStackTrace();
	        }
		}
	}	
	
	private String readToFile(String fileName)
	{
		FileReader fw = null;
		char[] data = new char[128];
		try {
			fw = new FileReader(fileName);
//			//System.out.println("buf:"+buf[0]+buf[1]);
			//System.out.println("fileName:"+fileName);
			fw.read(data); 
			if(data[0] == 0x31)
			{
				return new String("1");
			}
			else
			{
				return new String("0");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//System.out.println("exception:"+e);
			e.printStackTrace();
			return null;
		} finally {
	        try {
	        	if(fw != null)
	        		fw.close(); 
	        } catch (IOException e) {
	        //e.printStackTrace();
	        }
		}
	}	

}
