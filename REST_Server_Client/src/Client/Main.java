package Client;
/**
 * 
 * @author Gilho Lee
 * Email : leeeeegilho 
 * linkdein : https://www.linkedin.com/in/gilho0608/
 * 
 */

import java.util.Scanner;


public class Main {
	
	private static Rest_Client agent;
	private static boolean check = true;
	private static String ip =null;
	private static int port =0;
	private static String uri = null;
	private static Main main =null;
	
	public static void main(String arsg[]) {

		main = new Main();

		main.module();
		
	}

	public static void module() {

		Scanner in = new Scanner(System.in);

			System.out.println("## Input SERVER IP :");
			ip = in.next();
			System.out.println("## Input SERVER PORT :");
			port = in.nextInt();
			System.out.println("## Input Uri ");
			uri = in.next();


			agent = new Rest_Client(ip, port, uri);
			agent.connect();

			main.module();


		}
	private static void sleep(int i) {
		// TODO Auto-generated method stub
		
	}

}
