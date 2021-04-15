/**
 * Created by sayali on 4/11/21.
 */

import com.jcraft.jsch.*;

import java.util.Scanner;
import java.io.Console;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author YOUR NAME
 */
public class OnlineTicTacToe implements ActionListener {
    private final int INTERVAL = 1000; // 1 second
    private final int NBUTTONS = 9; // #bottons
    private ObjectInputStream input = null; // input from my counterpart
    private ObjectOutputStream output = null; // output from my counterpart
    private JFrame window = null; // the tic-tac-toe window
    private JButton[] button = new JButton[NBUTTONS]; // button[0] - button[9]
    private boolean[] myTurn = new boolean[1]; // T: my turn, F: your turn
    private String myMark = null; // "O" or "X"
    private String yourMark = null; // "X" or "O"
    private boolean server=false;
    /**
     * Prints out the usage.
     */
    private static void usage() {
        System.err.
                println("Usage: java OnlineTicTacToe ipAddr ipPort(>=5000) [auto]");
        System.exit(-1);
    }

    /**
     * Prints out the track trace upon a given error and quits the application.
     *
     * @param an exception
     */
    private static void error(Exception e) {
        e.printStackTrace();
        System.exit(-1);
    }

    /**
     * Starts the online tic-tac-toe game.
     *
     * @param args[0]: my counterpart's ip address, args[1]: his/her port, (arg[2]: "auto")
     *                 if args.length == 0, this Java program is remotely launched by JSCH.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            // if no arguments, this process was launched through JSCH
            try {
                OnlineTicTacToe game = new OnlineTicTacToe();
            } catch (IOException e) {
                error(e);
            }
        } else {
            // this process wa launched from the user console.
            // verify the number of arguments
            if (args.length != 2 && args.length != 3) {
                System.err.println("args.length = " + args.length);
                usage();
            }
            // verify the correctness of my counterpart address
            InetAddress addr = null;
            try {
                addr = InetAddress.getByName(args[0]);
            } catch (UnknownHostException e) {
                error(e);
            }
            // verify the correctness of my counterpart port
            int port = 0;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                error(e);
            }
            if (port < 5000) {
                usage();
            }
            // check args[2] == "auto"
            if (args.length == 3 && args[2].equals("auto")) {
                // auto play
                OnlineTicTacToe game = new OnlineTicTacToe(args[0]);
            } else {
                // interactive play
                OnlineTicTacToe game = new OnlineTicTacToe(addr, port);
            }
        }
    }

    /**
     * Is the constructor that is remote invoked by JSCH. It behaves as a server.
     * The constructor uses a Connection object for communication with the client.
     * It always assumes that the client plays first.
     */
    public OnlineTicTacToe() throws IOException {
        // receive an ssh2 connection from a user-local master server.
        Connection connection = new Connection();
        input = connection.in;
        output = connection.out;

//         for debugging, always good to write debugging messages to the local file
//         don't use System.out that is a connection back to the client.

        PrintWriter logs = new PrintWriter(new FileOutputStream("logs.txt"));
        logs.println("Autoplay: got started.");
        logs.flush();
        myMark = "X"; // auto player is always the 2nd.
        yourMark = "O";



//        String message = "Hello from slave at: "+InetAddress.getLocalHost().getHostName();
//
//        output.writeObject(message);
//        output.flush();

//        try{
//            String actionId= (String)(input.readObject());
//            logs.println(actionId);
//            logs.flush();
//        } catch ( Exception e ) {
//            e.printStackTrace( );
//            System.exit( -1 );
//        }
        // the main body of auto play.


        // IMPLEMENT BY YOURSELF

        boolean listing = true;
        server=true;

        while (listing){
            // start my counterpart thread
            Counterpart counterpart = new Counterpart();
            counterpart.start();
        }
//        try (ServerSocket indexServerSocket = new ServerSocket()) {
//            while (listing) {
//                new Counterpart(indexServerSocket.accept()).start();
//            }
//        } catch (IOException e) {
//            System.err.println("Could not listen on server port");
//            System.exit(-1);
//        }
    }

