import egps2.Launcher;

public class Launcher4Test extends Launcher {


    public static void main(String[] args) throws Exception {

        Launcher.isDev = true;
        Launcher.main(args);
    }

}