import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LL1 {
	static String path;
	private static final String static_path = "../21400646_LimChaeEon_HW4/java/test/basic2.jss";  //21400646_LimChaeEon_HW3/
	Parser parser;
	Scanner machine;
    BufferedReader reader;
	List<Token> TokenTable;
	//Iterator<Token> itr;
    
    public LL1() {}
	
	public LL1(String argument) {
		this.path = argument;
	}
	

	private void init() {
		print("Initializing Process..");
		machine = new Scanner();
		TokenTable = new ArrayList<Token>();
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
            print(path);
            print("file loading error.");
			e.printStackTrace();
		}
		machine.fileReader();
		//for(int i = 0 ; i < TokenTable.size(); i++) print(i+") \t " + TokenTable.get(i).toString());
		parser = new Parser();
	}

	public static void main(String[] args) {
        String argument;
        if(args.length == 0) argument = static_path;
        else argument = args[0];
        LL1 module = new LL1(argument);
        module.init();
        // List<Token> tokenTable = new List<Token>();
    }
	
	private static void print(String message) {
		System.out.println(message);
	}
	
	
	
	class Scanner{
		DFA dfa;
		int lineno = 0;
		private char[] digit = "0123456789".toCharArray();
		private char[] letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_.".toCharArray();
		private char[] specials = "!@#$%^&*()-=+[]{}|\\;:\",/<>?\'".toCharArray();
	

	    public Scanner() {
			dfa = new DFA();
		}

		private boolean fileReader(){
			boolean flag;
	        try {
	        	while(true) {
	        		String buffer = reader.readLine();
	                if(buffer != null) {
		        		lineno++;
	                	dfa.lexer(buffer.toCharArray());
	                	flag = true;
	                }
	                else {
	                	dfa.lexer("$".toCharArray());
	                	flag = false;
	                	break;
	                }
	        	}
	                
	                //print(buffer);
	                //string_to_char(buffer);
	        } catch (IOException e) {
	            print("file reading error.");
	            flag = false;
	        }
	        return flag;
	        
	    }
		
		private boolean stringReader(String input_string) {
			boolean flag;
			if(input_string.isEmpty()) flag = false;
			else {
				dfa.lexer(input_string.toCharArray());
				flag = true;
			}
			return flag;
			
		}
		
		class DFA{
			
			private int state;
			private int index;
			private Token token = new Token();
			private TokenType tokenType = TokenType.UNDEFINED;
			private String stream = "";
	
			public void lexer(char[] input){
				resetToken();
				if(input.length != 0){
					for(index = 0 ; index < input.length ; index++){
						state = transition(input[index], state);
						if(state == 99){
							generateToken();
							index--;
							resetToken();
						}
					}
					generateToken();

				}
				
			}
			
			private void resetToken() {
				tokenType = TokenType.UNDEFINED;
				token.setToken(tokenType);
				token.setName("");
				token.setLiteral("");
				token.setNumber(Integer.MAX_VALUE);
				stream = "";
				state = 0;
			}
	
			private int transition(char character, int current_state){
				switch (current_state) {
					case 0:
						if(isWhiteSpace(""+character)) {
							return 0;
						}
						
						else if(isContained(character, digit)) {
							stream = stream + character;
							tokenType = TokenType.NUMBER;
							return 1;
						}
						else if(isContained(character, letter)) {
							stream = stream + character;
							return 2;
						}
						else if (isContained(character, specials)){
							stream = stream + character;
							switch (character) {
								case '+':
									return 3;
								case '-':
									return 6;
								case '*':
									return 9;
								case '/':
									return 10;
								case ':':
									tokenType = TokenType.COLON;
									return 12;
								case ';':
									tokenType = TokenType.SEMICOLON;
									return 13;
								case ',':
									return 14;
								case '(':
									return 15;
								case '<':
									return 18;
								case '>':
									return 22;
								case '{':
									tokenType = TokenType.BRACKET_LEFT;
									return 24;
								case '}':
									tokenType = TokenType.BRACKET_RIGHT;
									return 25;
								case '\"':
									return 26;
								case '=':
									return 29;
								case '$':
									return 0;
								default:
									return 0;
							}
						}
						else return 0;
					case 1:
						if(isContained(character, digit)){   
							stream = stream + character; 
							return 1;
						}
						else {
							return 99;
						}
					case 2:
						if(isContained(character, letter) || isContained(character, digit)) {
							tokenType = TokenType.IDENTIFIER;
							stream = stream + character;
							return 2;
						}
						else {
							return 99;
						}
					case 3:
						if(character == '+') {
							stream = stream + character;
							return 4;  
						}
						else if(character == '=') {
							stream = stream + character;
							return 5;
						}
						else {
							tokenType = TokenType.ADD;
							return 99;
						}
					case 4:
						tokenType = TokenType.INCREMENT;
						return 99;
					case 5:
						tokenType = TokenType.ADDASSIGN;
						return 99;
					case 6:
						if(character == '-') {
							stream = stream + character; 
							return 7;
						}
						else if(character == '=') {
							stream = stream + character;
							return 8;
						}
						else {
							tokenType = TokenType.SUBT;
							return 99;
						}
					case 7:
						tokenType = TokenType.DECREMENT;
						return 99;
					case 8:
						tokenType = TokenType.SUBTASSIGN;
						return 99;
					case 9:
						tokenType = TokenType.MULTI;
						return 99;
					case 10:
						if(character == '/') {
							stream = stream + character;
							return 11;
						}
						else {
							tokenType = TokenType.DIVIDE;
							return 99;
						}
					case 11:
						stream = stream + character;
						tokenType = TokenType.COMMENT;
						return 11;
					case 12:
						return 99;
					case 13:
						
						return 99;
					case 14:
						tokenType = TokenType.COMMA;
						return 99;
					case 15:
						stream = stream + character;
						if(character == ')'){
							tokenType = TokenType.FUNCTION_PARAMETER;
							return 17;
						}
						return 16;
					case 16:
						stream = stream + character;
						if(character == ')'){
							tokenType = TokenType.FUNCTION_PARAMETER;
							return 17;
						}
						else {
							return 16;
						}
					case 17:
						return 99;
					case 18:
						
						if(character == '='){
							stream = stream + character;
							return 19;
						} 
						else if(isContained(character, letter) || character == '/') {
							stream = stream + character;
							return 20;
						}
						else{
							tokenType = TokenType.LESS;
							return 99;
						}
					case 19:
						tokenType = TokenType.LESS_EQ;
						return 99;
					case 20:
						if(isContained(character, letter)) {
							stream = stream + character;
							return 20;
						}
						else if(character == '>') {
							tokenType = TokenType.IDENTIFIER;
							stream = stream + character;
							//tokenType = new TokenType();
							return 21;
						}
						return 99;
					case 21:
						return 99;
					case 22:
						if(character == '='){
							stream = stream + character;
							return 23;
						}
						else{
							tokenType = TokenType.GREATER;
							return 99;
						}
					case 23:
						tokenType = TokenType.GREATER_EQ;
						return 99;
					case 24:
						
						return 99;
					case 25:
						
						return 99;
					case 26:
						stream = stream + character;
						return 27;
					case 27:
						stream = stream + character;
						if(character == '\"'){
							return 28;
						}
						else return 27;
					case 28:
						tokenType = TokenType.LITERAL;
						return 99;
					case 29:
						if(character == '='){
							stream = stream + character;
							return 30;
						}
						else{
							tokenType = TokenType.ASSIGN;
							return 99;
						}
					case 30:
						tokenType = TokenType.EQ;
						return 99;
					case 99:
						return 0;
					case 100:
						print("something is error. state = 100");
						return 0;
					default:
						return 0;
				}
			}
			
			private void generateToken() {
				Token token = new Token();
				token.setLineno(lineno);
				switch(tokenType) {
					case IDENTIFIER:
						// keyword id case
						//int i = 0;
						for(TokenType temp : TokenType.values()) {
							if(stream.equals(temp.getWord())) {
								tokenType = temp;
								token.setToken(tokenType);
								token.setName(stream);
								break;
							}
							//i++;
						}
						// user defined identifier case
						if(tokenType.equals(TokenType.IDENTIFIER)) {
							token.setToken(tokenType);
							token.setName(stream);
						}
						break;
					case NUMBER:
						token.setToken(tokenType);
						token.setNumber(Integer.parseInt(stream));
						break;
					case COMMENT:
						token.setToken(tokenType);
						token.setLiteral(stream);
						break;
					case LITERAL:
						token.setToken(tokenType);
						token.setLiteral(stream);
						break;
					default:
						token.setToken(tokenType);
						token.setName(stream);
						break;
						// common reserved word case 		
				}

				if(!token.getToken().equals(TokenType.UNDEFINED)) TokenTable.add(token);
				
				
			}
	
			
			private void token_print(){
				print(stream +"\t"+ token.getTokenTag());
			}
			
			private boolean isContained(char target, char[] set){
				for(int i = 0; i < set.length ; i++){
					if(set[i] == target) return true;
				}
				return false;
			}
			
			
			private boolean isWhiteSpace(String target) {
				for(WhiteSpace sample : WhiteSpace.values()) {
					if(target.equals(sample.getWord())) return true;
				}
				return false;
			}
			
			private boolean isNextLine(String target) {
				for(NextLine sample : NextLine.values()) {
					if(target.equals(sample.getWord())) return true;
				}
				return false;
			}
		}

	    
	}
	
	class Token {
		private TokenType token;
		private String name;
		private String literal;
		private int number;
		private int lineno;
		
		public Token() {
		}
		public Token(TokenType token) {
			this.token = token;
		}
		public TokenType getToken() {
			return token;
		}

		public void setToken(TokenType token) {
			this.token = token;
		}

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		public String getLiteral() {
			return literal;
		}
		
		public void setLiteral(String literal) {
			this.literal = literal;
		}
		
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}

		public String getTokenTag() {
			return token.getTag();
		}
		
		public int getLineno() {
			return lineno;
		}
		public void setLineno(int lineno) {
			this.lineno = lineno;
		}
		@Override
		public String toString() {
			return "{ " +
					"TokenType : " + token.name() +
					"\t\tTokenTag : " + getTokenTag() +
					"\tTokenName : " + getName() +
					"\tLiteral data : " + getLiteral() +
					"\tNumeric data : " + getNumber()+
					" }";
			
		}
		
	}
	
	enum TokenType{
		
		UNDEFINED(" ","this token is defined yet"),

		IDENTIFIER("","user_defined_identifier"),
		LITERAL("", "literal"),
		NUMBER("", "number"),
		COMMENT("", "comment"),
		FUNCTION_PARAMETER("", "function parameter"),
		
		ADD("+","addition operator"),
		SUBT("-", "subtraction operator"), 
		MULTI("*", "multiplication operator"),
		DIVIDE("/", "devision operator"),
		INCREMENT("++", "increment operator"), 
		DECREMENT("--", "decrement operator"),
		
		ASSIGN("=", "assignment operator"), 
		ADDASSIGN("+=", "additive assingment operator"),
		SUBTASSIGN("-=", "subtractive assignemnt operator"),
		
		EQ("==", "equality logic operator"), 
		NOT_EQ("!=", "not equality logic operator"),
		LESS_EQ("<=", "equal to or smaller than logic operator"), 
		GREATER_EQ(">=", "equal to or greater than logic operator"),
		LESS("<", "less than logic operator"),
		GREATER(">", "greater than logic operator"),
		
		COLON(":", "colon"), 
		SEMICOLON(";", "semicolon"),
		COMMA(",", "punctuation character"),
		BRACKET_LEFT("{", "left curly bracket"),
		BRACKET_RIGHT("}", "right curly bracket"),
		PARENTHESES_OPEN("(", "parentheses open"),
		PARENTHESES_CLOSE(")","parentheses close"),	
		WHITE_SPACE(" "),
		
		
		VAR("var", "keyword id"), 
		WHILE("while", "keyword id"), 
		IF("if", "keyword id"), 
		ELSE("else", "keyword id"),
		FOR("for", "keyword id"),
		SWITCH("switch", "keyword id"),
		CASE("case", "keyword id"), 
		DEFAULT("default", "keyword id"), 
		FALSE("false", "keyword id"), 
		TRUE("true", "keyword id"), 
		DO("do", "keyword id"), 
		FUNCITON("function", "keyword id"), 
		RETURN("return", "keyword id"),
		BREAK("break", "keyword id"),
		
		WINDOW("window", "keyword function name"), 
		PARSFLOAT("parseFloat", "keyword function name"),
		PROMPT("window.prompt", "keyword function name"), 
		DOCUMENT("document", "keyword function name"), 
		WRITELN("document.writeln", "keyword function name"), 
		WRITE("document.write", "keyword function name"),
		
		H1("<h1>", "keyword tag name"), 
		H1_END("</h1>", "keyword ending tag name"),
		H2("<h2>", "keyword tag name"),
		H2_END("</h2>", "keyword ending tag name"),
		H3("<h3>", "keyword tag name"),
		H3_END("</h3>", "keyword ending tag name"),
		BR("<br/>", "keyword tag name"), 
		UL("<ul>", "keyword tag name"),
		UL_END("</ul>", "keyword ending tag name"),
		OL("<ol>", "keyword tag name"),
		OL_END("</ol>", "keyword ending tag name"),
		SCRIPT_START("<script_start>", "keyword tag name"),
		SCRIPT_END("<script_end>", "keyword tag name"),
		
		END_OF_FILE("$", "end of file"),
		
		ENUM_END;
		
		private String word;
		private String tag; 
		
		TokenType(){
			
		}
	
		TokenType(String string){
			this.word = string;
		}
		
		TokenType(String string, String tag){
			this.word = string;
			this.tag = tag;
		}

		String getWord() {
			return word;
		}
		
		String getTag() {
			return tag;
		}
		
		static boolean isMulOp(TokenType target) {
			switch(target) {
			case MULTI:
				return true;
			case DIVIDE:
				return true;
			 default:
				 return false;
			}
		}
		
		static boolean isAddOp(TokenType target) {
			switch(target) {
			case ADD:
				return true;
			case SUBT:
				return true;
				default:
					return false;
			}
		}
		
		static boolean isLogicOperator(TokenType target) {
			switch(target) {
				case EQ:
					return true;
				case LESS_EQ:
					return true;
				case GREATER_EQ:
					return true;
				case LESS:
					return true;
				case GREATER:
					return true;
				default:
					return false;
			}
			
		}
	}
	
	enum WhiteSpace{

		WHITE_SPACE(" "),
		TAB("\t");
		String word;
		WhiteSpace(String string){
			this.word = string;
		}
		String getWord() {
			return word;
		}
		
		
	}
	
	enum NextLine{
		NEXT_LINE("\n"),
		CARRIAGE_RETURN("\r");
		
		String word;
		NextLine(String string){
			this.word = string;
		}
		String getWord() {
			return word;
		}
		
	}

	
	class Parser {
		int lineno = 0;
		int start_index = 0;
		Parser(){
			init_parsing();
			parsing();
		}
		
		void init_parsing() {
			EBNF.itr = TokenTable.iterator();

			//itrTokenTable = TokenTable.iterator();
			if(TokenTable.isEmpty())
				try {
					throw new ParsingExceptionHandler("No Element");
				} catch (ParsingExceptionHandler e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			while(EBNF.itr.hasNext()) {
				if(EBNF.itr.next().getToken().equals(TokenType.SCRIPT_START)) break;
				start_index++;
			}
			if(start_index >= TokenTable.size())
				try {
					throw new ParsingExceptionHandler("No <script_start> tag");
				} catch (ParsingExceptionHandler e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			EBNF.itr = TokenTable.iterator();
		}
		
		void parsing() {
			try {
				EBNF.PROGRAM.parse(TokenTable, start_index);
				print("File : " + path + " parsing OK");
			} catch (ParsingExceptionHandler e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	enum EBNF{
		
		PROGRAM{
			/*
			 *		PROGRAM
			 *	Program		→ 	<script_start> stmt-sequence <script_end>
			 */
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				
				Token startToken = TokenList.get(index); //set script_start at first			
				int i;
				int max = TokenList.size();
				for(i = index; i < max ; i++){
					if(matching(TokenList.get(i), TokenType.SCRIPT_END)) {
						STMT_SEQUENCE.parse(TokenList, index+1);
						break;
					}
				}
				if( i >= max )throw new ParsingExceptionHandler(startToken, "No pairs for script start sign");
			}
			
		},
		
		STMT_SEQUENCE{
			/*		STMT_SEQUENCE
			 * stmt-sequence	→ 	statement [ ; stmt-sequence ]
			 * 
			 */
			
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				Token thisToken = TokenList.get(index);
				//System.out.println("index : "+index+"\t->\t" + thisToken.toString()  );
				switch(thisToken.getToken()) {
				case IF:
					STATEMENT.parse(TokenList, index);
					break;
				case VAR:
					STATEMENT.parse(TokenList, index);
					break;
				case FOR:
					STATEMENT.parse(TokenList, index);
					break;
				case WHILE:
					STATEMENT.parse(TokenList, index);
					break;
				case IDENTIFIER:
					STATEMENT.parse(TokenList, index);
					break;
				case INCREMENT:
					STATEMENT.parse(TokenList, index);
					break;
				case DECREMENT:
					STATEMENT.parse(TokenList, index);
					break;
				case SWITCH:
					STATEMENT.parse(TokenList, index);
					break;
				case COMMENT:
					COMMENT.parse(TokenList, index);
					break;
				case WINDOW:
					STATEMENT.parse(TokenList, index);
					break;
				case DOCUMENT:
					STATEMENT.parse(TokenList, index);
					break;
				case WRITE:
					STATEMENT.parse(TokenList, index);
					break;
				case WRITELN:
					STATEMENT.parse(TokenList, index);
					break;
				case PARSFLOAT:
					STATEMENT.parse(TokenList, index);
					break;
				case PROMPT:
					STATEMENT.parse(TokenList, index);
					break;
				case BREAK:
					STATEMENT.parse(TokenList, index);
					break;
				case BRACKET_RIGHT:
					break;
				case SCRIPT_END:
					break;
				case CASE:
					break;
				default:
					break;
				}
				int max = TokenList.size();
				
				for(int i = index+1; i < max ; i++){
					if(matching(TokenList.get(i), TokenType.SEMICOLON)) {
						//System.out.println("\tSearching SEMICOLON");
						//System.out.println("\ti : "+i+"\t->\t" + TokenList.get(i).toString()  );
						STMT_SEQUENCE.parse(TokenList, i+1);
						break;
					}
				}

				
			}
			
		},
		
		STATEMENT{
			/* STATEMENT
			 * statement 		→ 	if-stmt | declaration-stmt | loop-stmt | assign-stmt
			 *						| function-stmt | switch-stmt | increment-stmt  
			 *						| comment | break
			 */
			
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()) {
					case IF:
						IF_STMT.parse(TokenList, index);
						break;
					case VAR:
						DECLARATION_STMT.parse(TokenList, index);
						break;
					case FOR:
						LOOP_STMT.parse(TokenList, index);
						break;
					case WHILE:
						LOOP_STMT.parse(TokenList, index);
						break;
					case IDENTIFIER:
						ASSIGN_STMT.parse(TokenList, index);
						break;
					case INCREMENT:
						INCREMENT_STMT.parse(TokenList, index);
						break;
					case DECREMENT:
						INCREMENT_STMT.parse(TokenList, index);
						break;
					case SWITCH:
						SWITCH_STMT.parse(TokenList, index);
						break;
					case WINDOW:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case DOCUMENT:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case WRITE:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case WRITELN:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case PARSFLOAT:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case PROMPT:
						FUNCTION_STMT.parse(TokenList, index);
						break;
					case BREAK:
						break;
					default:
						throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
		},
		
		SIMPLE_STMT{
			/*
			 *  simple-stmt		→ 	statement ; | { stmt-sequence }
			 */

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub

				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.BRACKET_LEFT)) {
					int i;
					int max = TokenList.size();
					for(i = index; i < max ; i++){
						if(matching(TokenList.get(i), TokenType.BRACKET_RIGHT)) {
							STMT_SEQUENCE.parse(TokenList, ++i);
							STMT_SEQUENCE.parse(TokenList, index+1);
							
							break;
						}
					}
					if( i >= max )throw new ParsingExceptionHandler(thisToken, "No pairs for this curly bracket");
					
				}
				else {
					switch(thisToken.getToken()) {
					case IF:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case VAR:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case FOR:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case WHILE:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case IDENTIFIER:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case INCREMENT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case DECREMENT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case SWITCH:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case COMMENT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case WINDOW:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case DOCUMENT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case WRITE:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case WRITELN:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case PARSFLOAT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case PROMPT:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case BREAK:
						STMT_SEQUENCE.parse(TokenList, index);
						break;
					case BRACKET_RIGHT:
						break;
					default:
						throw new ParsingExceptionHandler(thisToken, " for "+this.name());
					}
					//STMT_SEQUENCE.parse(TokenList, index);
				}
				
			}
			
		},
		
		IF_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.IF)) {
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					SIMPLE_STMT.parse(TokenList, index+2);
					int max = TokenList.size();
					for(int i = index; i < max ; i++){
						if(matching(TokenList.get(i), TokenType.ELSE)) {
							ELSE_PART.parse(TokenList, i);
							break;
						}
					}
				}
				else {
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
				}
			}
			
		},
		
		ELSE_PART{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.ELSE)) {
					SIMPLE_STMT.parse(TokenList, index+1);
					
				}else if(matching(thisToken, TokenType.SEMICOLON)) {
					
				} else {
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
				}
				
			}
			
		},
		
		FUNCTION_PARAMETER{
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				//Token thisToken = TokenList.get(index);
				//LL1.Scanner.stringReader(TokenList.get(index).getName());
			}
			
		},
		
		PARAMETER_PART{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				
			}
			
		},
		
		CONDITION_PART{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				
			}
			
		},
		
		EXP{
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.IDENTIFIER) || matching(thisToken, TokenType.NUMBER) || matching(thisToken, TokenType.PARENTHESES_OPEN) || matching(thisToken, TokenType.FUNCTION_PARAMETER)) {
					SIMPLE_EXP.parse(TokenList, index);
					LOGIC_EXP.parse(TokenList, index+1);
				}else throw new ParsingExceptionHandler(thisToken, " for "+this.name());
			}
			
		},
		
		LOGIC_EXP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()) {
				case SEMICOLON:
					break;
				case PARENTHESES_CLOSE:
					break;
				case GREATER:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case LESS:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case GREATER_EQ:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case LESS_EQ:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case EQ:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case NOT_EQ:
					LOGIC_OP.parse(TokenList, index);
					SIMPLE_EXP.parse(TokenList, index+1);
					break;
				case COMMA:
					break;
				default:
					
				}
				
			}
			
		},

		
		LOGIC_OP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case EQ:
					break;
				case GREATER:
					break;
				case LESS:
					break;
				case LESS_EQ:
					break;
				case GREATER_EQ:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		SIMPLE_EXP{
			
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				
				Token thisToken = TokenList.get(index);
				//SIMPLE_EXP.parse(TokenList, index);
				if(matching(thisToken, TokenType.IDENTIFIER) || matching(thisToken, TokenType.NUMBER) || matching(thisToken, TokenType.PARENTHESES_OPEN) || matching(thisToken, TokenType.FUNCTION_PARAMETER)) {
					TERM.parse(TokenList, index);
					SIMPLE_EXP2.parse(TokenList, index+1);
				}else throw new ParsingExceptionHandler(thisToken, " for "+this.name());
				
			}
			
		},
		
		SIMPLE_EXP2{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()) {
				case SEMICOLON:
					break;
				case PARENTHESES_CLOSE:
					break;
				case GREATER:
					break;
				case LESS:
					break;
				case GREATER_EQ:
					break;
				case LESS_EQ:
					break;
				case EQ:
					break;
				case NOT_EQ:
					break;
				case ADD:
					ADD_OP.parse(TokenList, index);
					TERM.parse(TokenList, index+1);
					SIMPLE_EXP2.parse(TokenList, index+2);
					break;
				case SUBT:
					ADD_OP.parse(TokenList, index);
					TERM.parse(TokenList, index+1);
					SIMPLE_EXP2.parse(TokenList, index+2);
					break;
				case COMMA:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
				}
			}
		
		},
		
		ADD_OP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case ADD:
					break;
				case SUBT:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		TERM{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				//SIMPLE_EXP.parse(TokenList, index);
				if(matching(thisToken, TokenType.IDENTIFIER) || matching(thisToken, TokenType.NUMBER) || matching(thisToken, TokenType.PARENTHESES_OPEN) || matching(thisToken, TokenType.FUNCTION_PARAMETER)) {
					FACTOR.parse(TokenList, index);
					TERM2.parse(TokenList, index+1);
				}else throw new ParsingExceptionHandler(thisToken, " for "+this.name());
				
			}
			
		},
		
		TERM2{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case SEMICOLON:
					break;
				case PARENTHESES_CLOSE:
					break;
				case GREATER:
					break;
				case LESS:
					break;
				case GREATER_EQ:
					break;
				case LESS_EQ:
					break;
				case EQ:
					break;
				case NOT_EQ:
					break;
				case ADD:
					break;
				case SUBT:
					break;
				case MULTI:
					MUL_OP.parse(TokenList, index);
					FACTOR.parse(TokenList, index+1);
					TERM2.parse(TokenList, index+2);
					break;
				case DIVIDE:
					MUL_OP.parse(TokenList, index);
					FACTOR.parse(TokenList, index+1);
					TERM2.parse(TokenList, index+2);
					break;
				case COMMA:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		MUL_OP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case MULTI:
					break;
				case DIVIDE:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		FACTOR{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case NUMBER:
					break;
				case IDENTIFIER:
					break;
				case FUNCTION_PARAMETER:
					break;
				case PARENTHESES_OPEN:
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		LOOP_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.FOR)) {
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					SIMPLE_STMT.parse(TokenList, index+2);
				} else if(matching(thisToken, TokenType.WHILE) ) {
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					SIMPLE_STMT.parse(TokenList, index+2);
				} else new ParsingExceptionHandler(thisToken, " for "+this.name());
				
			}
			
		},
		
		FUNCTION_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case PROMPT:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					break;
				case WINDOW:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					break;
				case PARSFLOAT:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					break;
				case WRITELN:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					break;
				case WRITE:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1);
					break;
				case DOCUMENT:
					FUNCTION_KEYWORD.parse(TokenList, index);
					FUNCTION_PARAMETER.parse(TokenList, index+1); 
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());						
				}
				
			}
			
		},
		
		FUNCTION_KEYWORD{
			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case PROMPT:
					break;
				case WINDOW:
					break;
				case PARSFLOAT:
					break;
				case WRITELN:
					break;
				case WRITE:
					break;
				case DOCUMENT:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());						
				}
				
			}
			
		},
		
		INCREMENT_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);

				switch(thisToken.getToken()){
				case IDENTIFIER:
					INCREMENT_OP.parse(TokenList, index+1);
					break;
				case INCREMENT:
					INCREMENT_OP.parse(TokenList, index);
					break;
				case DECREMENT:
					INCREMENT_OP.parse(TokenList, index);
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
					
			}
			
		},
		
		INCREMENT_OP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case INCREMENT:
					break;
				case DECREMENT:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		SWITCH_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				int max = TokenList.size();
				int i;
				Token lbracketToken = TokenList.get(index+2);
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.SWITCH)) {
					if(matching(lbracketToken, TokenType.BRACKET_LEFT)) {

						for(i = index+3; i < max ; i++){
							if(matching(TokenList.get(i), TokenType.BRACKET_RIGHT)) {
								FUNCTION_PARAMETER.parse(TokenList, index+1);
								CASE_PART.parse(TokenList, index+3);
								break;
							}
						}
						for(i = index+3; i < max ; i++){
							if(matching(TokenList.get(i), TokenType.DEFAULT)) {
								DEFAULT_BLOCK.parse(TokenList, i);
								break;
							}
						}
						if(i >= max) throw new ParsingExceptionHandler(lbracketToken, " for "+this.name());
				}else throw new ParsingExceptionHandler(lbracketToken, " for "+this.name() );
				}else {
					throw new ParsingExceptionHandler(thisToken, " for "+this.name() );
				}
			}
			
		},
		
		CASE_PART{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token caseToken = TokenList.get(index);
				int max = TokenList.size();
				if(matching(caseToken, TokenType.CASE)) {
					CASE_BLOCK.parse(TokenList, index);
					
					for(int i = index+1; i < max ; i++){
						if(matching(TokenList.get(i), TokenType.CASE)) {
							CASE_PART.parse(TokenList, i);
							break;
						}
					}
				}else if(matching(caseToken, TokenType.DEFAULT)) {
					
				}else throw new ParsingExceptionHandler(caseToken, " for "+this.name());
			}
			
		},
		
		CASE_BLOCK{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token caseToken = TokenList.get(index);
				Token colonToken = TokenList.get(index+2);
				if(matching(caseToken, TokenType.CASE)) {
					if(matching(colonToken, TokenType.COLON)) {
						CASE_CONDITION.parse(TokenList, index);
						STMT_SEQUENCE.parse(TokenList, index+3);
					}else throw new ParsingExceptionHandler(colonToken, " for "+this.name());
				}else throw new ParsingExceptionHandler(caseToken, " for "+this.name());
			}
			
		},
		
		CASE_CONDITION{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token caseToken = TokenList.get(index);
				if(matching(caseToken, TokenType.CASE)) {
					CASE_PARAMETER.parse(TokenList, index+1);
				}else throw new ParsingExceptionHandler(caseToken, " for "+this.name());
			}
			
		},
		
		CASE_PARAMETER{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case NUMBER:
					break;
				case LITERAL:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		DEFAULT_BLOCK{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token defaultToken = TokenList.get(index);
				Token colonToken = TokenList.get(index+1);
				if(matching(defaultToken, TokenType.DEFAULT)) {
					if(matching(colonToken, TokenType.COLON)) {
						STMT_SEQUENCE.parse(TokenList, index+2);
					}else throw new ParsingExceptionHandler(colonToken, " for "+this.name());
				}else if(matching(defaultToken, TokenType.BRACKET_RIGHT)) {
					
				}else throw new ParsingExceptionHandler(colonToken, " for "+this.name());
			}
			
		},
		
		DECLARATION_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.VAR)) {
					ID.parse(TokenList, index+1);					
				}else throw new ParsingExceptionHandler(thisToken, " for "+this.name());
			}
			
		},
		
		ID{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);

				Token assignToken = TokenList.get(index++);
				if(matching(assignToken, TokenType.EQ)) {
					thisToken.setToken(TokenType.IDENTIFIER);
					ASSIGN_STMT.parse(TokenList, index);
				} else if(matching(thisToken, TokenType.IDENTIFIER)) {
					ID2.parse(TokenList, index+1);
				}else throw new ParsingExceptionHandler(thisToken, " for "+this.name());
					
			}
			
		},
		
		ID2{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				if(matching(thisToken, TokenType.SEMICOLON)) {
					
				}else if(matching(thisToken, TokenType.COMMA)) {
					ID.parse(TokenList, index+1);
					ID2.parse(TokenList, index+2);
				}else {
					
				}
					
			}
			
		},
		
		ASSIGN_STMT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				Token nextToken = TokenList.get(index+1);
				if(matching(nextToken, TokenType.INCREMENT) || matching(nextToken, TokenType.DECREMENT)) {
					INCREMENT_STMT.parse(TokenList, index);
				}
				else if(matching(thisToken, TokenType.IDENTIFIER)) {
					ASSIGN_OP.parse(TokenList, index+1);
					EXP.parse(TokenList, index+2);
				}
				else 
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
			}
			
		},
		
		ASSIGN_OP{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				Token thisToken = TokenList.get(index);
				switch(thisToken.getToken()){
				case ASSIGN:
					break;
				case ADDASSIGN:
					break;
				case SUBTASSIGN:
					break;
				default:
					throw new ParsingExceptionHandler(thisToken, " for "+this.name());
						
				}
			}
			
		},
		
		COMMENT{

			@Override
			void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler {
				// TODO Auto-generated method stub
				
			}
		};

		static Iterator<Token> itr;
		abstract void parse(List<Token> TokenList, int index) throws ParsingExceptionHandler;

		boolean matching(Token token, TokenType expect) {
			if(token.getToken().equals(expect)) return true;
			else return false;
		}

		
		
	}

	
	static class ParsingExceptionHandler extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2759182948133817455L;

		public ParsingExceptionHandler(String message) {
			super(message);
		}
		
		public ParsingExceptionHandler(Token token) {
			super(path + "\n\n\tParsing Error at line : " + token.getLineno() + " near \""+ getTokenDescription(token) + "\" (" + token.getTokenTag() +" : " + token.getToken() + ") "+" is invalid keyword\n");
		}
		
		public ParsingExceptionHandler(Token token, String message) {
			super(path + "\n\n\tParsing Error at line : " + token.getLineno() + " near \""+ getTokenDescription(token) + "\" (" + token.getTokenTag() +" : " + token.getToken() + ") "+" reason : " + message + "\n");
		}
		
		static Object getTokenDescription(Token temp) {
			switch(temp.getToken()) {
				case COMMENT:
					return temp.getLiteral();
				case LITERAL:
					return temp.getLiteral();
				case NUMBER:
					return temp.getNumber();
				default:
					return temp.getName();
					
			}
		}
	}
	
}
