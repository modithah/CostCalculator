import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TestWriter {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Experiments\\multicriteria\\histogram\\histo.csv";
        ArrayList<String> doc= new ArrayList<>();
        ArrayList<String> doc3= new ArrayList<>();
        ArrayList<String> doc5= new ArrayList<>();
        ArrayList<String> doc5agg= new ArrayList<>();
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(s->{
                String[] vals = s.split(",");
                if(vals[1]!="0"){
                    addToList(doc, vals,1);
                }
                if(vals[2]!="0"){
                    addToList(doc3, vals,2);
                }
                if(vals[3]!="0"){
                    addToList(doc5, vals,3);
                }
                if(vals[4]!="0"){
                    addToList(doc5agg, vals,4);
                }
            });
writeFile(doc,"docdesign");
writeFile(doc3,"doc3");
writeFile(doc5,"doc5");
            writeFile(doc5agg,"doc5agg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addToList(java.util.List<String> list, String[] vals, int loc) {
        int count = Integer.parseInt(vals[loc]);
        for (int i = 0; i <count; i++) {
            list.add(vals[0]);
        }
    }

    private static void writeFile(ArrayList<String> arr, String name) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\Moditha\\Documents\\PhD\\SVN\\Experiments\\multicriteria\\histogram\\"+name+".csv");
        for(String str: arr) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }
}
