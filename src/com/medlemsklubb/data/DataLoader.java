package com.medlemsklubb.data;

import com.medlemsklubb.head.*;

import java.io.BufferedReader;
import java.io.FileReader;

//DataLoader läser in data för textfiler.
public class DataLoader {

    //läser in data från filen member.txt och lägger det i MemberRegistry
    public static void loadMember(MemberRegistry registry) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/member.txt"))) {
            String line;

            //här läser jag in filen tills det inte finns några fler rader
            while ((line = reader.readLine()) != null) {

                line = line.trim(); // jag tar bort mellanslag som kan råkas läggas.
                if (line.isEmpty()) continue;

                String[] data = line.split(";"); //delar upp raden med semikolkon

                if (data.length < 5) { //kontrollerar så att varje medlem har 5 värden.
                    System.out.println("Invalid member row: " + line);
                    continue;
                }

                //konverterar text till datan typ
                int id = Integer.parseInt(data[0].trim());
                String firstName = data[1].trim();
                String lastName = data[2].trim();
                String level = data[3].trim();
                boolean license = Boolean.parseBoolean(data[4].trim());


                registry.add(new Member(id, firstName, lastName, level, license)); //medlem skapas och läggs in i registret
            }

        } catch (Exception e) {
            e.printStackTrace(); // skriver ut fel vid fel vid inläsning
        }
    }

    //läser in data från filen inventory.txt och lägger det i Inventory
    public static void loadInventory(Inventory inventory) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/inventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(";");

                if (data.length < 7) { //kontrollerar så att varje medlem har 7 vräden
                    System.out.println("Invalid inventory row: " + line);
                    continue;
                }

                String id = data[0].trim();
                String type = data[1].trim();
                String name = data[2].trim();
                double price = Double.parseDouble(data[3].trim());

                if ("Bil".equals(type)) {
                    inventory.add(new Bil(
                            id, name, price,
                            data[4].trim(),
                            data[5].trim(),
                            Boolean.parseBoolean(data[6].trim())
                    ));

                } else if ("Elskooter".equals(type)) {
                    inventory.add(new Elskooter(
                            id, name, price,
                            data[4].trim(),
                            Boolean.parseBoolean(data[5].trim()),
                            Integer.parseInt(data[6].trim())
                    ));
                } else {
                    System.out.println("Unknown type: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); //skrivrr ut fel vd fel av inläsning
        }
    }
}
