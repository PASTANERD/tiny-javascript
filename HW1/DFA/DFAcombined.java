package DFA;

import java.util.ArrayList;
import java.util.Scanner;


public class DFAcombined{
	
	
	private static DFAcombined model;
	private DFA_Models dfa;
	private Scanner reader = new Scanner(System.in);
	private String checkString;
	private ArrayList<String> quitOptions;
	private ArrayList<String> IntegerSet;
	private ArrayList<Integer> DFA_A_AcceptStates;
	private ArrayList<Integer> DFA_B_AcceptStates;
	private ArrayList<Integer> DFA_C_AcceptStates;
	private char[] input_stream;
	private boolean epsilon = false;

	
	private DFAcombined() {
		
		init(); // to make sure initial conditions which are AcceptStates for DFAs, Exit conditions and etc.
		run(); // run DFA machine
		
	}
	
	
	public static void main(String args[]) {
		model = new DFAcombined();
	}
	
	/* method run() is in charge of taking sentences. */
	private void run() {
		print("Type E or e to exit");
		while(true) {
			print("Input: ");
			string_reader();
			if(quitOptions.contains(checkString)) {	
				print("Program is terminated. Thank you");
				break;
			}
			else {
				if(checkString.length() < 1) {
					epsilon = true;
				
				} else epsilon = false;
				scan_generator(input_stream);
			}
		}
	}
	
	/* method string_reader() assigns received strings into String and character array to */
	private void string_reader() {
		checkString = reader.nextLine();
		input_stream = checkString.toCharArray();
	}
	
	
	/* method print() is for using System.out.println() more easy */
	private void print(String message) {
		System.out.println(message);
	}
	
	/* method scan_generator() is to determine which DFA model is needed.*/
	private void scan_generator(char[] string) {
		if(epsilon || string[0] == 'a') {
			dfa = new DFA_Model_A();
			dfa.set_model(0, DFA_A_AcceptStates, string);
			dfa.stream_reader();
		}
		else if (string[0] == '#' || string[0] == '$') {
			dfa = new DFA_Model_B();
			dfa.set_model(0, DFA_B_AcceptStates, string);
			dfa.stream_reader();
		
		}
		else if (string[0] == 's' || string[0] == 'x') {
			dfa = new DFA_Model_C();
			dfa.set_model(0, DFA_C_AcceptStates, string);
			dfa.stream_reader();
			
		}
		else {
			print("Rejected");
		}
		
	}
	
	/* method init() is to set initial conditions. */
	private void init() {
		quitOptions = new ArrayList<String>(); // quitOption is set of special words which contains e, E, exit, Exit, EXIT that makes this program terminates. 
		IntegerSet = new ArrayList<String>(); // IntegerSet is set of integers in String, from 0 to 9, to make comparing more easy
		DFA_A_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model A which is related to problem 1
		DFA_B_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model B which is related to problem 2
		DFA_C_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model C which is related to problem 3
		quitOptions.add("e");
		quitOptions.add("E");
		quitOptions.add("exit");
		quitOptions.add("Exit");
		quitOptions.add("EXIT");
		IntegerSet.add("0");
		IntegerSet.add("1");
		IntegerSet.add("2");
		IntegerSet.add("3");
		IntegerSet.add("4");
		IntegerSet.add("5");
		IntegerSet.add("6");
		IntegerSet.add("7");
		IntegerSet.add("8");
		IntegerSet.add("9");
		DFA_A_AcceptStates.add(0);
		DFA_A_AcceptStates.add(1);
		DFA_A_AcceptStates.add(2);
		DFA_A_AcceptStates.add(4);
		DFA_B_AcceptStates.add(3);
		DFA_C_AcceptStates.add(3);
		DFA_C_AcceptStates.add(7);
		
	}

	
	/** DFA models are created into subclass 
	 * 	There is a meta model for DFA (name is DFA_Models) and each specific models(which are DFA_Model_A, DFA_Model_B, and DFA_Model_C) extend this.
	 * meta model DFA_Models has 2 method for common use (set_model and stream_reader). 
	 * 	set_model is setting DFA model where other class want to use DFA.
	 * 	At that class, the class have to set initial state and finish state. also turn over input string.
	 * 	stream_reader is method for reading input string word by word. so that make DFA read string and change its state until the string is over.
	 * 
	 * 	transition method is abstract for preserve diversity. each different DFA may need their own transition function.
	 *  By leaving this method as abstract, each DFA will override their own transition function.
	 * **/
	
	
abstract class DFA_Models{
	
