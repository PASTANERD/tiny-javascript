import java.util.ArrayList;
import java.util.Scanner;


public class DFAcombined{
	
	
	//private static DFAcombined model;
	private DFA_Models scanner;
	private String buffer = "";
	//private Scanner reader = new Scanner(System.in);
	private String checkString;
	//private ArrayList<String> quitOptions;
	private ArrayList<Integer> digit;
	private ArrayList<String> letter;
	char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private ArrayList<String> keyword_id;
	private ArrayList<String> keyword_logic_value;
	private ArrayList<String> keyword_function_name;
	private ArrayList<String> keyword_tag_name;

	private ArrayList<String> operator_logic;
	private ArrayList<String> operator_arithmetic;
	private ArrayList<String> operator_assignment;
	private ArrayList<String> special_character;

	private ArrayList<Integer> DFA_A_AcceptStates;
	private ArrayList<Integer> DFA_B_AcceptStates;
	private ArrayList<Integer> DFA_C_AcceptStates;
	private char[] input_stream;
	private boolean epsilon = false;

	
	public DFAcombined() {
		
		init(); // to make sure initial conditions which are AcceptStates for DFAs, Exit conditions and etc.
		//run(); // run DFA machine
		
	}
	
	/* method run() is in charge of taking sentences. */
	/*
	private void scanning(String string) {
		//print("Type E or e to exit");
		//while(true) {
			//print("Input: ");
			string_reader();
			if(checkString == null) {	
				print("Program is terminated. Thank you");
				break;
			}
			else {
				if(checkString.length() < 1) {
					epsilon = true;
				} else epsilon = false;
				scan_generator(input_stream);
		//	}
		}
	}
	
*/

	/* method string_reader() assigns received strings into String and character array to */
	public void string_reader(String string) {
		//checkString = string;
		input_stream = string.toCharArray();
/*
		if(checkString == null) {	
			print("Program is terminated. Thank you");
		}
*/
		
		if(checkString.length() < 1) {
				epsilon = true;
			} else epsilon = false;
			scan_generator(input_stream);
		
	}
	
	/* method scan_generator() is to determine which DFA model is needed.*/
	private void scan_generator(char[] string) {
		/*
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
		*/
		scanner = new Identifier();
		scanner.set_model(0, DFA_A_AcceptStates, string);
		scanner.scanning();
		
	}
	private void print(String message){
        System.out.println(message);
    }
	
	/* method init() is to set initial conditions. */
	
