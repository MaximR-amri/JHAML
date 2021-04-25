package be.syntra.beige.team;

import java.io.*;

/**
 * <h1>App</h1>
 * Read command from console.
 * Create ReaderHaml
 * Create HamlData
 * Validate HamlData
 * Create Html
 * Write Html to file
 *
 *
 * @author  Team Beige
 * @version 1.0
 * @since   2021-03-24
 */

public class App {
    public static void main(String[] args) throws IOException {

        CommandLineInterpreter interpreter = new CommandLineInterpreter();
        interpreter.interpretCommand(args);

        if(!interpreter.isIsError() && interpreter.getFileNames().size()>0) {
            for (int i = 0; i < interpreter.getFileNames().size(); i += 2) {
                if (interpreter.getFileNames().get(i + 1) == null) {
                    compileFile(interpreter.getFileNames().get(i));
                } else if (interpreter.getFileNames().get(i + 1) != null) {
                    compileFile(interpreter.getFileNames().get(i), interpreter.getFileNames().get(i + 1));
                }
            }
            System.out.println("Compiling is done.");
        } else System.out.println(interpreter.getError());
    }

    public static void compileFile(String input) throws IOException {
        ReaderHaml rh = new ReaderHaml(input);
        HamlData hd = rh.read();
        HamlValidation validator = HamlValidation.validateHaml(hd);
        if(validator.isValid()){

            // Create Html object
            Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
            // Pass HamlDataElements to HtmlConverter
            HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
            // Pass Html object to Writer
            Writer writer = new Writer(html.getInputFileName(), html.getOutputFileName(), html.getHtmlElements());
            writer.writeToOutputFile();

        }else{

            // Parse the hamlErrors arraylist of the validator object
            //
            System.out.println("\nErrors found in " + hd.getInputFileName() + ":");
            for(String s : validator.getHamlErrors()){
                System.out.println(s);
            }
            System.out.println("=> " + hd.getInputFileName() + " not parsed!");

        }
    }

    public static void compileFile(String input, String output) throws IOException {
        ReaderHaml rh = new ReaderHaml(input, output);
        HamlData hd = rh.read();
        HamlValidation validator = HamlValidation.validateHaml(hd);
        if(validator.isValid()){

            // Create Html object
            Html html = new Html(hd.getInputFileName(), hd.getOutputFileName());
            // Pass HamlDataElements to HtmlConverter
            HtmlConverter.convertToHtml(hd.getHamlDataElements(), html);
            // Pass Html object to Writer
            //
            Writer writer = new Writer(html.getInputFileName(), html.getOutputFileName(), html.getHtmlElements());
            writer.writeToOutputFile();

        }else{

            // Parse the hamlErrors arraylist of the validator object
            //
            System.out.println("\nErrors found in " + hd.getInputFileName() + ":");
            for(String s : validator.getHamlErrors()){
                System.out.println(s);
            }
            System.out.println("=> " + hd.getInputFileName() + " not parsed!");

        }
    }





}
