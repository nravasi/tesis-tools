package configuration

/**
 * Created by nmravasi on 10/8/16.
 */
class Command {

    public static Process run(String command){
        return command.execute();
    }

    public static ArrayList runAndRead(String command) {
        def process = command.execute();

        def waiting = process.waitFor();

        if(waiting != 0){
        }

        return process.text.readLines();
    }

    public static void run(String... commands){
        commands.each {it.execute()};
    }
}
