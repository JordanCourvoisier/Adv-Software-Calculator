public static void main(String[] args){
    //Initialize variables and Scanner
    Scanner input = new Scanner(System.in);

    //Get formula to evaluate from user
    System.out.println("Welcome to the Courvoisier Calculator");
    System.out.print("Enter the formula would you like to evaluate =  ");
    String eq = input.nextLine();

    //Remove white space
    eq = eq.replaceAll("\\s", "");
    //Parse parentheses for validity
    eq = parseParen(eq);
    //Parse chars for validity
    validChars(eq);
    //Ensure no empty tan/cos/log etc exit
    emptySys(eq);
    //Change - to + or +-1* depending on amount
    eq = unaryOp(eq);
    //Ensure equation syntax is accurate
    validEquation(eq);

    //Seperate numbers / operators with a space and convert to postfix notation
    eq = tokenize(eq);
    eq = postfix(eq);

    //Evaluate number and print 
    evaluate(eq);

    input.close();
}

    // Checks formula to guarentee that parentheses and curly braces
    // are in a valid position, number, etc.
    // Takes formula as param
    // Returns updated formula (removed empty param / braces)
    private static String parseParen(String eq){
        //Removes any parentheses or curly braces with nothing inside of them
        for(int i = 0; i < eq.length() - 1; i++){
            //Solves edge case where empty paren at the end of the formula
            if(eq.charAt(i) == '(' && i == eq.length() - 2){
                eq = eq.substring(0, eq.length() - 2);
                break;
            } 
            //Cut out empty paren
            else if(eq.charAt(i) == '(' && eq.charAt(i + 1) == ')'){
                eq = eq.substring(0, i) + eq.substring(i + 2, eq.length());
                i--;
            }
            //Cut out empty braces
            else if(eq.charAt(i) == '{' && i == eq.length() - 2){
                eq = eq.substring(0, eq.length() - 2);
                break;
            } 
            else if(eq.charAt(i) == '{' && eq.charAt(i + 1) == '}'){
                eq = eq.substring(0, i) + eq.substring(i + 2, eq.length());
                i--;
            }
        }
        
        //If at any point there ar emore right parentheses than left, or more right curly braces than left, then the formula is invalid
        int Lparen = 0;
        int Rparen = 0;
        int Lcurl = 0;
        int Rcurl = 0;
        String temp;
        char curr;

        //If we get a closing parentheses while a curly brace is open, or a closing brace while a parentheses is open, then the formula is invalid
        boolean openParen = false;
        boolean openCurl = false;

        //Stack to hold memory of previous left paren / curly braces
        Stack<String> stack = new Stack<>();

        //Check to ensure parentheses and curly braces are in a valid order
        for(int i = 0; i <= eq.length() - 1; i++){
            curr = eq.charAt(i);
            //Check for paren/curly, add to stack
            if(curr == '('){
                stack.push("(");
                Lparen++;
                openParen = true;
                openCurl = false;
            }if(curr == ')'){
                if(openCurl){
                    System.out.println("Invalid formula, check your () and {}");
                    System.exit(0);
                }
                Rparen++;
                openParen = false;
                // Return to last brace / paren
                if(!stack.isEmpty()){
                    temp = stack.pop();
                    if(temp.equals("(")) openParen = true;
                    else openCurl = true;
                }
            }if(curr == '{'){
                stack.push("{");
                Lcurl++;
                openCurl = true;
                openParen = false;
            }if(curr == '}'){
                if(openParen){
                    System.out.println("Invalid formula, check your () and {}");
                    System.exit(0);
                }
                Rcurl++;
                openCurl = false;
                if(!stack.isEmpty()){
                    temp = stack.pop();
                    if(temp.equals("(")) openParen = true;
                    else openCurl = true;
                }
            }
            
            // More right than left, exit program with message
            if(Rparen > Lparen || Rcurl > Lcurl){
                System.out.println("Invalid formula, check your () and {}");
                System.exit(0);
            }

        }
    
        if(Lparen != Rparen || Lcurl != Rcurl){
            System.out.println("Invalid formula, check your () and {}");
            System.exit(0);
        }

        return eq;
    }