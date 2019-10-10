import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class smalljs{

    private smalljs module;
    private DFA machine;
    private String path;
    private char[] input_string;
    private char[] digit = "0123456789".toCharArray();
    private char[] letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_.".toCharArray();
    private char[] specials = "!@#$%^&*()-=+[]{}|\\;:\",/<>?\'".toCharArray();
    private String[] keyword_id = {"var", "do", "while", "if", "else", "switch", "case", "break", "default", "function", "return"};
    private String[] keyword_tag = {"<script_start>", "<script_end>", "<h1>", "</h1>", "<h2>", "</h2>", "<h3>", "</h3>", "<ul>", "</ul>", "<ol>", "</ol>", "</br>"};
    private String[] keyword_logical_value = {"true", "false"};
    private String[] keyword_function_name = {"window", "parseFloat", "document", "square"};
    private String[] white_space = {" ", "  ", "\t"};
    private String[] next_line = {"\n", "\r", "\n\r"};
    private ArrayList<String> user_defined_identifier;
    

    private smalljs(String fpath){
        this.path = fpath;
        init();
    }

    private void init(){
        print("Initializing Process..");
        user_defined_identifier = new ArrayList<String>();
        machine = new DFA();

        print("Scanning Starts");
        fileReader();
    }

    public static void main(String[] args) {
        String argument;
        if(args.length == 0) argument = "../test2.jss";
        else argument = args[0];
        smalljs module = new smalljs(argument);
    }

    private void fileReader(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while(true){
                String buffer = reader.readLine();
                if(buffer == null) break;
                //print(buffer);
                string_to_char(buffer);
                machine.scanner(input_string);
            }
        } catch (IOException e) {
            print(path);
            print("file reading error.");
        }
    }

    public void string_to_char(String string) {
        input_string = string.toCharArray();
    }


    class DFA{
        private int state;
        private int index;
        private String token_tag = "";
        private String stream = "";

        public void scanner(char[] input){
            state = 0;
            if(input.length != 0){
                for(index = 0 ; index < input.length ; index++){
                    state = transition(input[index], state);
                    if(state == 99){
                        token_print();
                        index--;
                        stream = "";
                        token_tag = "";
                        state = 0;
                    }
                }
                token_print();
                stream = "";
                token_tag = "";
            }
            
        }

        public int transition(char character, int current_state){
            switch (current_state) {
                case 0:
                    if(isReserved(""+character, white_space) || isReserved(""+character, next_line)) return 0;
                    
                    else if(isContained(character, digit)) {
                        stream = stream + character;
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
                                token_tag ="colon character";
                                return 12;
                            case ';':
                                token_tag = "semicolon character";
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
                                token_tag = "left brace character";
                                return 24;
                            case '}':
                                token_tag = "right brace character";
                                return 25;
                            case '\"':
                                return 26;
                            case '=':
                                return 29;
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
                        token_tag = "number";
                        return 99;
                    }
                case 2:
                    if(isContained(character, letter) || isContained(character, digit)) {
                        stream = stream + character;
                        return 2;
                    }
                    else {
                        keyword_checker(stream);
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
                        token_tag = "additive operator";
                        return 99;
                    }
                case 4:
                    token_tag = "assignment with addition operator";
                    return 99;
                case 5:
                    token_tag = "assignment with addition operator";
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
                        token_tag = "subtract operator";
                        return 99;
                    }
                case 7:
                    token_tag = "decrement operator";
                    return 99;
                case 8: 
                    token_tag = "assginment with subtraction operator";
                    return 99;
                case 9:
                    token_tag = "multiplication operator";
                    return 99;
                case 10:
                    if(character == '/') {
                        stream = stream + character;
                        return 11;
                    }
                    else {
                        token_tag = "division operator";
                        return 99;
                    }
                case 11:
                    stream = stream + character;
                    token_tag = "comment";
                    return 11;
                case 12:
                    return 99;
                case 13:
                    
                    return 99;
                case 14:
                    token_tag = "punctuation character";
                    return 99;
                case 15:
                    stream = stream + character;
                    if(character == ')'){
                        token_tag = "function parameter";
                        return 17;
                    }
                    return 16;
                case 16:
                    stream = stream + character;
                    if(character == ')'){
                        token_tag = "function parameter";
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
                        token_tag = "less than comparison operator";
                        return 99;
                    }
                case 19:
                    token_tag = "less than or equal comparison operator";
                    return 99;
                case 20:
                    if(isContained(character, letter)) {
                        stream = stream + character;
                        return 20;
                    }
                    else if(character == '>') {
                        stream = stream + character;
                        token_tag = "keyword tag name";
                        return 21;
                    }
                    return 99;
                case 21:
                    token_tag = "keyword tag name";
                    return 99;
                case 22:
                    if(character == '='){
                        stream = stream + character;
                        return 23;
                    }
                    else{
                        token_tag = "larger than comparsion operator";
                        return 99;
                    }
                case 23:
                    token_tag = "larger than or eqaul comparison operator";
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
                    token_tag = "literal";
                    return 99;
                case 29:
                    if(character == '='){
                        stream = stream + character;
                        return 30;
                    }
                    else{
                        token_tag = "assignemnt operator";
                        return 99;
                    }
                case 30:
                    token_tag = "equality comparsion operator";
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

        private void keyword_checker(String target){
            if(isReserved(target, keyword_id)) token_tag = "keyword id";
            else if(isReserved(target, keyword_logical_value)) token_tag = "keyword logical value";
            else if(isReserved(target, keyword_function_name)) token_tag = "keyword function name";
            else if(user_defined_identifier.contains(target)) token_tag = "user defined id";
            else{
                user_defined_identifier.add(target);
                token_tag = "user defined id";
            }

        }

        private void token_print(){
            print(stream +"\t"+ token_tag);
        }
    }

    private boolean isContained(char target, char[] set){
        for(int i = 0; i < set.length ; i++){
            if(set[i] == target) return true;
        }
        return false;
    }

    private boolean isReserved(String target, String[] set){
        for(int i = 0; i < set.length ; i++){
            if(set[i].equals(target)) return true;
        }
        return false;
    }

    

    private void print(String message){
        System.out.println(message);
    }
}