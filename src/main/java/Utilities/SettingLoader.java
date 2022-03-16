package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingLoader {

    private String path;

    public SettingLoader() {
        path = setPath();
        createFile();
        loadSettings();
    }

    /*
    PrintLog
    IngoreDice
    Mctstime
    playgame
    isExperiment
    expecitdepth
    side
    goal/expectimax
     */

    public String setPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            return "src\\main\\resources\\setting\\set.txt";
        } else return "src/main/resources/setting/set.txt";
    }

    public void createFile() {
        File file = new File(path);


        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write("false\nfalse\n5000\n1\nfalse\n5\n-1\nfalse");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Setting File Exception");
            e.printStackTrace();
        }

    }

    public void loadSettings() {
        ArrayList<String> settings = new ArrayList();
        File file = new File(path);
        try {
            Scanner reader = new Scanner(file);

            GlobalVariables.PRINTLOG = Boolean.parseBoolean(reader.nextLine());//0
            GlobalVariables.IGNOREDICE = Boolean.parseBoolean(reader.nextLine());//1
            GlobalVariables.MCTSTIME = Long.parseLong(reader.nextLine());//2
            GlobalVariables.PLAYGAMES = Integer.parseInt(reader.nextLine());//3
            GlobalVariables.isExperiment = Boolean.parseBoolean(reader.nextLine());//4
            GlobalVariables.EXPECTIDEPTH = Integer.parseInt(reader.nextLine());//5
            GlobalVariables.GOALSIDE = Integer.parseInt(reader.nextLine());//6
            GlobalVariables.ISGOALOREXPECTI = Boolean.parseBoolean(reader.nextLine());//74

            System.out.println("Setting loaded:");
            System.out.println("Log:\t\t" + GlobalVariables.PRINTLOG);
            System.out.println("DIce:\t\t" + GlobalVariables.IGNOREDICE);
            System.out.println("MCTSTIME:\t" + GlobalVariables.MCTSTIME);
            System.out.println("HAMES:\t\t" + GlobalVariables.PLAYGAMES);
            System.out.println("Exp:\t\t" + GlobalVariables.isExperiment);
            System.out.println("Side:\t\t" + GlobalVariables.GOALSIDE);
            System.out.println("Goal/Exp:\t" + GlobalVariables.ISGOALOREXPECTI);


            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSetting() {
        ArrayList<String> settings = new ArrayList();
        File file = new File(path);
        try {
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                settings.add(reader.nextLine());
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public void editSetting(ArrayList<String> in)
    {
        if(in.size()!=8)
            return;

        File file = new File(path);


        try {
            FileWriter writer = new FileWriter(file);
            for(String s : in)
            {
                writer.write(s+"\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Setting File Exception");
            e.printStackTrace();
        }
    }

    public void resetSettings()
    {
        File file = new File(path);
        file.delete();
        createFile();
    }
}
