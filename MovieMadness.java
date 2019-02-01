/*
 * 
 MovieMadness.java
 
 COSC 102, Colgate University

 DO NOT MODIFY ANY CODE IN THIS FILE 
 */

import java.io.*;
import java.util.*;

public class MovieMadness
{
  
  
  

  
  
  
  /*
   MoviesMadness.main({file})
   
   Takes the filename of a movie database as an argument.
   All but the first argument will be ignored.
   */
  public static void main (String[] args) {
    //retrieves Dataset file from command-line argument
    if (args == null || args.length < 1) {
      System.out.println("Movies Madness: error; no database file provided");
      System.exit(1);
    }
    
    MovieGraph mg = createMovieGraph(args[0]);
    promptUser(mg);
    System.out.println("Thanks for playing!"); 
  }
  
  
  //Parses data from file, uses parsed data to create Movie Graph object
  //terminates program if format is invalid
  private static MovieGraph createMovieGraph(String fname){
    try {
      ArrayList<String[]> data = new ArrayList<String[]>();
      Scanner s = new Scanner(new File(fname));
      while (s.hasNextLine())
        data.add(s.nextLine().trim().split("/"));  
      s.close();
      return new MovieGraph(data);     
    } 
    catch (FileNotFoundException e) {
      System.err.println("Movie Madness: database file " + fname + " not found");
      System.exit(2);
    } 
    catch (SecurityException e) {
      System.err.println("Movie Madness: security violation reading file " + fname);
      System.exit(3);
    }     
        
    return null; //dummy, program terminates if invalid file is provided
  }
  
  
  //Prompts user for movie/actor names, and displays results from MovieGraph  
  //Stops when user enters !quit
  private static void promptUser(MovieGraph mg){
    Scanner s = new Scanner(System.in);
    int response = 0;
    String input1 = "";
    String input2 = "";
    

    while (true) {
      if (response == 0){
        System.out.print("Enter a actor or movie name, or type !quit to exit: ");
        input1 = s.nextLine();
        response++;
      }
      else{
        System.out.print("Enter another actor or movie name: \n");
        input2 = s.nextLine();
        response++;
      }
      if (input1.toLowerCase().equals("!quit") || input2.toLowerCase().equals("!quit")){
        return;
      }
      else if(response % 2 == 0){
        System.out.println("Finding shortest link for targets: '" + input1 + "' and '" + input2 + "': \n");
        ArrayList<String> link = mg.findShortestLink(input1, input2);
        if (link == null || link.size() == 0)
          System.out.println("No link found!\n\n");
        else{
          int numOfLinks = link.size();
          System.out.println("Link found in " + numOfLinks + " steps!");
          for (int i = 0; i < numOfLinks - 1; i++)
            System.out.print(link.get(i) + " --> ");
          System.out.println(link.get(numOfLinks - 1) + "\n\n");
        }
        response = 0;
      }
    }    
  }
  
  
  
  
}
