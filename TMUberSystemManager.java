//Raza Hussain Mirza
//501242038

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private ArrayList<User>   users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>(); 
    
    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    //for each loop
    for(User x: users)
    {
      //if accountID = any users accountID
      if(x.getAccountId().equals(accountId))
      {
        //Return the user
        return x;
      }
    }
    //Return null
    return null;
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {    
    //for each loop
    for(User x: users)
    {
      //if accountID = any users accountID
      if(x.equals(user))
      {
        //return true
        return true;
      }
    }
    //return false
    return false;
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 { 
   for(int i = 0; i < drivers.size(); i++)
   {
      //if driver in drivers array list at index i equals given driver, return true
      if (drivers.get(i).equals(driver))
      {
        return true;
      }
   }
   //else return false
   return false;
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    // I added thsi
    for(int i = 0; i < serviceRequests.size(); i++)
   {
      //if service in service requests array list at index i has a user that equals the given user, return true
      if (serviceRequests.get(i).getUser().equals(req.getUser()))
      {
        return true;
      }
   }
   //else return false
    return false;
  }

  // Calculate the cost of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  // Calculate the cost of a ride based on distance 
  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {
    //loop through drivers array
    for(int i = 0 ; i < drivers.size(); i++)
    {
      //check if any driver is available
      if(drivers.get(i).getStatus() == Driver.Status.AVAILABLE)
      {
        //return driver
        return drivers.get(i);
      }
    }
    //else return nothing
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    
    for (int i = 0; i < users.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    System.out.println();
    
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    System.out.println();
    
    for (int i = 0; i < serviceRequests.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-1s. %s", index, "-".repeat(60));
      serviceRequests.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String name, String address, double wallet)
  { 
    //Check for input errors
    if(name == null || name == "")
    {
      errMsg = "Invalid User Name";
      return false;
    }
    else if(wallet < 0)
    {
      errMsg = "Invalid Money in Wallet";
      return false;
    }
    else 
    {
      //create user and check if already exists
      User tempUser = new User("" + userAccountId + users.size() ,name, address, wallet);
      if(userExists(tempUser))
      {
        errMsg = "User Already Exists in System";
        return false;
      }
      else
      { 
        //add user to users array list
        users.add(tempUser);
        return true;
      }
    }
  }

  // Add a new driver to the system
  public boolean registerNewDriver(String name, String carModel, String carLicencePlate)
  {
    //Check for input errors
    if(name == null || name == "")
    {
      errMsg = "Invalid Driver Name";
      return false;
    }
    else if(carModel == null || carModel == "")
    {
      errMsg = "Invalid Car Model";
      return false;
    }
    else if(carLicencePlate == null || carLicencePlate == "")
    {
      errMsg = "Invalid Car Licence Plate";
      return false;
    }
    else 
    {
      //create driver and check if already exists
      Driver tempDriver = new Driver("" + driverId + drivers.size() ,name, carModel, carLicencePlate);
      if(driverExists(tempDriver))
      {
        errMsg = "Driver Already Exists in System";
        return false;
      }
      else
      { 
        //add driver to drivers array list
        drivers.add(tempDriver);
        return true;
      }
    }
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to)
  {
    // Check for valid parameters
	  // Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user at a time!
    // Change driver status
    // Add the ride request to the list of requests
    // Increment the number of rides for this user
    
    //Check for errors in input
    if(getUser(accountId) == null)
    {
      errMsg = "User Account Not Found";
      return false;
    }
    else if(!(CityMap.validAddress(from)) || !(CityMap.validAddress(to)))
    {
      errMsg = "Invalid Address";
      return false;
    }
    else if(CityMap.getDistance(from, to) < 1)
    {
      errMsg = "Insufficient Travel Distance";
      return false;
    }
    else if(getRideCost(CityMap.getDistance(from, to)) > getUser(accountId).getWallet())
    {
      errMsg = "Insufficient Funds";
      return false;
    }
    else if(getAvailableDriver() == null)
    {
      errMsg = "No Drivers Available";
      return false;
    }
    else
    {
      //create TMUberRide using given info adn check if ride already exists
      TMUberRide temp = new TMUberRide(getAvailableDriver(), from, to, getUser(accountId), CityMap.getDistance(from, to), getRideCost(CityMap.getDistance(from, to))); 
      if(existingRequest(temp) )
      {
        errMsg = "User Already Has Ride Request";
        return false;
      }
      else
      {
        //Set driver to status to DRIVER
        getAvailableDriver().setStatus(Driver.Status.DRIVING);
        
        //Add ride to service request array list
        serviceRequests.add(temp);
        
        //Add ride to user
        getUser(accountId).addRide();
        return true;
      }
    }
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food order id
    // Increment the number of deliveries the user has had

    //Check for errors in input
    if(getUser(accountId) == null)
    {
      errMsg = "User Account Not Found";
      return false;
    }
    else if(!(CityMap.validAddress(from)) || !(CityMap.validAddress(to)))
    {
      errMsg = "Invalid Address";
      return false;
    }
    else if(CityMap.getDistance(from, to) < 1)
    {
      errMsg = "Insufficient Travel Distance";
      return false;
    }
    else if(getDeliveryCost(CityMap.getDistance(from, to)) > getUser(accountId).getWallet())
    {
      errMsg = "Insufficient Funds";
      return false;
    }
    else if(getAvailableDriver() == null)
    {
      errMsg = "No Drivers Available";
      return false;
    }
    else
    {
      //create TMUberDilivery using given info adn check if ride already exists
      TMUberDelivery temp = new TMUberDelivery(getAvailableDriver(), from, to, getUser(accountId), CityMap.getDistance(from, to), getDeliveryCost(CityMap.getDistance(from, to)), restaurant, foodOrderId); 
      if(existingRequest(temp) )
      {
        errMsg = "User Already Has Delivery Request at Restaurant with this Food Order";
        return false;
      }
      else
      {
        //Add TMUberDilivery to service requests array list 
        serviceRequests.add(temp);

        //Add dilivery to user
        getUser(accountId).addDelivery();

        //Set driver status to Driving
        getAvailableDriver().setStatus(Driver.Status.DRIVING);
        return true;
      }
    }
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request)
  {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    
    //Check for valid input
    if(request > serviceRequests.size() || request <1)
    {
      errMsg = "Invalid Request #";
      return false;
    }
    else
    {
      //Store the service you want to remove in temp service
      TMUberService tempService = serviceRequests.get(request-1);

      //Get the user for the service u want to remove
      User tempUser = tempService.getUser();

      //If ride decrrement ride from user if dilivery decrement dilivery
      if(tempService.getServiceType().equals("RIDE"))
      {
        tempUser.decrementRide();
      } 
      else 
      {
        tempUser.decrementDilivery();
      } 
  
      //Remove from service requests
      serviceRequests.remove(request - 1);

      //Set driver status to available 
      tempService.getDriver().setStatus(Driver.Status.AVAILABLE);
    }
    return true;
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request)
  {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    
    
    // Drop off a ride or a delivery. This completes a service.
    // parameter request is the index in the serviceRequests array list
    
    //Check if input is valid
    if (request > serviceRequests.size() || request < 1) {
      errMsg = "Invalid Request #";
      return false;
    }  
    else 
    {
      //get the service request for the ride/delivery u want to remove
      TMUberService tempService = serviceRequests.get(request - 1);
      
      //Get the driver for the service u want to mark as done
      Driver tempDriver = tempService.getDriver();
      
      //Get the user for the service u want to mark as done
      User tempUser = tempService.getUser();
      
      double costOfService = tempService.getCost();

      // Deduct cost of service from user
      tempUser.payForService(costOfService);  

      // Pay the driver
      tempDriver.pay(costOfService * PAYRATE);

      // Update total revenue
      totalRevenue += costOfService-costOfService*PAYRATE;
      
      // Change driver status
      tempDriver.setStatus(Driver.Status.AVAILABLE);

      // Remove the completed service request
      serviceRequests.remove(tempService);
    }
    return true;
  }



  // Sort users by name
  // Then list all users
  public void sortByUserName()
  {
    Collections.sort(users, new NameComparator());
    listAllUsers();
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User>
  {
    //Compare name of user a to user b and return -1,0,1 depending on answer 
    public int compare(User a, User b)
    {
      return a.getName().compareTo(b.getName());
    }
  }

  // Sort users by number amount in wallet
  // Then list all users
  public void sortByWallet()
  {
    Collections.sort(users, new UserWalletComparator());
    listAllUsers();
  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User>
  {
    //I added this
    public int compare(User a, User b)
    {
      //Compare wallet of user a to user b and return -1,0,1 depending on answer 
    	if (a.getWallet() < b.getWallet()) return -1;
		  if (a.getWallet() > b.getWallet()) return  1;
		  return 0;
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance()
  {
    Collections.sort(serviceRequests);
  }
}
