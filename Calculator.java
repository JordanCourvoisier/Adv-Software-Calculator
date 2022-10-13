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