    /**
     * Is the constructor that, upon receiving the "auto" option,
     * launches a remote OnlineTicTacToe through JSCH. This
     * constructor always assumes that the local user should play
     * first. The constructor uses a Connection object for
     * communicating with the remote process.
     *
     * @param my auto counter part's ip address
     */
    public OnlineTicTacToe(String hostname) {
        final int JschPort = 22; // Jsch IP port
        // Read username, password, and a remote host from keyboard
        Scanner keyboard = new Scanner(System.in);
        String username = null;
        String password = null;
        // The JSCH establishment process is pretty much the same as Lab3.
        // IMPLEMENT BY YOURSELF
        // establish an ssh2 connection to ip and run
        // Server there.
//        String  command= "";


        try {
            // read the user name from the console                                             \

            System.out.print("User: ");
            username = keyboard.nextLine();

            // read the password from the console                                              \
            System.out.print("password: ");
            password = keyboard.nextLine();

//            Console console = System.console( );
//            password = new String( console.readPassword( "Password: " ) );

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String cur_dir = System.getProperty("user.dir");

        cur_dir = cur_dir + "/out/production/ds";  //need to remove
       // System.out.println(cur_dir);

        String command
                = "java -cp " + cur_dir + "/jsch-0.1.55.jar:" + cur_dir +
                " OnlineTicTacToe";

        //System.out.println(command);

        Connection connection = new Connection(username, password,
                hostname, command);
       // the main body of the master server
        input = connection.in;
        output = connection.out;

//
//        try{
//            String message= (String)(input.readObject());
//
//            System.out.println(message);
//        } catch ( Exception e ) {
//            e.printStackTrace( );
//            System.exit( -1 );
//        }
        // set up a window
        makeWindow(true); // I'm a former

        // start my counterpart thread
        Counterpart counterpart = new Counterpart();
        counterpart.start();
    }

    /**
     * Is the constructor that sets up a TCP connection with my counterpart,
     * brings up a game window, and starts a slave thread for listenning to
     * my counterpart.
     *
     * @param my counterpart's ip address
     * @param my counterpart's port
     */
    public OnlineTicTacToe(InetAddress addr, int port) {
        // set up a TCP connection with my counterpart
        // IMPLEMENT BY YOURSELF
        // set up a window
        makeWindow(true); // or makeWIndow( false );
        // start my counterpart thread
        Counterpart counterpart = new Counterpart();
        counterpart.start();
    }

    /**
     * Creates a 3x3 window for the tic-tac-toe game
     *
     * @param true if this window is created by the former, (i.e., the
     *             person who starts first. Otherwise false.
     */
    private void makeWindow(boolean amFormer) {
        myTurn[0] = amFormer;
        myMark = (amFormer) ? "O" : "X"; // 1st person uses "O"
        yourMark = (amFormer) ? "X" : "O"; // 2nd person uses "X"
        // create a window
        window = new JFrame("OnlineTicTacToe(" +
                ((amFormer) ? "former)" : "latter)") + myMark);
        window.setSize(300, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(3, 3));
        // initialize all nine cells.
        for (int i = 0; i < NBUTTONS; i++) {
            button[i] = new JButton();
            window.add(button[i]);
            button[i].addActionListener(this);
        }
        // make it visible
        window.setVisible(true);
    }

    /**
     * Marks the i-th button with mark ("O" or "X")
     *
     * @param the  i-th button
     * @param a    mark ( "O" or "X" )
     * @param true if it has been marked in success
     */
    private boolean markButton(int i, String mark) {
        if (button[i].getText().equals("")) {
            button[i].setText(mark);
            button[i].setEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * Checks which button has been clicked
     *
     * @param an event passed from AWT
     * @return an integer (0 through to 8) that shows which button has been
     * clicked. -1 upon an error.
     */
    private int whichButtonClicked(ActionEvent event) {
        for (int i = 0; i < NBUTTONS; i++) {
            if (event.getSource() == button[i])
                return i;
        }
        return -1;
    }

    /**
     * Checks if the i-th button has been marked with mark( "O" or "X" ).
     *
     * @param the i-th button
     * @param a   mark ( "O" or "X" )
     * @return true if the i-th button has been marked with mark.
     */
    private boolean buttonMarkedWith(int i, String mark) {
        return button[i].getText().equals(mark);
    }

    /**
     * Pops out another small window indicating that mark("O" or "X") won!
     *
     * @param a mark ( "O" or "X" )
     */
    private void showWon(String mark) {
        JOptionPane.showMessageDialog(null, mark + " won!");
    }

    /**
     * Is called by AWT whenever any button has been clicked. You have to:
     * <ol>
     * <li> check if it is my turn,
     * <li> check which button was clicked with whichButtonClicked( event ),
     * <li> mark the corresponding button with markButton( buttonId, mark ),
     * <li> send this information to my counterpart,
     * <li> checks if the game was completed with
     * buttonMarkedWith( buttonId, mark )
     * <li> shows a winning message with showWon( )
     */
    public void actionPerformed(ActionEvent event) {
        // IMPLEMENT BY YOURSELF

        int actionId = whichButtonClicked(event);
        markButton(actionId, "O");

        try {


            output.writeObject("ActionId" + actionId);
            output.flush();

            Object action = input.readObject();
            System.out.println((String) action );

        } catch (Exception e) {

        }
    }

    /**
     * This is a reader thread that keeps reading fomr and behaving as my
     * counterpart.
     */
    private class Counterpart extends Thread {
        private Socket socket = null;


        public Counterpart() {

        }
//        public Counterpart(Socket socket) {
//            super("Counterpart");
//            this.socket = socket;
//        }

        /**
         * Is the body of the Counterpart thread.
         */
        @Override
        public void run()  {

            if(server) {
                String purpose = null;
                try {


//                        String message = "Hello from slave at: "+InetAddress.getLocalHost().getHostName();
//
//                        output.writeObject(message);
//                        output.flush();

                   String action = (String) input.readObject();

                  output.writeObject("return" + action);
                    output.flush();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
