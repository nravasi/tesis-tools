package configuration

/**
 * Created by nmravasi on 10/8/16.
 */
class Command {

    public static Process run(String command){
        return command.execute();
    }

    public static void run(String... commands){
        commands.each {it.execute()};
    }
}
