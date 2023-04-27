package com.SaravananInterpeter.Jnox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class jnox
{

    static boolean hadError = false;

    public static void main(String[] args) throws IOException
    {
        if(args.length>1)
        {
            System.out.println("Usage: jnox [script]");
            // Indicate an error in the exit code.
            if(hadError) System.exit(64);
        } else if (args.length==1) {
            runFile(args[0]);
        }
        else {
            runPrompt();
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        // Infinite for loop
        for(;;){
            System.out.println("> ");
            //reads the input from user and stores it in a string line
            String line = reader.readLine();
            if(line==null) break;
            run(line);
            // Reset the flag in loop if user makes a mistake
            // it should not kill their entire session
            hadError=false;
        }
    }

    private static void runFile(String path) throws IOException {
        //Read the contents of the source file into byte array
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run(String source){
        Scanner sc = new Scanner(source);
        List<Token> tokens = sc.scanTokens();

        // for now, we print the tokens.
        for(Token token : tokens){
            System.out.println(token);
        }

    }
    static void error(int line, String message){
        report(line,"",message);
    }
    private static void report(int line, String where, String message){
        System.err.println("[line " + line + "] Error" + where +": " + message);
        hadError = true;
    }

}
