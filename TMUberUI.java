//Raza Hussain Mirza
//501242038

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        if (!tmuber.registerNewDriver(name, carModel, license))
          System.out.println(tmuber.getErrorMessage()); 
        else
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s", name, carModel, license);
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl!! Only needed when mixing strings and int/double
        }
        if (!tmuber.registerNewUser(name, address, wallet))
          System.out.println(tmuber.getErrorMessage());  
        else
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestRide() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)

        //Get input for ride
        System.out.print("User Account Id: ");
        String accountId = scanner.nextLine();
        System.out.print("From Address: ");
        String from = scanner.nextLine();
        System.out.print("To Address: ");
        String to = scanner.nextLine();

        //check if input is valid
        if(!(tmuber.requestRide(accountId, from, to)))
        {
          System.out.println(tmuber.getErrorMessage());
        }
        else
        {
          //Get User name
          String name  = tmuber.getUser(accountId).getName();
          
          //Create ride using request ride 
          tmuber.requestRide(accountId, from, to);
          System.out.println();
          
          //Display ride info
          System.out.printf("RIDE for: %-15s From: %-15s To: %-15s", name, from, to);
        }
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestDelivery() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        // "Restaurant: "           (string)
        // "Food Order #: "         (string)
        
        //Get input
        System.out.print("User Account Id: ");
        String accountId = scanner.nextLine();
        System.out.print("From Address: ");
        String from = scanner.nextLine();
        System.out.print("To Address: ");
        String to = scanner.nextLine();
        System.out.print("Restaurant: ");
        String restaurant = scanner.nextLine();
        System.out.print("Food Order #: ");
        String orderNum = scanner.nextLine();

        //Check if info is valid 
        if(!(tmuber.requestDelivery(accountId, from, to, restaurant, orderNum)))
        {
          System.out.println(tmuber.getErrorMessage());
        }
        else
        {
          //Get user name for dilivery
          String name  = tmuber.getUser(accountId).getName();

          //Create delviery using request delivery method
          tmuber.requestDelivery(accountId, from, to, restaurant, orderNum);
          
          //Print info
          System.out.println();
          System.out.printf("DELIVERY for: %-15s From: %-15s To: %-15s ", name, from, to);
        }
      }
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        tmuber.sortByDistance();
      }
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        //Get an int input
        int request = -1;        
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        
        //display error if cancelServiceRequest
        if (!tmuber.cancelServiceRequest(request))
          System.out.println(tmuber.getErrorMessage());  
        else
          //print out info
          System.out.println("Service request #" + request + " cancelled");
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        //Get an int input
        int request = -1;
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl
        }

        //display error if cancelServiceRequest
        if (!tmuber.dropOff(request))
          System.out.println(tmuber.getErrorMessage());  
        else
            //print out info
          System.out.println("Successful Drop Off - Service request #" + request + " complete");
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        //get input 
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }

        //Print address
        System.out.print(address);
        
        //Check if address is valid 
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        //Get input
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }

        //Print info address
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      System.out.print("\n>");
    }
  }
}

