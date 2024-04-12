package com.se309.soccernexus.util;

import java.util.ArrayList;

/**
 * @author Nathan Turnis, Tristan Sayasit
 * Class for storing the global variables of the program
 */
public class Const {
    //Base server URL used for all endpoints
    public static final String BASE_SERVER_URL = "http://coms-309-019.class.las.iastate.edu:8080";

    //the signed-in ID the user is assigned upon login or sign up
    public static long playerID;

    //the username tied with the user
    public static String username;

    //the number of the teams the user is on
    public static int numTeams;

    //whether or not the user has a profile
    public static boolean hasProfile;

    //a list of id's of all the team's the user ison
    public static ArrayList<Long> teamsPlayerIsOn = new ArrayList<>();
}
