package com.qiyang.wb_dzzp.allcontrol;

public class LedControl {
	private GPIO_Operation mGPIO_Operation;
	private static LedControl ledControl = null;
	public static int ledStatus = 0;
	private LedControl()
	{
		mGPIO_Operation = new GPIO_Operation(GPIO_Operation.CONTROL_LED);
	}
	
	public static LedControl getInstance()
	{
		if(ledControl == null)
		{
			ledControl = new LedControl();
		}
		return ledControl;
	}
	
	public boolean ledOpen()
	{
		ledStatus = 1;
		return mGPIO_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_HIGH);
	}
	public boolean ledClose()
	{
		ledStatus = 0;
		return  mGPIO_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_LOW);
	}
}
