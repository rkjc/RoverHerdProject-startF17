# Rover Swarm Project

This project is a simulation of a set of autonomous rovers that are exploring, mapping, and harvesting science on an alien planet. The rovers have very limited capabilities individually and therefor have to operate as a swarm to fulfill their objectives.

![image of mars rovers](./docs/images/PIA15277_3rovers-hi_D2011_1215_D511_br2-1024x682.jpg)






<h1> Rover Swarm Project - Rover 02 </h1>

<p> This project is a simulation of a set of autonomous rovers that are exploring, mapping, and harvesting science on an alien planet. The rovers have very limited capabilities individually and therefore have to operate as a swarm to fulfill their objectives. </p>

![image of mars rovers](http://i.imgur.com/8n6arMu.jpg)



<h1 align=center > What's in this Documentation: </h1>

<h3>1.What are the movement commands? What are the scan commands?</h3>
  <p>- Brief information about the rover movement and some of the useful commands that you might need to understand the rover. </p>

<h3>2.What are the harvesting commands?</h3>
  <p>- Moving around is good but the purpose of these rovers is to harvest as much science as they can. In this section, you will find
    more about how rovers harvesting these sciences. </p>

<h3>3.What are the communication commands?</h3>
<p> - Each rover can't stand alone. To get the best out of each rover in less time, you need all the rovers to communicate together.
  In this section, you will find more information about how they communicate together through some methods calls. </p>

<h3>4.How are the pathfinding classes used?</h3>
 <p> - In this section, you will go deeper to understand how to rover is deciding the shortest path to a specific science using some algorithm. </p>

<h3>5.What are some design approaches to be considered for mapping behavior and harvesting behavior and when/how to switch from one to the other?
Also, what are some approaches to not getting the rovers stuck in a corner?</h3>
<p>- In this section, we explain more about rovers step-by-step decisions based on exploring or harvesting mode. </p>

<h3>6.What equipment is available to a rover and how is it configured?</h3>
 <p> - Each rover has its own equipment. We explain how to configure each rover with its own tools.</p>

<h3>7.Describe the different drive and tool types, how they are used and how they interact with the environment. Go into some of the design considerations for choosing different equipment configurations for small (5) medium (6-10) and large (10+) groups of rovers. How should tools and drive types be mixed?</h3>
<p>- This section talks more about the different type of drive and tools. How rovers have been equiped and how to make the best out of these equipment.</p>

<h3>8.Make some recommendations on how to improve the implementation of the project. Make some recommendations on additional features and functions to add to the simulation such as, liquid terrain features, hex vs. square map tiles, power limitations (solar, battery, etc.), towing, chance of break downs, etc</h3>
 <p> - Our ideas of how to impove this project. </p>

<h1 align=center> Group-2 </h1>

**1.What are the movement commands? What are the scan commands?**

The basic movement commands are the `moveNorth()`,`moveEast()`,`moveSouth()` and `moveWest()`. These methods are called from `Rover.java` which prints a statement with help of a [PrintWriter](https://docs.oracle.com/javase/7/docs/api/java/io/PrintWriter.html) object.

For instance, this is how the `moveNorth()` method is implemented:

```
protected void moveNorth() {
    sendTo_RCP.println("MOVE N");
   }
```

The `sendTo_RCP` is an instance of the `PrintWriter` class that sends the corresponding movement message to the `RoverCommandProcessor`. Based on the direction, the RCP determines its next coordinates. For example, a direction towards `South` would mean an increase in the `y` coordinate, thus we do `yCurrentPos = yCurrentPos + 1;` for going south.

All the Rovers are inside the (swarmBots) package inside the project files. All the implementation for each rover will be inside this package. Each rover will have different way to move based on the purpose and the tools of each Rover.

  ![image of swarmBots directory](http://i.imgur.com/LCKuaw4.png)

  When the roverâ€™s tools and abilities have been assigned, new movement for the rover or further improvement on it can be done. Rover-02, for instance, has these pre-assigned abilities and tools:

  **Rover02: (The Walker)**

  1.Walkers move much slower than Wheels and a little slower than Treads.

  2.Walkers can travel over Soil, Gravel, and Rocks.

  3.Walkers will immediately get stuck upon entering Sand terrain.

  4.SPECTRAL_SENSOR -> Crystal Science

  Spectral sensor four is exclusive to Rover-02's, other rovers canâ€™t catch Crystal Science on the map. Crystal objects can be detected by finding the letter ( C ) on the raw map in the project folder in eclipse.


The movement of the rover is indicated by four letters as East, West, North, South as the following:

            String[] cardinals = new String[4];
  			cardinals[0] = "N";
  			cardinals[1] = "E";
  			cardinals[2] = "S";
  			cardinals[3] = "W";

 <p> To move the rover to any of these directions, a simple call to one of the functions corresponding to that direction would suffice. This class has all the four function which each function will send the indicated letter matched the movement to the server to move your rover. </p>

<p>  Each rover is designed with a unique movement algorithm. This algorithm make sure that the rover knows where to go before moving. For example, the function below is making sure that the rover is checking whether the next move is allowed or not: </p>

          if (scanMapTiles[centerIndex +1][centerIndex].getHasRover()
              || scanMapTiles[centerIndex +1][centerIndex].getTerrain() == Terrain.SAND
              || scanMapTiles[centerIndex +1][centerIndex].getTerrain() == Terrain.NONE) {
            System.out.println(">>>>>>>EAST BLOCKED<<<<<<<<");
            blocked = true;
            stepCount = 5;  //side stepping
          } else {
            moveEast();		

<p>  ROVER-02 is not allowed to step over sand and over another rover. These conditions are also pre-assigned to each rover. However, since the rover is checking each move, the rover has to communicate with the communication server to send whether this move is allowed or not, if allowed, then move. If this move is not allowed, then based on algorithm that this rover has, get a different direction move and check.

  All these commands and communication is inside the while loop which makes the rover keep going unless the rover is stuck somewhere.

<h1>Scan Commands </h1>

The `ScanMap.java` is the main controller responsible for scanning the entire map. This program contains a scanArray, size and the coordinates as the parameters which is initialized to null if the rover is at the start position. </p>

```
public ScanMap(){
  this.scanArray = null;
  this.edgeSize = 0;
  this.centerPoint = null;		
}

public ScanMap(MapTile[][] scanArray, int size, Coord centerPoint){
  this.scanArray = scanArray;
  this.edgeSize = size;
  this.centerPoint = centerPoint;		
}
```
As it moves it stores details of the coordinate in a `scanArray` which is used to get the details of a particular coordinate. Another purpose of this program is that, this is input for creating the map by the rover, as the details of the coordinates are stored in an array.

![scanMap](http://i.imgur.com/un23bl6.png)

This helps to create the map which can be used to share the information with the communication server as well other rovers.

Each Rover has been assigned a tool. Each tool has the ability to harvest a specific type of the science. Later in this documentation, we will describe more about these tools.

One of the important things you might need to know as well is the objects and their shapes:

 Crystal ( C ) : <img src="https://s28.postimg.org/bx5ewp0nd/Screen_Shot_2017-05-03_at_2.07.34_PM.png" width="5%" /> 		Radioactive ( R ): <img src="https://s28.postimg.org/guizht2mh/Screen_Shot_2017-05-03_at_2.07.26_PM.png" width="5%"/>		


Organic ( O ): <img src="https://s28.postimg.org/dri9efnnt/Screen_Shot_2017-05-03_at_2.07.54_PM.png" width="5%"/>
Mineral( M ): <img src="https://s28.postimg.org/wvbkus0i1/Screen_Shot_2017-05-03_at_2.07.42_PM.png" width="5%"/>    

To apply any of these sciences to the map, you just need to add the letter corresponding to the science name as showing:

```
    NONE("N")
    RADIOACTIVE("Y")
    ORGANIC("O")
    MINERAL("M")
    ARTIFACT("A")
    CRYSTAL("C")
```

Also there are several types of Terrains that you can add to create you own map or add more difficulties to the existing one as showing:

<img src="https://s28.postimg.org/gzmqrh9xl/Screen_Shot_2017-05-03_at_7.02.57_PM.png" width="30%"/>

These are defined in Terrain.java in the enums package:

<img src="https://s28.postimg.org/jwed5i559/Screen_Shot_2017-05-03_at_7.12.06_PM.png" width="60%"/>

```
   NONE ("X")
   ROCK ("R")
   SOIL ("N")
   GRAVEL ("G")
   SAND ("S")
   FLUID ("F")
```

Each rover has been configured with set of tools, type of science that rover can get, and the type of terrains the rover can go over without getting stuck.
Rover tools are:

<img src="https://s28.postimg.org/j5lmzq2rt/Screen_Shot_2017-05-03_at_7.05.51_PM.png" width="30%" />

**2.What are the harvesting commands?**

The harvesting mechanism in the rovers are implemented through the `GATHER` commands and its associated mechanism. Firstly, when the `GATHER` command is issued, if the rover is positioned on a tile that contains a sample of science, and if the rover
has the proper extraction tool for that particular tile terrain, then the science is removed from the map and placed
in the roverâ€™s cargo storage.

While traversing, the rovers keep checking if the `scienceDetail` instance of class `ScienceDetail` is null. If not, it means that the server has responded with a specific kind of science. The rover then prints out the details for that particular science first:

```
if (scienceDetail != null) {
		System.out.println("FOUND SCIENCE TO GATHER: " + scienceDetail);
	}
```

To have a deeper look at the `scienceDetail` instance, we need to go to the base class for the rovers, `ROVER.java`:

```
protected ScienceDetail analyzeAndGetSuitableScience() {
		ScienceDetail minDistanceScienceDetail = null;
		try {
			Communication communication = new Communication("http://localhost:3000/api", rovername, "open_secret");

			ScienceDetail[] scienceDetails = communication.getAllScienceDetails();
			RoverDetail[] roverDetails = communication.getAllRoverDetails();

			if (roverDetails == null || roverDetails.length == 0) {
				if (scienceDetails != null && scienceDetails.length > 0) {
					return analyzeAndGetSuitableScienceForCurrentRover(scienceDetails);
				}
			}
      ...
      ...
    }
```

The `analyzeAndGetSuitableScience()` method does the bulk of the work for the gathering procedures. The `getAllScienceDetails` method returns an array of the following:

* `x` and `y` coordinates

* if it has a rover

* the name of the science itself

* the terrain it was found on, and

* setter values for the rover that found and gathered it

Back in `ROVER_02.java`, if we get something in the `scienceDetail`, we do either of two things. First, if our current position coincides with the position of the science, we gather it and print out a statement saying the science has been gathered:

```
if ( scienceDetail.getX() == getCurrentLocation().xpos
							&& scienceDetail.getY() == getCurrentLocation().ypos ) {
						gatherScience( getCurrentLocation() );
						System.out.println( "$$$$$> Gathered science "
								+ scienceDetail.getScience() + " at location "
								+ getCurrentLocation() );
					}
```

Otherwise, the rover will head towards that science by calling the pathfinding algorithm, in this case, the A-Star. Before doing so, it sets its appropriate `driveType` and `ToolType`. Afterwards, it sets its motion based on the response it gets from the A-Star, which will be either `N` for North, `E` for East, `W` for West or `S` for South.

<h3> **3.What are the communication commands?** </h3>

The `communication.java` contains the required methods for communicating with the server as well as other rovers. This program contains code for getting the details of the rover as in what are the features it contains, the coordinate location of the rover it is approaching, the science details.
```
if (roverDetail == null) {
  throw new NullPointerException("roverDetail is null");
}
```
In this case, a `NullPointerException` is thrown because the `roverDetail` object (instance of the class `RoverDetail`) is null. Otherwise the `roverDetailMsg` JSONObject can be populated with relevant data in the following way:

```
roverDetailMsg.put("roverName", roverDetail.getRoverName());
roverDetailMsg.put("x", roverDetail.getX());
roverDetailMsg.put("y", roverDetail.getY());
```

In order to communicate with the server, several request headers are added. This depends on the API call that's being made. For example, in order to send a `GET` request to the URL `/science/all`, the following request headers are added:
```
      con.setRequestMethod("GET");
		con.setRequestProperty("Rover-Name", rovername);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
```
where `con` is an instance of `HttpURLConnection`.

This communication will perform these activities for communicating with other rovers as well as with the server.

When the rovers are moving throughout the map, they will be conversing with the communication server and vice versa. In order to make that happen, we are using an API. This API provides us with a variety of options for doing RESTful services. As a result, we will be able to extract valuable information as each of the rovers are making their way through the map and define necessary actions based on the information we get. For example: when the communication server is running, the call ` /api/science/all` gives us the following result:

```
[
  {
      hasrover: false,
      science: "CRYSTAL",
      x: 27,
      y: 8,
      terrain: "SOIL",
      f: 2,
      g: 2
  },
  {
      hasrover: false,
      science: "CRYSTAL",
      x: 24,
      y: 10,
      terrain: "SOIL",
      f: 2,
      g: 2
  }
]
```
In this case, we are receiving the information that Rover_02 has found `CRYSTAL` in the terrain `SOIL` at positions `x` and `y` stated above. The Communications Server API contains several other API calls that returns detailed information for `Global`, `Sciences`, `Gather`, `Coordinate` and `Misc`.

**4.How are the pathfinding classes used?**

The basic feature of the path finding algorithm is the implementation of the A-Star algorithm. The purpose of the A-Star algorithm is to find the shortest path at each step. The rovers, while traversing through their corresponding maps, make a call to the `findPath` method in the class `A-Star` like the following:

```
char dirChar = aStar.findPath( getCurrentLocation(),
              new Coord( scienceDetail.getX(),
              scienceDetail.getY() ),driveType );
```
The above statement is implemented in the rover program, the variable `dirChar` is going to have the values in which direction the rover has to move, say `N`,`S`,`W`,`E`.

This statement is repeatedly called and it returns which direction the rover has to move next. That is, if the rover is standing at a current location then it finds the next step which it has to take based on the A-Star algorithm.

The findPath method implemented in the A-Star class returns the series of steps that the rover has to follow in order to reach to the crystal (or any other science material based on its configuration).
If there is a block then the variable will have an 'U' which means that particular coordinate cannot be visited by the rover. In the following declaration of the findPath method, the first two argument it takes are instances of the `Coord` class. When implementing we are sending the `getCurrentLocation` `ew Coord( scienceDetail.getX(),
scienceDetail.getY()` to be those two arguments. The last argument is the driveType, and instance of `RoverDriveType`:

```
public char findPath(Coord start, Coord dest, RoverDriveType drive) {
  // If destination coordinate is blocked/unreachable, return U
    if (blocked(dest, drive)) {
      return 'U';
    }
    ...
    ...
}
```
The following snippet from the method `findPath` helps to get the current location of the rover's coordinate and then stored into a variable called current:

```
 while(!openSet.isEmpty()) {
            Coord current = null;
            for(int i = 0; i < openSet.size(); i++) {
                if(current == null || fScore[openSet.get(i).xpos][openSet.get(i)
                .ypos] < fScore[current.xpos][current.ypos]) {
                    current = openSet.get(i);
                }
```
In order to check whether the rover has moved from the previous coordinate, so that it can ensure that the algorithm provides the required path towards which it has to move further.

```
if(current.equals(dest)) {
                Coord prev = cameFrom[current.xpos][current.ypos];
                while(!start.equals(prev)) {
                    current = prev;
                    prev = cameFrom[prev.xpos][prev.ypos];
                }
```

Based on the current location it redirects to the direction the rover has to move further. Thus the final result will be a character that instructs the rover of its next direction.
```
if(current.ypos < start.ypos) {
                    return 'N';
                } else if(current.xpos > start.xpos) {
                    return 'E';
                } else if(current.ypos > start.ypos) {
                    return 'S';
                } else {
                    return 'W';
                }
```

Here is a demonstration of the scenario when the Rover-02 is moving south because it received an `S` from the A-Star, meaning the nearest crystal is towards its south.

![A_Star img](https://i.imgur.com/gyjKWpF.png)


 **5.What are some design approaches to be considered for mapping behavior and harvesting behavior and when/how to switch from one to the other?**
 **Also, what are some approaches to not getting the rovers stuck in a corner?**

Currently, with each step, the rover is trying to find if itâ€™s the closest one to a science, using A-Star:

    char dirChar = aStar.findPath( getCurrentLocation(),
          new Coord( scienceDetail.getX(),
          scienceDetail.getY() ),
          driveType );

If it is, it travels to that science and prints out the following pair of messages that have the detail for the science as well as the location.

    if(scienceDetail.getX() == getCurrentLocation().xpos
          && scienceDetail.getY() == getCurrentLocation().ypos ) {
        gatherScience( getCurrentLocation() );
        System.out.println( "$$$$$> Gathered science "
            + scienceDetail.getScience() + " at location "
            + getCurrentLocation() );
      }

On the other hand, while a rover is locating a piece of science, if it finds that another rover is closer to the science, the current rover switches to the default explore mode and lets the other rover harvest that science.

 ```
 roverDetail.setRoverMode( RoverMode.EXPLORE );
 ```

The movement for the rovers have been implemented in a way where it checks if the rover is going to face an end in the terrain in its next move:

```
  if(scanMapTiles[centerIndex -1][centerIndex].getTerrain() ==Terrain.NONE){...}
```
If this is true, the rover considers that to be a block and implements the sidestepping logic it has for facing a `block`. It sets the `stepCount` to a value of 5, does sidestepping accordingly, and then check if the next move is valid once again.


**6.What equipment is available to a rover and how is it configured?**

The list of equipments available vary for each of the rovers. Upon every request to access the list of equipments for each of the rovers, we will get one drive type (instance of the `RoverDriveType` class) and two tool types (instance of the `RoverToolType` class). We do this if the command sent to the server consists of the string `â€œEQUIPMENTâ€�`:

```
else if(input.startsWith("EQUIPMENT")) {
        	Gson gson = new GsonBuilder()
        			.setPrettyPrinting()
        			.enableComplexMapKeySerialization()
        			.create();
        	ArrayList<String> eqList = new ArrayList<String>();

        	eqList.add(rover.getRoverDrive().toString());
        	eqList.add(rover.getTool_1().toString());
        	eqList.add(rover.getTool_2().toString());
```

The result returned by the server will consist of a series of text strings. The number of lines of text returned is
variable.

The first line of text returned will be the string â€œEQUIPMENTâ€�. The following lines will be an `ArrayList<String>` object that has been converted to a string json format.

The last line of text returned will be the string `â€œEQUIPMENT_ENDâ€�`. When reconstructed the ArrayList will contain a listing of the Rover Drive system Type and the two RoverToolType attachments. The Drive and ToolTypes will be listed by their string converted names.

The following example shows how the `RoverToolType` and `RoverDriveType` classes handle the cases for each tool type/drive type:

```
RoverToolType output;
switch(input){
  ...
  case "SPECTRAL_SENSOR":
    		output = RoverToolType.SPECTRAL_SENSOR;
    		break;
        ...
      }
```

```
RoverToolType output;
switch(input){
  ...
  case "WALKER":
      		output = RoverDriveType.WALKER;
      		break;
        }
```

When the rovers are traversing the map, either following their default movement logic or the pathfinding (A-Star) algorithm, they will be communicating with the central server, which in our case is called the communication server. Alongside this communication server works the class `Rover.java`, which serves the purpose of being the base class for all the rovers. In this base class, we are implementing a method to retrieve a list of the rover's equipment from the server.

```
protected ArrayList<String> getEquipment() throws IOException {
  ...
  if (jsonEqListIn.startsWith("EQUIPMENT")) {
			while (!(jsonEqListIn = receiveFrom_RCP.readLine()).equals("EQUIPMENT_END")) {
				if (jsonEqListIn == null) {
					break;
				}
				jsonEqList.append(jsonEqListIn);
				jsonEqList.append("\n");
			}
		}
```

As stated above, this portion of the method checks if the string that was returned starts with `"EQUIPMENT"`. Upon satisfying this condition it goes until the last line of the text returned and keeps appending to the StringBuilder instance `jsonEqList`. Otherwise, it would simply mean that the server response did not start with "EQUIPMENT" and would return a null in that case. Finally it will return an ArrayList that contains a listing of the Rover Drive system Type and the two RoverToolType attachments.

Rovers 01, 02 and 03 have different drive types and tool types and based on their corresponding type, their actions vary. For example, due to Rover_02's having a `"SPECTRAL_SENSOR"`, it will be able to detect a crystal science, but might not be able to detect other types of science. The types are defined in the `RoverConfiguration` class:


```
    ROVER_01 ("WHEELS", "EXCAVATOR", "CHEMICAL_SENSOR"),
	ROVER_02 ("WALKER", "SPECTRAL_SENSOR", "DRILL"),
	ROVER_03 ("TREADS", "EXCAVATOR", "RADAR_SENSOR"),
```

In this project, we are running a simulation of NASA's mars rovers. In reality, for NASA's Mars Science Laboratory mission, Curiosity, the following are the detectors and their related instruments:

![radiation_detector_nasa_curiosity](http://i.imgur.com/s9nxYK6.png)

More information for Curiosity's sensors and detectors can be [found here](https://mars.nasa.gov/msl/mission/instruments/radiationdetectors/)



**7.Describe the different drive and tool types, how they are used and how they interact with the environment. Go into some of the design considerations for choosing different equipment configurations for small (5) medium (6-10) and large (10+) groups of rovers. How should tools and drive types be mixed?**

The different types of drive and tool types are:

* The wheels which can travel on soil and gravel but not on rock and sand

* The walkers are the slowest of all the three types of the drive tools. The main advantage is that they can walk over all except sand.

* The treads are similar to walkers but are little faster when compared with the walkers. The main purpose of these treads are that they are used to run over sand.

Next comes the extraction tools, there are two types of extraction tools:  

* Drills can extract samples of science from Rock or Gravel terrain.
* Excavators can extract samples of science from Soil or Sand terrain.

These are used in rock and gravel, soil and sand respectively. Another important tool is the scanner tools, the scanning tools are:

* Radiation Sensor(Scans radioactive material)
* Chemical Sensor(Scans organic material)
* Spectral Sensor(Scans crystal science material)
* Radar Sensor(Scans mineral science material)

Another type of tool is the Range Extender, which helps to extend the visibility from 7x7 to 11x11 square.

All the rovers should have the extraction tools. Let 1/3 of the rovers be wheelers, other 4/5 be walkers and treads. The main reason for the wheelers is less because they can move faster and extract in larger amount.


**8.Make some recommendations on how to improve the implementation of the project. Make some recommendations on additional features and functions to add to the simulation such as, liquid terrain features, hex vs. square map tiles, power limitations (solar, battery, etc.), towing, chance of break downs, etc**

  - One of the difficulties that might be added to the rover's movements are the ability to avoid obstacle that aren't part of their configuration.  
  - In the future, rovers could be configured to know whether they have already Explored a certain area so they wouldn't go back there again.
  - One of the ideas that could be implemented later is that rovers can establish a separate communication protocol for a map and another for movements.

The rovers can be given the ability to sense the liquid terrain and also need to ensure that they can drill through them. While moving, it has to be ensured that the rover does not get toppled upside down. Additional features that could be added are, to prevent the parts from eroding by the exposure of cosmic rays, additional sensors can to be added to enhance each of the rovers' features.







# CS-5337-Spring 2017 Rover Swarm Project

# Group-3 - Describe how the Rover Swarm Project simulation engine works

**About the Simulation Project:**

The rover simulation software contains three major components that work in coordination to provide the simulation where rovers explore any given terrain made of different types of material and collect and analyze material substances of interest called science located at different regions of the terrain. Examples of terrain types are soil, rock, fluid, gravel, etc. Examples of sciences are minerals, radioactive materials, etc.

**Components of Rover Simulation Project:**

The following are the three major components of the rover simulation software:

1.	Rover Command Processor
2.	Rovers
3.	Communication Server

**Rover Command Processor**

The rover command processor is the Java Swing user interface (UI) that reads the map information stored in a text file format and displays the visual representation of the map terrain along with the rovers and science information. The rover command processor also receives the movement information from each of the rovers and updates the display to show the most current location of each of the rovers. The rover command processor and the rovers are packaged into the same Java project called "RoverSwarmProject."

**Rovers**

Rovers are Java programs that contain algorithms to explore the terrain looking for sciences. Each rover has different drive types such as wheels, treads, legs (walkers), etc., and they have different capabilities and are equipped with different types of tools to analyze and report the different types of sciences. Rovers may encounter sciences that they are capable of gathering and analyzing or they may encounter sciences that they are not equipped to handle. If they encounter sciences they cannot gather and analyze, they report the sciences they have discovered to the communications server that the other rovers equipped to handle that science can read and harvest. The rover java programs are packaged along with the rover command processor into the same Java project: "RoverSwarmProject."

**Communication Server**

The communication server is a RESTful server application implemented in Node.JS. The communication server mimics a satellite that receives messages sent by rovers on the ground and also broadcasts information to rovers. The information shared via communication server includes details about activities of all active rovers in the terrain being explored, the details about all sciences that were communicated to the communication server by the rovers and the overall terrain map information showing what regions of the overall map have been explored so far and what portion is yet to be explored.

![image 1](./docs/images/1.png)

**RoverSwarmProject â€“ Java Project Structure**

The following table lists all Java packages in the RoverSwarmProject project and descriptions of key classes in those packages.

![image 2](./docs/images/2.png)

**SwarmCommunicationServer â€“ Node.JS application structure**

The following is the table that shows various key files in the SwarmCommunicationServer Node.JS application:

![image 3](./docs/images/3.png)

### How can the project be set up to run locally using Eclipse, command line, or runnable JARâ€™s?

First install Git on your local machine in order to pull and push code from GitHub and the local machine.

Use the following link for reference.

(https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

To run the Rover Swarm Project locally, you need to take care of the following things:

	* Pull code from GitHub repository.
	* Set up that code in local machine.

Now, setting up the project in the local machine can be done using Eclipse.
Running this project depends on the way you have files in your local computer, i.e., JAR, BAT files, or inside Eclipse as a whole project.

#### Using Eclipse

To set up the project locally using Eclipse, follow the steps mentioned below:

#### Grab Rover Code

1. In your local machine, create a new folder where you want to store your project.
2. Create a blank Eclipse Java project in that folder.
3. Create a folder ROVER_XX inside that folder, Where XX represents your rover number.
4. Go to your rover-specific repository on GitHub and use the big green button to copy the link to the clipboard.

![image for clone link](./docs/images/4.png)

5. On your local machine folder where you're storing your project, create a folder ROVER_XX. (Again as previously, XX represents your rover number.)
6. Open the terminal at that location and type following commands:
```
  1. git init
  2. git remote set origin "paste your url here"
  3. git pull origin master
```
7. Now, rename this folder to something else and create folder ROVER_XX.
8. Copy everything from the other folder to your src folder under ROVER_XX folder.
9. Now, once you open Eclipse and refresh your project, you'll get all the files there, but with several errors.
10. To remove all errors, right click on your project, navigate to Build Path --> Configure Build Path under the Java Build Path tab, click on Libraries, and click on add External JARs.

![image for adding jars](./docs/images/5.png)

11. Navigate to your project folder, and inside the Lib folder, add all the JAR files and refresh your project. Errors are now gone.

#### Grab Communication Server

Communication server does not need to be started in order to run the Rovers, but it must be started first if you want all the Rovers to communicate with one another.

Setting up the communication server is fairly similar to the Rover process.

The prerequisite is to have Node JS installed on your computer.

Note: Follow the link below for reference.

(https://nodejs.org/en/download/)

1. Now, go to your course repository on GitHub, and then go to the Swarm Communication Server Repo and copy link to clone as shown in the Rover part.

2. Create a local folder with any name on your local computer.

3. Open the terminal to that location and type in the following commands:
Note: Make sure you have Git installed.
```
1. git init
2. git remote set origin "paste url to swarm communication server"
3. git pull origin master
```

#### Running the Project

1. Run Swarm Communication Server by opening the terminal to the Swarm Communication Server folder and type in the following command:

```
node app.js
```
Note: Make sure you have Node JS installed.

2. Inside your Eclipse project under src/controlServer, run `RoverCommandProcessor.java`.
This will run the Map and the Command Processor, and then you'll be able to see all Rovers at their initial positions.

3. Now, to make your Rover move, run the `ROVER_XX.java` file under the src/swarmBots folder from Eclipse.

### How are the Windows BAT files used, and how are the runnable JARs generated in Eclipse and used?

#### **JAR Files**

	* JAR files are created to easily run the program so we don't have to go through the entire sequence of steps from Eclipse each time.

	* There are two main files of which JAR files need to be created.

1) ROVER_XX.java

2) RoverCommandProcessor.java

Steps to create JAR files are as follows:

1. Right click on your Java file and click on `Export`.

2. Under the Java tab, select `Runnable Jar Files`.

![Jar file 1](./docs/images/6.png)

3. Click `Next` and under Launch Configuration, select your Java file and export destination, and then select your location to save.

![Jar file 2](./docs/images/7.png)

4. Click `Next` and follow the remaining steps.

Note: Just ignore all errors from Eclipse because these executable JAR files will still run.

**Running JAR files**

* Open the terminal at that location, and type in the following command:
```
java -jar <myjarfile.jar>
```

**BAT files**

1. BAT files are essentially shortcuts to run JAR files so that you don't have to run the Rovers using a terminal.

2. First, make sure your BAT file and JAR files are in the same directory.

3. BAT files can be created using any text editor. Open the text editor and type in the following command:
```
start java -jar JarFileName.jar
```
4. Save it as a .bat file.

5. Double click to run the BAT file.

**Note:** You can use one BAT file to run multiple JAR files. See the example below.

```
start java -jar ROVER_XX.jar
start java -jar ROVER_XX2.jar
```

### What is the sequence for starting the project?

#### Using Eclipse to start a project without the use of the Swarm Communication Server:

	* First, go to your Rover Project and open the source folder.

	* Locate and open the Control Server package.

	* Locate the Rover Command Processor dot java file.

	* Right click on it, and select run as Java application.

	* A GUI Map will display on your screen, and the console will display the message > The Swarm server is running.  

	* Next, locate and open the Swarm Bots package.

	* Select the Rover that you want to run.

	* Right click on it and select run as Java application.

	* Your Rover is now moving in the GUI Map.

#### Using Eclipse to start a project with the use of the Swarm Communication Server:

	* First, make sure you have Node JS installed in your system.

	* Open Git Bash or Command Prompt, and navigate to the Swarm Communication Server project.

	* To start the Swarm Communication Server, enter `node app.js`.

	* It will display the message > Express app running on port 3000.

	* Note: To terminate the Swarm Communication Server hit `control + c`.

	* Next, go to your Rover Project and open the source folder.  

	* Locate and open the Control Server package.

	* Locate the Rover Command Processor dot java file.

	* Right click on it, and select run as Java application.

	* A GUI Map will display on your screen, and the console will display the message > The Swarm server is running.

	* Finally, locate and open the Swarm Bots package.

	* Select the Rover that you want to run.

	* Right click on it, and select run as Java application.

	* Your Rover is now moving in the GUI Map, communicating with the server and other Rovers, and collecting pieces of science.

### How are the maps structured and edited? How can the RoverCommandProcessor be started with a custom map?

* The maps are structured as a text file where each square has X and Y coordinates.  The (0, 0) coordinates are at the top left corner.  You can place the starting position for your particular Rover anywhere on the map by adding the Rover's number to the square on the map, as long as there is no duplicate.  There are some key terrain and science features on the map such as R for rock, G for gravel, S for sand, X for abyss, Y for radioactive, C for crystal, M for mineral, and O for organic.

* Because the map is a text file, you can pretty much edit anything as long as it is consistent with all the default values.

* To start the Rover Command Processor with a custom map, all you have to do is add the map to your project folder and name it MapDefault.txt.

### How can ROVER_xx be started with a custom URL?

#### Running on a Custom Server:

First, we need to install Node and Java on the server. Then, to run the Swarm Communication Server, install the dependencies in `package.json` and run the server file `app.js` with node. Finally, run the Java Swarm project by first running the Rover Command Processor and then running any Rover Swarm Bots.

### Make some recommendations on how to improve the implementation of the project.

#### Possible Implementations to Improve Simulation

There can be a system that regulates time by days and keeps account of months and years.  If this is implemented, there can be all sorts of applications. The first thing is to have weather implemented in such a way that when it is winter, the weather will be colder, and the program will create ice on the ground at night; therefore, it could greatly hinder the movement of the Rovers. Also, random occurrences of clouds block sunlight to the solar panels, which will slow down the exploration and resource gathering process of the Rovers.  There could also be dust storms that could possibly block the solar panels from receiving any light.  When a Rover breaks down, it would be a random occurrence because of weather conditions.  There can also be a random breakdown, depending on how much time and how far it has traveled.

##### Possible Implementations on Rover Capabilities

1. checkRange() - This will check the range between two Rovers and its ETA of both Rovers.

2. foundResources() - This will give the location of found resources from all Rovers and display which Rover has discovered what resources.

3. setQueue() - This will set the queue for the Rover to retrieve its next resource.

4. getQueue() - This will show the next resource to which the Rover will be attending.

5. GetCurrentQueue() - This will show the current resource to which the Rover is attending.   

6. getQueueOptions() - This will give options regarding the Rover's next available resource.

7. getRoversQueue() - This will display every Roverâ€™s current resource and its next resource.   

8. switchToLongMessages() - If need be, this will project more messages for the Rover.

9. statusCheckConnectionAll() - This will forward all messages to and from each Rover and return the time it takes to communicate.

10. statusCheckDiagnosisAll() - This will check all Rovers and do a diagnosis.



# **CS 5337 - Spring 2017 Advanced Software Engineering**
## **Group 1: Describe how the Communication Server works**
### Node.js & java


Computer Science Department <br/>
California State University, Los Angeles

**Description:** <br/>
Communication Server acts as a source of mediator between the Server and the Rovers, constantly transferring the required information to the Rovers regarding the co-ordinates, locations of science on the map, messages and etc. All communications between the server and the rover are text based i.e., a series of Strings are shared between the Rover and the Communication Server in order to establish a successful connection between them. For the Communication Server to successfully communicate with the Rovers we need an API (Application Program Interface) to do that. We use these API's with a set of commands. 

## **Communication Server**

The Communication Server acts as the link between the Rovers and the Rover Command Processor so that the rovers can retrieve and send information to the Command Processor. To be precise it is a restful Server that is implemented as a node.js application. It simulates a spacecraft that receives multiple requests from different rovers and also broadcast information based on the rovers request. The information that is sent through the communication server specifies the details about all the active rovers that are exploring the  
map on their respective terrain, It also includes the locations of all sciences that have been discovered and communicated by each rover to the communication server and the complete map showing what regions of the map have been explored and the locations which have not been explored yet. 
<br>

![initial scan](http://i.imgur.com/71sgoFe.jpg)
<br><br>

Once the rover starts exploring the map, it constantly sends the post request to the java communication in order to get the response from the Communication Server with the response of an array of JSON Objects. The rover keeps scanning the tiles and updates the information on what terrain or sciences have been located by the rover and keeps the Communication Server up to date with that information. This information will then be available to different rovers with the request and response messages which is a two way handshake process. All the objects that are located by each rover is also stored in the Communication Server as a JSON Object using the sendRoverDetailJSONDataToServer() method. These changes are also reflected in the console in the form of a basic UI as shown below:

![scanned locations](http://i.imgur.com/zd1kNv5.jpg)

<br>

## **What are the restful API interface commands?**

The restful API commands are pretty much the same commands that we use to send GET and POST requests to the server whenever the rover is either trying to retrieve some information from the server or push the data to the server. That is when we use these commands in the form of a URL and send a request to the server that is sent as a message to the server and the server responds back with a series of string type messages. Usually these messages are considered to be JSON Objects. Let us consider an example, one of the rovers on the planet is trying to get the map details to explore the map it send a GET request to the server using the [GET] [/api/global](/api/global) command in order to send a request to the server to get the next tile location on the map as shown in the picture above. Then the server responds by returning the global as a JSON array through which you can pull the required data from the JSON array. When it comes to sending information like science locations i.e., marking tile for gather etc, to the server then you send POST requests to the server using the [POST] [/api/gather/x/y](/api/gather/x/y) to send the details of the respective rover to the Communication Server. Here the 'x' & 'y' represent the rover coordinates on the map based on the X and Y axis of the map respectively. Again the restful API commands are categorized into different types, they are:


![restful API types](http://i.imgur.com/bWIr8D9.jpg)


Each of these types have their own set of commands in order to send and receive information through the java communications class which are listed below:

## Global
* [POST] [/api/global](/api/global)
    - it contains an array of JSON Objects sent by each active rover on the map or the planet which includes the coordinates of all the active rovers along with their terrain type, science locations, which rover found what type of science and which rover could gather what and returns everything as an array of JSON Objects in the following format:

```
    [
        {
            "x":12,                 // coordinates must be Integers, not String
            "y":14,                 // ALL CAPS as in enums folder
            "terrain": "SAND",      // ROCK, SOIL, GRAVEL, SAND, NONE
            "science": "CRYSTAL",   // RADIOACTIVE, ORGANIC, MINERAL, CRYSTAL, NONE
            "f": 12                 // Found by Rover 12 (for debugging)
            "g": 15                 // Marked by Rover 15 for gathering
        }, ...
    ]
```

* [GET] [/api/global](/api/global)
    - It's just the same as the POST request but the only difference is in the POST method the information is given by the rover whereas with the GET request the rover is acquiring the information from the server that is the server is responding back to the rover by sending an array of JSON Objects for the rover to read the locations or updated science information on the map provided by other active rovers on the map.

* [GET] [/api/global/size](/api/global/size)
    - This API is most useful for testing purposes where the team can test the map boundaries for the planet so that the rovers don't keep wandering away from the specific location where the sciences are said to be located if you look at it as a real time based scenario.


* [GET] [/api/global/reset](/api/global/reset)
    - It is **highly** recommended to use this API only when you wish to reset the entire data on the map including the information sent by the rovers and the information that was already in-built in the initial stages.

The map that is sent or received by the rovers and the communication server is also available in a GUI format which looks something like this 

![map image](http://i.imgur.com/fMatJ4N.jpg)

As shown in the map above there are different types of terrains and science that is available throughout the map which we will discuss later.

## Science

* [GET] [/api/science/all](/api/science/all)
    - This api is used from the java communication class to the server in order to retrieve all the science locations from the server which also includes the type of terrain on which the science is on. These locations are constantly updated by the rovers whenever they find a new science that is located on the map and is pushed to the server using that specific API request command. The overview of all the objects that are included in the array of JSON Objects is shown below:

```
    [
        {
            "x": 19,
            "y": 47,
            "science": "ORGANIC",
            "terrain": "SOIL",
            "f": 13,  // found by rover 13
            "g": 18   // marked by rover 18 for gather
       } ...
    ]

```

Each active rover on the map is allocated two different types of tools. One is the scanner where as the other could either be a drill or an excavator. The first one is used to scan the map tiles to find whether there exists any science at that location or not and the latter one is used to harvest that specific tile where the science is located but this is limited only to the rover that can both harvest that tile as well as has the necessary tools to scan the science on that location. There are different types of sciences available all over the map which will be discussed going forward 

* [GET] [/api/science/drill](/api/science/drill)
    -  This API specifies all the locations of sciences that is possible for the driller to extract. For example, If you have a rover that is a WALKER and has a drill as one of its tools and there's a science on a ROCK Terrain, when you use this API command the locations are popped up on the map so that the rover can go to that location to drill and gather the science out of that terrain. 

* [GET] [/api/science/excavate](/api/science/excavate)
    -  This API specifies all the locations of sciences that is possible for the excavator to extract. For example, If you have a rover that is WHEELS and has an excavator as one of its tools and there's a science on a SOIL or GRAVEL Terrain, when you use this API command the locations are popped up on the map so that the rover can go to that location to excavate and gather the science out of that terrain.

## Gather
* [POST] [/api/gather/x/y](/api/gather/x/y)
    - When the rover is exploring the map and it finds that there's some science next to its locations then it send this information to the server and the server sends a broadcast message to all the other rovers. The rover that is most close to that location will mark that tile as gathering tile by this API by including the rover coordinates based on the a and y axis points of the map. When a rover points the gathering update to a specific location then that specific tile details look like this which includes Header which consists of the ROVER_Name and the Corp-Secret value and the post request with its coordinate value as shown below

```
        header: 'Rover-Name' : rovername (ie. ROVER_11)
        header: 'Corp-Secret': corp_secret
        POST: /api/gather/78/52

        Now the tile looks like this:
        {
                "x":78,
                "y":52,
                "terrain": "SAND",      // ROCK, SOIL, GRAVEL, SAND, NONE
                "science": "CRYSTAL",   // RADIOACTIVE, ORGANIC, MINERAL, CRYSTAL, NONE
                "f": 10                 // Found by Rover 10 (for debugging)
                "g": 11                 // Marked by Rover 11 for gathering   
        }

```


## Coordinate
* [GET] [/api/coord/:x/:y](/api/coord/:x/:y)
    - As explained in GATHER API when a rover finds some science but cannot gather it, it sends a GET request to the server using the x and y coordinate locations so that the other rover who is close by can locate it and if possible gather it. 

* [POST] [/api/coord/:x/:y/:science](/api/coord/:x/:y/:science) 
    - This API is pretty much the same as the previous one except for that these locations are forwarded by the rovers to the communication server notifying that it has found some science that it is visible to it and updates the science location on the map for the same. These updates can also be seen by the other rovers

## Miscellaneous
* [GET] [/api/roverinfo](/api/roverinfo)
    - Each and every minute detail about the rover is stored in this API that is all the rover information including the name, id, sensor, tools and the drive system of the rover is stored in this API. 

## **What is and how does the java communications interface class work?**
The java communications class is the interface to the SwarmCommunicationServer nodejs/javascipt program. There are three parameters that we use constantly sue in the communications class, their brief description is shown below:

 
Parameter | Description 
---------|----------
 url | used to define the link to the restful or interface API Commands 
 rovername | contains all the details about the specific rover 
 JSON Objects | contains all the details about that specific object (Ex: Rover Locations, Excavating Tools, Harvesting Tools, Destination sent by the other rover)

Since the communications between the rover and the server are all text based, whenever a rover sends a request to the server, the requests will be bi-directional, the server intermittently sends a response back to the rover with one or more lines of text. These responses will either be a simple text or can be objects that are encoded by json. The java communication class is responsible to send and retrieve information of the rover to the Communication Server. We use different methods to do so both to send information as well as retrieve the information.


## **_Methods used to send data to the Server_**
![sending details to the server](http://i.imgur.com/BSbw8zT.jpg)

<br>

## **_Methods used to get the data from the Server_**
![getting details from the server](http://i.imgur.com/2LNK8ZK.jpg)

All the above specified restful API commands are used in the java communications and the javascript file for the server to send responses back to the requests made by the rover to retrieve the information to make the necessary changes like moving to the specified destination by other rover, gathering sciences on a specific location of the map, drilling or excavating science from the terrain, finding status of other rovers on gathering sciences. All these requests and responses are mostly relied on the GET and POST methods in the Communication Class. 


## **What are some recommendations for different ways in which the rovers can negotiate duties such as mapping or harvesting?**
In order to increase the potential of the program it is best to use the "Divide and Rule" policy where each rover is assigned a different job to do. Let us say we have 3 rovers (walkers, wheels and treads) roaming around the planet. Instead of the rovers being all over the place we can distribute different duties to each rover like the wheels are the fastest rovers on the map so it will be advantageous for it to explore the map and update different locations of the map constantly. Doing so the other rovers can focus on which place they want to go and since not all rovers can move over all types of terrains, the treads which are a little slower than the wheels can go over the terrains Sand, Soil and Gravel whereas the walkers which are the slowest of all the rovers can travel over Soil, Gravel and Rocks but get stuck upon entering the Sand Terrain and the wheels get stuck as soon as they enter the sand terrain so it is best for the Treads rover to find the type of surrounding that are on different locations of the map. Once we have the map locations with what type of terrain is there at which location each of the rovers can the do their own will of harvesting sciences. But, how do they do it when each of the rover is travelling in a different location and what would be the worst case scenario where the rover is in a location where it sees something but it cannot extract it. This is when we can use the API's to communicate with the server and MAP the location to the server to communicate with the other rovers saying "Hey I found something over here, it's open for Harvesting". 

The image below shows that ROVER_02 has found something but can't harvest it and changes the tiles status to open for harvest.
![found something](http://i.imgur.com/Y8Q164T.jpg)

Other rovers let us take ROVER_05 being the closest is trailing upon a different science location since it's status is pending so it sends a message to the Communication Server that it is already going to get a science from a different location.

![pending to harvest something else on the map](http://i.imgur.com/WENrwR0.jpg)

Then ROVER_01 being the next closest ROVER to the science location sends a message to the Communication Server saying hey I'm open for gathering and then changes it's status from open to pending harvest with the tile coordinates as the destination and highlights the path to it. 

![open for gathering science](http://i.imgur.com/8r6HCWe.jpg)

Doing so, any rover that is close to that location can change the status of going to that location and harvesting the tile to pending. So, each map tile that has some sort of science will have some sort of data which also gives information about the status of the tile as explained below.

<p align="center"><img src="http://i.imgur.com/EYcns25.jpg"></p>

As shown above status has three different definitions namely open, pending and done which is basically given as the parameters to a map tile which adds an additional JSON object called status. Each value of this JSON Object defines a different meaning to the status value which is shown below:

![status_description](http://i.imgur.com/u7E5e6B.jpg)

Now in the future version if there is any hidden trap and the rover has fallen into this trap then the rover sends a message to the server with a new JSON object which can be a boolean value which changes to true if stuck else it remains falls. If all the active rovers in the map are given the capacity to have a towing tool then we can apply the same logic that we have used for mapping the status of the tile for gather logic to the rover towing when stuck in a hidden trap that the rover is not aware of. For us to do that in a much simple way where the complexity of the logic is as minimum as possible we can use the highlighted path logic for the rovers current location to the destination where the rover is stuck because this highlighted path cannot pass through the obstacles until and the unless the rover has the tendency to go over that terrain type. Once the rover reaches the other rover that is trapped in the hidden trap then it can use the towing logic to pull the rover out of that trap. Below is the example on how the JSON values look like for the hidden trap and trapped rovers that need to be towed out. 

```
[
  {
      rs: 4                 // Rover Stuck is 4
      trapped: true,        // Rover is trapped? yes
      x: 27,                // x coordinate
      y: 8,                 // y coordinate
      terrain: "HIDDEN",    // terrain type
      tb: 3                 // towed by Rover 3
  }
]
```

![stuck on hidden trap](http://i.imgur.com/OdnMkb6.jpg)


## **How can additional commands and functions be added to the Communications Server and accessed or utilized by the rovers?**

* Providing some extra information on the distance between the rover and the science will be helpful instead of applying different logics by the rovers to calculate the distance between the rovers and the sciences. 

* The highlighted path logic can be directly implementing the highlighted path logic into the server with necessary parameters passed to the function that are color coordinated to each rover will give a clear user interface to the viewer as to which rover is going towards which direction or tile location. This function will also reduce the chances of rovers colliding with each other based on the estimated time to reach each science location. 

* Declaring API commands in app.js and create a HTTP Connection on rover side to call those commands. For example let us consider the following code to be written in the communication class

```
readScienceDetailJSONDataFromServer(){
    ....
     obj = new URL(url + "/science/all/");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    ....
}

```
and use the following commands in javascript

```
 app.get('/api/science/:option', function (req, res)  

```
Doing so we can directly create an object in the communication class and call its method to get the location of all the sciences in the map

```
(ScienceDetail[] scienceDetails = communication
               .getAllScienceDetails();)

```
![Highlighted path](http://i.imgur.com/sF4OGsN.jpg)

* The above shown image is as of now only limited to a single rover since all the rovers were to create their own RoverCommandProcessor initially. So if this kind of logic is directly used in the RoverCommandProcessor for all the rovers many loopholes can be covered like running into rovers or getting stuck in an unknown land or even bumping into walls if the rover logic is implemented the correct way.
 

## **Make some recommendations on how to improve the implementation of the project. Make some recommendations on additional features and functions to add to the simulation such as, liquid terrain features, hex vs. square map tiles, power limitations (solar, battery, etc.), towing, chance of break downs, etc.**

Few of the recommendations for the implementations for better functionality of the project will include:

### **Simulation**

* A central server which has the tendency to locate all kinds of sciences and terrain will be helpful to send the data to the rovers which will also save time for scanning each and every tile on the map for exploring purposes.

*  Different tracking methods like ranging and doppler can be implemented for the rover to simulate better results compared to the current tracking scheme.

* In the original rover mechanisms there's a newer method called "delta differential one-way range measurement" that adds information about the location of the spacecraft in directions perpendicular to the line of sight.

### **Hex map vs. Square map**

* Using a Hex map in place of a square map will be a little more complicated for the rovers to write the logic when moving to a different location to gather science or tow a different rover, since the rover can move in 6 different directions in place of 4.

* The rovers should implement logic not just for NORTH, EAST, WEST & SOUTH but also for NORTH-EAST, NORTH-WEST, SOUTH-EAST & SOUTH-WEST direction for the rover to successfully roam around the planet without any obstruction.

* Yet, there are high chances of falling into a loop where the rover keeps moving in circles in the same location of the map.


### **Advantages of using Solar Panels alongside Batteries**

* Having a battery powered rover alone and having charging stations on a different planet is pointless so, all the rovers have their own type of solar panels which have a prime advantage when they are exploring towards the equatorial region where they can get enough sunlight to re-energize their batteries. 

* To increase the efficiency of the batteries, it is suggested to develop a logic in such a way that whenever the battery is losing its energy, it can move over to the equatorial region of the planet to generate sufficient energy to roam around the unexplored land of the planet

### **Future Implementations**

* Since the weather of the MARS planet is not known, when there's a dust storm or clouds like entities covering the rover that blocks sunlight then there's no the rover can generate power and travel over the terrain of the planet. This can effect almost all the rovers at a specific time.

* Rovers can carry an additional power source which acts as a power backup in case of emergency power to reach a good sunlight location to recharge.

* Rovers can be configured in such a way that it can recharge another rover if it has more than the minimum amount of power. This can be implemented the same way we have used for the towing process.
