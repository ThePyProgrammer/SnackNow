package data;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wow {

    public static void main(String[] args) {

        try {
            ArrayList<Location> someData = getConvenienceStores();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static ArrayList<Location> getConvenienceStores() throws IOException {

        ArrayList<Location> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(" showlocation\\(\"([\\w#.,'/@() \\-]+)\", \"([\\w#.,'/@() \\-]+)\", ([\\d.]+), ([\\d.]+)\\);");

        // load 7-11.txt
        try (BufferedReader br = new BufferedReader(new FileReader("data/7-11.txt"))) {
            String line = br.readLine();
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    Location location = new Location(matcher.group(1), Double.parseDouble(matcher.group(3)), Double.parseDouble(matcher.group(4)));
                    result.add(location);
                } else {
                    System.out.println("skill issue"); // replace with error or better text
                }
                line = br.readLine();
            }
        }
        catch (NumberFormatException exception) {
            exception.printStackTrace();
        } /* finally {
            // finally!
        } */
        // the buffered reader auto closes after this try

        return result;

    }

}
