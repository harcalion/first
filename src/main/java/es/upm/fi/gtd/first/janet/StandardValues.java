package es.upm.fi.gtd.first.janet;

/**
 * Abstract class used for supplying some standard values. <p>
 *
 * Revision list: <BR> Version 1.1: {@link #TIME_IDENTIFIER}, {@link
 * #STATE_IDENTIFIER}, {@link #PARAMETER_IDENTIFIER} and {@link
 * #AUXILIARY_IDENTIFIER} has been introduced.
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.1 (240900) 
 **/
public abstract class StandardValues {
    
    /**
     * This variable denotes the current version of Janet.
     **/
    public static final String JanetVersion = "1.09";
    /**
     * System dependent "Carriage Return" String object.
     **/
    public static final String CR = System.getProperty( "line.separator" );

    /**
     * The standard time for a progress bar to sleep between updates.
     **/
    public static final int PROGRESS_SLEEP_TIME = 1000;

    /**
     * The maximal number of visible rows in a combo box.
     **/
    public static final int MAX_ROW_COUNT = 5;

    /**
     * An integer value e.g. used for representing that the next
     * element an array is the value of time.
     **/
    public static final int TIME_IDENTIFIER = 0;

    /**
     * An integer value e.g. used for representing that the next
     * element an array is a state value.
     **/
    public static final int STATE_IDENTIFIER = 1;

    /**
     * The integer value e.g. used for representing that the next
     * element an array is a parameter value.
     **/
    public static final int PARAMETER_IDENTIFIER = 2;

    /**
     * The integer value e.g. used for representing that the next
     * element an array is a auxillary value.
     **/
    public static final int AUXILIARY_IDENTIFIER = 3;
    
    
    /**
     * Low standard value for initializing a {@link Janet.DataManager}
     * objects minimum size.  
     **/
    public static final int LOW_DATA_MANAGER_MIN = 25;

    /**
     * Low standard value for initializing a {@link Janet.DataManager}
     * objects maximum size.  
     **/
    public static final int LOW_DATA_MANAGER_MAX = 50;

    /**
     * Middle standard value for initializing a {@link Janet.DataManager}
     * objects minimum size.  
     **/
    public static final int MIDDLE_DATA_MANAGER_MIN = 100;

    /**
     * Middle standard value for initializing a {@link Janet.DataManager}
     * objects maximum size.  
     **/
    public static final int MIDDLE_DATA_MANAGER_MAX = 1000;

    /**
     * High standard value for initializing a {@link Janet.DataManager}
     * objects minimum size.  
     **/
    public static final int HIGH_DATA_MANAGER_MIN = 1000;

    /**
     * High standard value for initializing a {@link Janet.DataManager}
     * objects maximum size.  
     **/
    public static final int HIGH_DATA_MANAGER_MAX = 10000;

    /**
     * The default value of the interval between pollings
     * in {@link Janet.DebugObserver},
     * measured in milliseconds.
     **/
    public static final int DEFAULT_POLLING_INTERVAL = 1000; 

    /**
     * The default value of the maximum number of loggings
     * on objects being debugged
     * used in {@link Janet.DebugProxy}.
     **/
    public static final int DEFAULT_MAX_NUMBER_OF_DEBUG_LOGGINGS = 1000;

    /**
     * Name of the web server where Janet files may be downloaded.
     **/
    public static String WebServerName = "www.fys.dtu.dk";

    /**
     * Port number used by the web server.
     **/
    public static int WebServerPort = 80;

    /**
     * Name of mail server used for automatically generated bug reports.
     **/
    public static String MailServerName = "mail.fysik.dtu.dk";

    /**
     * Name of the Java compiler server defined in 
     * {@link Janet.JavaCompilerServer}.
     * Set to local value, and make sure the server only
     * responds to local requests.
     * Note that the compiler server must be started separately.
     **/
    public static String CompilerServerName = "localhost";

    /**
     * Port number used by the compiler server.
     **/
    public static int CompilerServerPort = 5000;

    /**
     * The maximum number of waiting clients for the Java compiler server.
     **/
    public static int MaxWaitingClients = 10;

    /**
     * The maximum number of clients served by the Java compiler server
     * simultaneously.
     **/
    public static int MaxSimultaneousClients = 2;
    /**
     * Name of the Java compiler server defined in 
     * {@link Janet.JavaCompilerServer}.
     * Set to local value, and make sure the server only
     * responds to local requests.
     * Note that the compiler server must be started separately.
     **/
    public static String SocketServerName = "localhost";

    /**
     * Port number used by the compiler server.
     **/
    public static int SocketServerPort = 5050;

    /**
     * The maximum number of waiting clients for the socket server.
     **/
    public static int SocketMaxWaitingClients = 10;

    /**
     * The maximum number of clients served by the socket server
     * simultaneously.
     **/
    public static int SocketMaxSimultaneousClients = 2;

} // StandardValues


