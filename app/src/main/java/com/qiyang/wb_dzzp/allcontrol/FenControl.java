package com.qiyang.wb_dzzp.allcontrol;

public class FenControl {
	private GPIO_Operation mGPIO_Operation;
	private static FenControl fenControl = null;
	public static int fenStatus = 0;
	
	private FenControl()
	{
		mGPIO_Operation = new GPIO_Operation(GPIO_Operation.CONTROL_FEN);
	}
	
	public static FenControl getInstance()
	{
		if(fenControl == null)
		{
			fenControl = new FenControl();
		}
		return fenControl;
	}
	
	public boolean fenOpen()
	{
		fenStatus = 1;
		return mGPIO_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_HIGH);
	}
	public boolean fenClose()
	{
		fenStatus = 0;
		return  mGPIO_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_LOW);
	}
}
