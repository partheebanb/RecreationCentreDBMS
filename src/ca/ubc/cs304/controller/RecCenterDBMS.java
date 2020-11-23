package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainWindow;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class RecCenterDBMS implements LoginWindowDelegate {
	public DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	public RecCenterDBMS() {
		dbHandler = new DatabaseConnectionHandler();
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			MainWindow mainWindow = new MainWindow(this.dbHandler);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	public void launchGUI(String username, String password) {
		if (dbHandler.login(username, password)) {
			MainWindow mainWindow = new MainWindow(this.dbHandler);
		}
	}
    
	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		RecCenterDBMS recCenterDBMS = new RecCenterDBMS();

		// If args == 2, it means user passed in schema name and password
		// By pass the login screen and just login
		if (args.length == 2) {
			recCenterDBMS.launchGUI(args[0], args[1]);

		// Else show the login page
		} else {
			recCenterDBMS.start();
		}
	}
}
