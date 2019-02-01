/*
*
MovieGraph.java
COSC 102, Colgate University


*/

import java.io.*;
import java.util.*;



public class MovieGraph {

    private HashMap<String, HashSet<String>> graph;

    //Constructor
    //Gets passed all of the data from the read in file
    //in the form of an ArrayList of String arrays.
    //Each string array represents one line of the source data
    //split on the forward slashes '/'.
    public MovieGraph(ArrayList<String[]> data) {
        graph = new HashMap<String, HashSet<String>>();

        for (String[] row : data) {

            String movie = row[0];
            HashSet<String> act = new HashSet<String>();
            graph.put(movie, act);

            for (String human : row) {
                if (!human.equals(movie)) {
                    act.add(human);

                    HashSet<String> movieList = new HashSet<String>();
                    movieList.add(movie);

                    if (graph.putIfAbsent(human, movieList) != null)
                        graph.get(human).add(movie);
                }
            }
        }
    }

    //Returns an ArrayList of Strings which is the shortest path of movies/actors between
    //target1 and target2.
    //If no path can be found, can return either null or an empty ArrayList
    public ArrayList<String> findShortestLink(String target1, String target2) {
        if (graph.get(target1) == null || graph.get(target2) == null)
            return null;

        HashMap<String, String> history = searchLink(target1, target2);
        if (history.get(target2) == null)
            return null;

        return findPath(target1, target2, history);
    }

    private HashMap<String, String> searchLink(String target1, String target2) {

        HashMap<String, String> history = new HashMap<String, String>();
        history.put(target1, null);
        String current = target1;
        LinkedList<String> queue = new LinkedList<String>();

        while (!current.equals(target2)) {
            Iterator<String> iter = graph.get(current).iterator();

            while (iter.hasNext()) {
                String item = iter.next();
                if (!item.equals(target1) &&
                                    history.putIfAbsent(item, current) == null)
                    queue.add(item);
            }

            if (!queue.isEmpty())
                current = queue.remove();
            else
                current = target2;
        }

        return history;
    }

    private ArrayList<String> findPath(String target1, String target2,
                                            HashMap<String, String> history) {

        ArrayList<String> path = new ArrayList<String>();
        String current = target2;
        path.add(current);

        while (!current.equals(target1)) {
            current = history.get(current);
            path.add(0, current);
        }

        return path;
    }

    public static void main(String[] args) {}

}

