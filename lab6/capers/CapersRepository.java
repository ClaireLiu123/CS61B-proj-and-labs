package capers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static capers.Utils.*;

/** A repository for Capers 
 * @author Claire Liu
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");
    // TODO Hint: look at the `join`
    //      function in Utils
    static File storyFile;

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        CAPERS_FOLDER.mkdir();
        //File dogFolder = Utils.join(CWD, ".capers", "dogs");
        storyFile = Utils.join(".capers", "Story.txt");
        try {
            storyFile.createNewFile();
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }

    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        if (storyFile != null) {
            String storyString = readContentsAsString(storyFile); // first read the file and store it in a string
            String withSpaces = storyString + text + "\n"; // add spaces
            writeContents(storyFile, withSpaces); // writeContents will lost the history
            System.out.println(withSpaces); // print it out
        } else {
            String textWithSpace = text + "\n";
            writeContents(storyFile, textWithSpace); // writeContents will lost the history
            System.out.println(textWithSpace); // print it out
        }

    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.println(d.toString());
        // TODO
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog d = Dog.fromFile(name);
        d.haveBirthday();
        d.saveDog(); // save the age+1
        // TODO
    }
}
