package com.qiyang.wb_dzzp.allcontrol;

public abstract class WaterSensorControl extends Thread{

    private GPIO_Operation mGPIO_Operation;
    public static int waterSensorStatus = 0;

    public void run()
    {
        while(true)
        {
            getWaterSensorStatus();
            try {
                sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public WaterSensorControl()
    {
        mGPIO_Operation = new GPIO_Operation(GPIO_Operation.CONTROL_WATER_SENSOR);
        if(!mGPIO_Operation.gpioSetDirection(GPIO_Operation.GPIO_DIRECTION_IN))
        {
            System.out.println("gpioSetDirection GPIO_DIRECTION_IN fail");
        }
    }

    public void getWaterSensorStatus()
    {
        String value = mGPIO_Operation.gpioGetValues();
        if(value != null)
        {
            int data  = Integer.valueOf(value);
            if(waterSensorStatus != data)
            {
                if(data == 0)
                {
                    //1 -》 0 无水
                    waterSensorEvent(0);
                }
                else if(data == 1)
                {
                    //0 -》 1 有水
                    waterSensorEvent(1);
                }
                System.out.println("read waterSensorStatus:"+data);
            }
            waterSensorStatus = data;
        }
    }

    public abstract  void waterSensorEvent(int open_or_close);
}
