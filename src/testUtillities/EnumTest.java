package testUtillities;

import enums.RoverConfiguration;

public class EnumTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("ENUM_TEST: starting");
		
		System.out.println("ENUM_TEST: toString RoverName.ROVER_00 " + RoverConfiguration.ROVER_00.toString());	
		
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
