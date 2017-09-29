package enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// thank you to this post: http://stackoverflow.com/questions/3054247/how-to-define-properties-for-enum-items

// Sensors: RADIATION_SENSOR->Radioactivescience; CHEMICAL_SENSOR->Organic Science;
//	SPECTRAL_SENSOR->Crystal Science; RADAR_SENSOR  ->mineral Science

// Harvesting tools: EXCAVATOR->Soil and Sand;  DRILL->Rocks and Gravel

public enum RoverConfiguration {
	// (Drive type, accessory slot 1, accessory slot 2)
	NONE,

	ROVER_01 ("WHEELS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_02 ("WALKER", "SPECTRAL_SENSOR", "DRILL"),
	ROVER_03 ("TREADS", "EXCAVATOR", "RADAR_SENSOR"),

	//not currently being used
	ROVER_04 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_05 ("TREADS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_06 ("WHEELS", "RANGE_BOOTER", "RADIATION_SENSOR"),
	ROVER_07 ("TREADS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_08 ("TREADS", "EXCAVATOR", "SPECTRAL_SENSOR"),
	ROVER_09 ("WALKER", "CHEMICAL_SENSOR", "DRILL"),
	ROVER_10 ("WHEELS", "RANGE_BOOTER", "RADIATION_SENSOR"),
	ROVER_11 ("WALKER", "DRILL", "EXCAVATOR"),
	ROVER_12 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_13 ("WHEELS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_14 ("WHEELS", "RANGE_BOOTER", "CHEMICAL_SENSOR"),
	ROVER_15 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_16 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_17 ("WHEELS", "RANGE_BOOTER", "RADAR_SENSOR"),
	ROVER_18 ("WHEELS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_19 ("NONE", "NONE", "NONE"),
	ROVER_20 ("NONE", "NONE", "NONE"),
	
	ROVER_31 ("WALKER", "DRILL", "EXCAVATOR"),
	ROVER_32 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_33 ("WHEELS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_34 ("WHEELS", "RANGE_BOOTER", "CHEMICAL_SENSOR"),
	ROVER_35 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_36 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_37 ("WHEELS", "RANGE_BOOTER", "RADAR_SENSOR"),
	ROVER_38 ("WHEELS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_39 ("NONE", "NONE", "NONE"),
	
	ROVER_41 ("WALKER", "DRILL", "EXCAVATOR"),
	ROVER_42 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_43 ("WHEELS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_44 ("WHEELS", "RANGE_BOOTER", "CHEMICAL_SENSOR"),
	ROVER_45 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_46 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_47 ("WHEELS", "RANGE_BOOTER", "RADAR_SENSOR"),
	ROVER_48 ("WHEELS", "EXCAVATOR", "RADAR_SENSOR"),
	ROVER_49 ("NONE", "NONE", "NONE"),

	// sample test rovers
	ROVER_00 ("WHEELS", "RADIATION_SENSOR", "RADAR_SENSOR"),
	ROVER_90 ("WHEELS", "RANGE_BOOTER", "RADIATION_SENSOR"),
	ROVER_91 ("WALKER", "DRILL", "EXCAVATOR"),
	ROVER_92 ("WHEELS", "RANGE_BOOTER", "SPECTRAL_SENSOR"),
	ROVER_93 ("TREADS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_94 ("WHEELS", "RANGE_BOOTER", "CHEMICAL_SENSOR"),
	ROVER_95 ("TREADS", "DRILL", "EXCAVATOR"),
	ROVER_96 ("WALKER", "DRILL", "RADIATION_SENSOR"),
	ROVER_97 ("WHEELS", "RANGE_BOOTER", "RADAR_SENSOR"),
	ROVER_98 ("WALKER", "DRILL", "SPECTRAL_SENSOR"),
	ROVER_99 ("TREADS", "SPECTRAL_SENSOR", "CHEMICAL_SENSOR");

    private final List<String> members;
    private RoverConfiguration(String... members){
        this.members=Arrays.asList(members);
    }
    public List<String> getMembers(){
        // defensive copy, because the original list is mutable
        return new ArrayList<String>(members);
    }

    public static RoverConfiguration getEnum(String input){
    	RoverConfiguration output;

    	switch(input){
    	case "ROVER_00":
    		output = RoverConfiguration.ROVER_00;
    		break;
    	case "ROVER_01":
    		output = RoverConfiguration.ROVER_01;
    		break;
    	case "ROVER_02":
    		output = RoverConfiguration.ROVER_02;
    		break;
    	case "ROVER_03":
    		output = RoverConfiguration.ROVER_03;
    		break;
    	case "ROVER_04":
    		output = RoverConfiguration.ROVER_04;
    		break;
    	case "ROVER_05":
    		output = RoverConfiguration.ROVER_05;
    		break;
    	case "ROVER_06":
    		output = RoverConfiguration.ROVER_06;
    		break;
    	case "ROVER_07":
    		output = RoverConfiguration.ROVER_07;
    		break;
    	case "ROVER_08":
    		output = RoverConfiguration.ROVER_08;
    		break;
    	case "ROVER_09":
    		output = RoverConfiguration.ROVER_09;
    		break;
    	case "ROVER_10":
    		output = RoverConfiguration.ROVER_10;
    		break;
    	case "ROVER_11":
    		output = RoverConfiguration.ROVER_11;
    		break;
    	case "ROVER_12":
    		output = RoverConfiguration.ROVER_12;
    		break;
    	case "ROVER_13":
    		output = RoverConfiguration.ROVER_13;
    		break;
    	case "ROVER_14":
    		output = RoverConfiguration.ROVER_14;
    		break;
    	case "ROVER_15":
    		output = RoverConfiguration.ROVER_15;
    		break;
    	case "ROVER_16":
    		output = RoverConfiguration.ROVER_16;
    		break;
    	case "ROVER_17":
    		output = RoverConfiguration.ROVER_17;
    		break;
    	case "ROVER_18":
    		output = RoverConfiguration.ROVER_18;
    		break;
    	case "ROVER_19":
    		output = RoverConfiguration.ROVER_19;
    		break;
    	case "ROVER_20":
    		output = RoverConfiguration.ROVER_20;
    		break;
    	
    	//Past semester archive of star rovers
    		
    	case "ROVER_31":
    		output = RoverConfiguration.ROVER_31;
    		break;
    	case "ROVER_32":
    		output = RoverConfiguration.ROVER_32;
    		break;
    	case "ROVER_33":
    		output = RoverConfiguration.ROVER_33;
    		break;
    	case "ROVER_34":
    		output = RoverConfiguration.ROVER_34;
    		break;
    	case "ROVER_35":
    		output = RoverConfiguration.ROVER_35;
    		break;
    	case "ROVER_36":
    		output = RoverConfiguration.ROVER_36;
    		break;
    	case "ROVER_37":
    		output = RoverConfiguration.ROVER_37;
    		break;
    	case "ROVER_38":
    		output = RoverConfiguration.ROVER_38;
    		break;
    	case "ROVER_39":
    		output = RoverConfiguration.ROVER_39;
    		break;
    		
    		
    		
    	case "ROVER_41":
    		output = RoverConfiguration.ROVER_41;
    		break;
    	case "ROVER_42":
    		output = RoverConfiguration.ROVER_42;
    		break;
    	case "ROVER_43":
    		output = RoverConfiguration.ROVER_43;
    		break;
    	case "ROVER_44":
    		output = RoverConfiguration.ROVER_44;
    		break;
    	case "ROVER_45":
    		output = RoverConfiguration.ROVER_45;
    		break;
    	case "ROVER_46":
    		output = RoverConfiguration.ROVER_46;
    		break;
    	case "ROVER_47":
    		output = RoverConfiguration.ROVER_47;
    		break;
    	case "ROVER_48":
    		output = RoverConfiguration.ROVER_48;
    		break;
    	case "ROVER_49":
    		output = RoverConfiguration.ROVER_49;
    		break;
    		

    	// Sample Rover Set
    	case "ROVER_90":
    		output = RoverConfiguration.ROVER_90;
    		break;
    	case "ROVER_91":
    		output = RoverConfiguration.ROVER_91;
    		break;
    	case "ROVER_92":
    		output = RoverConfiguration.ROVER_92;
    		break;
    	case "ROVER_93":
    		output = RoverConfiguration.ROVER_93;
    		break;
    	case "ROVER_94":
    		output = RoverConfiguration.ROVER_94;
    		break;
    	case "ROVER_95":
    		output = RoverConfiguration.ROVER_95;
    		break;
    	case "ROVER_96":
    		output = RoverConfiguration.ROVER_96;
    		break;
    	case "ROVER_97":
    		output = RoverConfiguration.ROVER_97;
    		break;
    	case "ROVER_98":
    		output = RoverConfiguration.ROVER_98;
    		break;
    	case "ROVER_99":
    		output = RoverConfiguration.ROVER_99;
    		break;
    	default:
    		output = RoverConfiguration.NONE;
    	}
    	return output;
    }
}
