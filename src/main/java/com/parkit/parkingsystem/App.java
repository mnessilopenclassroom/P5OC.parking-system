package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * This is the main class that call the shell to load the interface and start the app
 */
public class App {

    private static final Logger logger = LogManager.getLogger("App");
    public static void main(String args[]) throws Exception {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
