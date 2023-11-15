import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;



class Dictionary {
    static HashMap<String, List<String>> slangDict = new HashMap<String, List<String>>();
    static HashMap<String, List<String>> meaningDict = new HashMap<String, List<String>>();
    public static String slangDirectory = "slang.txt";
    static String backupSlangDirectory = "backup.txt";
    static String historyDirectory = "history.txt";

    public void readDataFromFile(String fileName){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line=bufferedReader.readLine())!=null) {
                if(line.contains("`"))
                {
                    String[] Word = line.split("`");
                    String wordname = Word[0];
                    String[] meanings = Word[1].split("\\|");
                    for(int i = 0; i < meanings.length; i++){
                        meanings[i] = meanings[i].trim();
                    }
                    List<String> definition = Arrays.asList(meanings);
                    slangDict.put(wordname, definition);

                    for(String i: definition){
                        List<String> slang;
                        if(!meaningDict.containsKey(i)){
                            slang = new ArrayList<>();
                            slang.add(wordname);
                            meaningDict.put(i,slang);
                        }
                        else{
                            slang = meaningDict.get(i);
                            slang.add(Word[0]);
                        }
                    }
                }
            }
            System.out.println("Import data from file successfully!");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Import data from file failed!");
        }
    }

    public void saveSlangDict(String fileName)
    {
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            StringBuffer stringBuffer = new StringBuffer();
            for (String nameword: slangDict.keySet()){
                stringBuffer.append(nameword);
                stringBuffer.append("`");

                for(String meaning: slangDict.get(nameword)){
                    if (slangDict.get(nameword).indexOf(meaning)!=0) stringBuffer.append("|");
                    stringBuffer.append(meaning);
                }

                stringBuffer.append(System.getProperty("line.separator"));
            }
            
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public String display(String wordname){
        wordname = wordname.toUpperCase();
        List<String> definition = slangDict.get(wordname);

        if (definition == null) {
            return "Slang \"" + wordname + "\" not found.";
        }

        StringBuilder temp = new StringBuilder(wordname).append(": ");

        for (String i : definition) {
            if (definition.indexOf(i) != 0) temp.append(" | ");
            temp.append(i);
        }
        return temp.toString();
    }

    public List<String> searchSlangDict(String slang){
        slang = slang.toUpperCase();
        List<String> temp = slangDict.get(slang);
        return temp != null ? temp : Collections.emptyList();
    }

    public List<String> searchDefinitionDict(String meaning){
        List<String> temp = null;
        if (meaningDict.containsKey(meaning))
        {
            temp = meaningDict.get(meaning);
        }
        return temp != null ? temp : Collections.emptyList();
    }

    public void saveSlangSearchHistory(String query, StringBuilder result) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(historyDirectory,true));
        String stringBuffer = query + result + System.getProperty("line.separator");

        bufferedWriter.write(stringBuffer);
        bufferedWriter.flush();
        bufferedWriter.close();
    }


    public void showHistory() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(historyDirectory))) {
            String inline;
            while ((inline = bufferedReader.readLine()) != null) {
                System.out.println(inline);
            }
        }
    }

    public void addSlangDict(String nameword, List<String> definition, Scanner scanner) {
    nameword = nameword.toUpperCase();

    if (slangDict.containsKey(nameword)) {
        System.out.println(nameword + " already exists.");
        System.out.println("Choose an action:");
        System.out.println("1 - Overwrite");
        System.out.println("2 - Duplicate");
        System.out.println("Any other keys - Cancel");

        System.out.print("Your choice: ");
        int choice;

        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Canceling operation.");
            return;
        }

        switch (choice) {
            case 1:
                slangDict.put(nameword, definition);
                break;
            case 2:
                List<String> values = slangDict.get(nameword);
                if (values != null) {
                    values.addAll(definition);
                }
                break;
            default:
                break;
        }
    } else {
        slangDict.put(nameword, definition);
    }
}

}

public class SlangDictionarys{
    static Dictionary slangDictionary = new Dictionary();
    public static void run() throws IOException {
        int choose;
        Scanner br = new Scanner(System.in);
        try {
            System.out.println("=======================================");
            System.out.println("           Slang Dictionary");
            System.out.println("=======================================");
            System.out.println("1. Search definition of a slang");
            System.out.println("2. Search slang by definition");
            System.out.println("3. Show search history");
            System.out.println("4. Add a new slang word");
            System.out.println("5. Edit a slang word");
            System.out.println("6. Remove a slang word");
            System.out.println("7. Reset dictionary");
            System.out.println("8. Get a random slang word");
            System.out.println("9. Quiz: What's that Slang");
            System.out.println("10. Quiz: What's that Mean");
            System.out.println("0. Save and exit");
            System.out.println("=======================================");
            System.out.print("Enter your choice: ");


            choose = br.nextInt();
            br.nextLine();
            switch (choose) {
                case 0:
                    slangDictionary.saveSlangDict("slang.txt");
                    System.exit(0);
                    break;

                case 1:
                    System.out.println("1 - Search definition of a slang \n Enter slang to search:");
                    String querySlang;
                    querySlang = br.nextLine();
                    List<String> meanings = slangDictionary.searchSlangDict(querySlang);
                    if (meanings == null) {
                        System.out.println("Slang \"" + querySlang + "\" is not found");
                    } else {
                        System.out.print(querySlang);
                        StringBuilder result = new StringBuilder();
                        result.append(": ");
                        for (String i : meanings) {
                            if (meanings.indexOf(i) != 0) result.append(" | ");
                            result.append(i);
                        }
                        System.out.println(result);
                        slangDictionary.saveSlangSearchHistory(querySlang, result);
                    }
                    break;

                case 2:
                    System.out.println("2 - Search slang by definition \n Enter definition to search:");
                    String queryDefinition;
                    queryDefinition = br.nextLine();
                    List<String> slangs = slangDictionary.searchDefinitionDict(queryDefinition);
                    if (slangs == null) {
                        System.out.println("Definition \"" + queryDefinition + "\" is not found");
                    } else {
                        System.out.print(queryDefinition);
                        StringBuilder result = new StringBuilder();
                        result.append(": ");
                        for (String i : slangs) {
                            if (slangs.indexOf(i) != 0) result.append(" | ");
                            result.append(i);
                        }
                        System.out.println(result);
                    }
                    break;

                case 3:
                    System.out.println("3 - Show search history \n Result search history:");
                    slangDictionary.showHistory();
                    break;
                
                case 4:
                    System.out.println("4 - Add slang word \n Enter a slang:");
                    String queryAddSlang = br.nextLine();
                    System.out.println("Enter its meanings:");
                    String queryAddDefinition = br.nextLine();
                    List<String> list4 = Arrays.asList(queryAddDefinition.split("\\|"));
                    slangDictionary.addSlangDict(queryAddSlang, list4, br);
                    System.out.println("Operation is completed");
                    break;

                default:
                    break;
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input. Try again");
                br.nextLine();
            }
            System.out.println("\nPress ENTER to continue");
            br.nextLine();

    }

    public static void main(String[] args) throws IOException {
        slangDictionary.readDataFromFile(Dictionary.slangDirectory);
        while(true)
        {
            run();
        }

    }
}