	private void init() {
		//quitOptions = new ArrayList<String>(); // quitOption is set of special words which contains e, E, exit, Exit, EXIT that makes this program terminates. 
		//IntegerSet = new ArrayList<String>(); // IntegerSet is set of integers in String, from 0 to 9, to make comparing more easy
		digit = new ArrayList<Integer>();
		letter = new ArrayList<String>();
		keyword_id = new ArrayList<String>();
		keyword_logic_value = new ArrayList<String>();
		keyword_function_name = new ArrayList<String>();
		keyword_tag_name = new ArrayList<String>();
		operator_logic = new ArrayList<String>();
		operator_arithmetic = new ArrayList<String>();
		operator_assignment = new ArrayList<String>();
		special_character = new ArrayList<String>();
		
		DFA_A_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model A which is related to problem 1
		//DFA_B_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model B which is related to problem 2
		//DFA_C_AcceptStates = new ArrayList<Integer>(); // this is a set of AccpetStates in DFA model C which is related to problem 3
		
		buffer = "";

		for(int i = 0 ; i < 10 ; i++){
			digit.add(i);
		}

		for(int i = 0 ; i < alphabet.length ; i++){
			letter.add(""+alphabet[i]);
		}

		keyword_id.add("var");
		keyword_id.add("do");
		keyword_id.add("while");
		keyword_id.add("if");
		keyword_id.add("else");
		keyword_id.add("switch");
		keyword_id.add("case");
		keyword_id.add("break");
		keyword_id.add("default");
		keyword_id.add("function");

		keyword_logic_value.add("true");
		keyword_logic_value.add("false");

		keyword_function_name.add("window");
		keyword_function_name.add("window.prompt");
		keyword_function_name.add("parseFloat");
		keyword_function_name.add("document");
		keyword_function_name.add("document.write");
		keyword_function_name.add("document.writeIn");

		keyword_tag_name.add("script_start");
		keyword_tag_name.add("script_end");
		keyword_tag_name.add("h1");
		keyword_tag_name.add("/h1");
		keyword_tag_name.add("h2");
		keyword_tag_name.add("/h2");
		keyword_tag_name.add("h3");
		keyword_tag_name.add("/h3");
		keyword_tag_name.add("ul");
		keyword_tag_name.add("/ul");
		keyword_tag_name.add("ol");
		keyword_tag_name.add("/ol");
		keyword_tag_name.add("br/");

		operator_logic.add("==");
		operator_logic.add("<=");
		operator_logic.add(">=");
		operator_logic.add("<");
		operator_logic.add(">");

		operator_arithmetic.add("+");
		operator_arithmetic.add("-");
		operator_arithmetic.add("*");
		operator_arithmetic.add("/");
		operator_arithmetic.add("++");
		operator_arithmetic.add("--");

		operator_assignment.add("=");
		operator_assignment.add("+=");
		operator_assignment.add("-=");
		
		special_character.add(".");
		special_character.add(",");
		special_character.add(";");
		special_character.add(":");


		DFA_A_AcceptStates.add(0);
		DFA_A_AcceptStates.add(1);
		DFA_A_AcceptStates.add(2);
		//DFA_B_AcceptStates.add(3);
		//DFA_C_AcceptStates.add(3);
		//DFA_C_AcceptStates.add(7);
		
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
	
	private void scanning(){
		int IC = 0; // index counter
		int string_length = stream.length;
		
		while(IC < string_length){
			transition(stream[IC]);
			buffer = buffer + stream[IC];
			IC++;
		}
		
		if(acceptState.contains(state)) {
			print(buffer+ "\t" +keyword_check(buffer));
		}
		else print("Rejected");
	}
	
	private String keyword_check(String buffer){
		if(keyword_id.contains(buffer)) return "keyword logical value";
		else if(keyword_function_name.contains(buffer)) return "keyword function name" ;
		else if(keyword_tag_name.contains(buffer)) return "keyword tag name" ;
		else if(keyword_logic_value.contains(buffer)) return "keyword logical value" ;
		else if(operator_arithmetic.contains(buffer)){
			switch(buffer){
				case "++":
					return "increment operator";
				case "--":
					return "decrement operator";
				case "+":
					return "addition operator";
				case "-":
					return "subtraction operator";
				case "/":
					return "division operator";
				case "*":
					return "multiplication operator";
				default:
					break;
				
			}
		}
		/*
		switch(buffer){
			case keyword_id.contains(buffer):
				return "keyword logical value";
				break;
			case keyword_function_name.contains(buffer):
				return "keyword function name" ;
				break;
			case keyword_tag_name.contains(buffer):
				return "keyword tag name" ;
				break;
			case keyword_tag_name.contains(buffer):
				return "keyword tag name";
				break;
			case operator_arithmetic.contains(buffer):
				if(buffer.equals("++")) return "increment operator";
				break;
			default:
				break;
		}
		*/
		/*
		if() return "keyword id"
		else if(keyword_logic_value.contain.(buffer)) return 
		else if(
		else if(
		else if(
		*/
		return "something wrong.";
	}
	public abstract void transition(char word);
	
}

// DFA model for problem 1, its language is {a, b}
class Identifier extends DFA_Models{

	@Override
	public void transition(char value) {
		String word = ""+value;
		switch(state) {
			case 0 :
				if(letter.contains(word)) state = 1;
				else state = 2;
				break;
			case 1 :
				if(letter.contains(word) || digit.contains(word.toString())) state = 1;
				else state = 2;
				break;
			case 2 : break;
			default : break;
		}
	}
	
}
/*
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
	
}*/

	
}



