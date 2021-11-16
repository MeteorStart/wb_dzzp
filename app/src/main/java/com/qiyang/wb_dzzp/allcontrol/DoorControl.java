package com.qiyang.wb_dzzp.allcontrol;

public abstract class DoorControl extends Thread{
	
	private GPIO_Operation mGPIO_Operation;
	public static int doorStatus = 0;
	
	public void run()
	{
		while(true)
		{
			getDoorStatus();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public DoorControl()
	{
		mGPIO_Operation = new GPIO_Operation(GPIO_Operation.CONTROL_DOOR);
		if(!mGPIO_Operation.gpioSetDirection(GPIO_Operation.GPIO_DIRECTION_IN))
		{
			System.out.println("gpioSetDirection GPIO_DIRECTION_IN fail");
		}
	}
	
	public void getDoorStatus()
	{
		String value = mGPIO_Operation.gpioGetValues();
		if(value != null)
		{
			int data  = Integer.valueOf(value);
		    if(doorStatus != data)
    		{
		    	if(data == 0)
		    	{
		    		//1 -》 0 关门
		    		doorEvent(0);
		    	}
		    	else if(data == 1)
		    	{
					//0 -》 1 开门
					doorEvent(1);
		    	}
				System.out.println("read doorStatus:"+data);
    		}
		    doorStatus = data;
		}
	}
	
	public abstract  void doorEvent(int open_or_close);
}