	int state;
	ArrayList<Integer> acceptState = new ArrayList<Integer>();
	char[] stream;
	
	public void set_model(int init_state, ArrayList<Integer> finishState, char[] input_string) {
		this.state = init_state;
		this.acceptState = finishState;
		this.stream = input_string;
		
	}
	
	private void stream_reader(){
		int IC = 0; // index counter
		int string_length = stream.length;
		
		while(IC < string_length){
			transition(stream[IC]);
			IC++;
		}
		
		if(acceptState.contains(state)) {
			if(IC < 1) print("Accepted (empty string)");
			else print("Accepted");
		}
		else print("Rejected");
	}
	
	public abstract void transition(char word);
	
}

// DFA model for problem 1, its language is {a, b}
class DFA_Model_A extends DFA_Models{

	@Override
	public void transition(char word) {
		switch(state) {
			case 0 :
				if(word == 'a') state = 1;
				else state = 5;
				break;
			case 1 :
				if(word == 'a') state = 2;
				else if(word == 'b') state = 3;
				else state = 5;
				break;
			case 2 :
				if(word == 'a') state = 2;
				else state = 5;
				break;
			case 3 : 
				if(word == 'a') state = 4;
				else if(word == 'b') state = 5;
				else state = 5;
				break;
			case 4 : 
				if(word == 'b') state = 3;
				else state = 5;
				break;
			case 5 : break;
			default : break;
		}
	}
	
}

// DFA model for problem 2, its language is {#, $, a, b, x, y, 0-9}
class DFA_Model_B extends DFA_Models{

	@Override
	public void transition(char word) {
		switch(state) {
			case 0 :
				if(word == '#') state = 11;
				else if(word == '$') state = 21;
				else state = 4;
				break;
			case 11 :
				if(word == 'a' || word == 'b') state = 12;
				else state = 4;
				break;
			case 12:
				if(IntegerSet.contains(""+word)) state = 3;
				else state = 4;
				break;
			case 21 :
				if(word == 'x' || word == 'y') state = 22;
				else state = 4;
				break;
			case 22:
				if(IntegerSet.contains(""+word)) state = 3;
				else state = 4;
				break;
			case 3 :  // accept state
				if(IntegerSet.contains(""+word)) state = 3;
				else state = 4;
				break;
			case 4 : break;
			default : break;
		}
	}
	
}


// DFA model for problem 3, its language is {s, t, u, x, y}
class DFA_Model_C extends DFA_Models{

	@Override
	public void transition(char word) {
		switch(state) {
			case 0 :
				if(word == 's') state = 1;
				else if(word == 'x') state = 4;
				else state = 8;
				break;
			case 1 :
				if(word == 't') state = 2;
				else if(word == 'u') state = 3;
				else state = 8;
				break;
			case 2 :
				if(word == 't') state = 2;
				else if(word == 'u') state = 3;
				else state = 8;
				break;
			case 3 : // accept state 
				state = 8;
				break;
			case 4 : 
				if(word == 'y') state = 5;
				else if(word == 'z') state = 7;
				else state = 8;
				break;
			case 5 :
				if(word == 's') state = 1;
				else if(word == 'x') state = 6;
				else state = 8;
				break;
			case 6:
				if(word == 'y') state = 5;
				else if(word == 'z') state = 7;
				else state = 8;
				break;
			case 7: // accept state
				state = 8;
				break;
			case 8:
				break;
			default : break;
		}
	}
	
}


	
}



