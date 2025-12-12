
import com.medlemsklubb.businesslogic.MemberService;
import com.medlemsklubb.businesslogic.RentalService;
import com.medlemsklubb.data.Invetory;
import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.head.*;
import com.medlemsklubb.history.Rental;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //skapar objekt
        Scanner scanner = new Scanner(System.in); //scanner för att läsa in val
        MemberRegistry memberRegistry = new MemberRegistry(); // Register för medlemmar
        Invetory invetory = new Invetory(); // lager av items
        MemberService memberService = new MemberService(memberRegistry); //service för att skapa och uppdatea medlemmar
        RentalService rentalService = new RentalService(invetory, memberRegistry); //kopplar medlemmar med uthyrning


        //Lägger till i invetory några fordon.
        invetory.add(new Bil("B001", "Manuell bil", 120, "Mercedes", "Svart", true));
        invetory.add(new Bil("B002", "Automat bil", 100, "BMW", "VIt", true));
        invetory.add(new Elskooter("S001", "Elskooter mini", 15, "skooter", false, 20));
        invetory.add(new Elskooter("S002", "Eslkooter super pro", 35, "skooter", true, 35));

        boolean menu = true; // boolean för huvudmeny

        while (menu) { //while-loop för huvudmeny. körs tills användare väljer 4.

            System.out.println("HUVUDMENY ");
            System.out.println("[1] Hantera medlemmar");
            System.out.println("[2] Lista/filtrera items");
            System.out.println("[3] Boka/avsluta uthyrning");
            System.out.println("[4] Summera intäkter");
            System.out.println("[5] Avsluta");
            System.out.println("Välj alternativ: ");

            String choice = scanner.nextLine().trim();
            if (choice.isBlank()) {
                System.out.println("Skriv en siffra 1-5.");
                continue; // hoppar till nästa varv i loopen om det blir fel.
            }
            int menuChoice = Integer.parseInt(choice);

            //switch-sats för menyn
            switch (menuChoice) {

                // Hantera medlemmar (LÄGG TILL/SÖK/ÄNDRA MEDLEMMAR
                case 1: //  lägg till/sök/ändra medlemmar
                    System.out.println("[1] Sök medlem");
                    System.out.println("[2] Lägg till ny medlem ");
                    System.out.println("[3] Ändra medlem");
                    System.out.print("Välj alternativ: ");

                    String choice2 = scanner.nextLine().trim();
                    if (choice2.isBlank()) {
                        System.out.println("Skriv en siffra 1-3.");
                        break;
                    }
                    int menuChoise2 = Integer.parseInt(choice2);

                    switch (menuChoise2) {

                        case 1: //sök efter medlem med id
                            System.out.println("Sök id: ");
                            String searchWord = scanner.nextLine().trim();

                            if (searchWord.matches("\\d+")) { // om bara siffror sökes
                                int id = Integer.parseInt(searchWord);
                                Optional<Member> opt = memberRegistry.findById(id);
                                if (opt.isPresent()) {
                                    Member member = opt.get();
                                    System.out.println(member);
                                    System.out.println("Historik: ");
                                    if (member.getHistory().isEmpty()) {
                                        System.out.println("hittar ingen historik");
                                    } else {
                                        for (String historik : member.getHistory()) {
                                            System.out.println(historik);
                                        }
                                    }
                                } else {
                                    System.out.println("Hittar ingen medlem tyvärr.");
                                }
                            } else { // annars om sökes med bokstäver
                                List<Member> found = new ArrayList<>();
                                for (Member member : memberRegistry.findAll()) {
                                    String fullName = member.getFirstname() + " " + member.getLastname();
                                    if (fullName.equalsIgnoreCase(searchWord)) {
                                        found.add(member);
                                    }

                                }

                            }
                            break;

                        case 2: //lägg till medlem
                            System.out.println("Förnamn: ");
                            String firstName = scanner.nextLine().trim();
                            System.out.println("Efternamn: ");
                            String lastName = scanner.nextLine().trim();
                            System.out.println("Status (skriv antingen: standard, senior eller student): ");
                            String statusLevel = scanner.nextLine().trim().toUpperCase();
                            if (statusLevel.isBlank()) {
                                statusLevel = StatusLevel.STANDARD;
                            }
                            System.out.println("Körkort? (ja eller nej): ");
                            String stringLicense = scanner.nextLine().trim();
                            Boolean license = stringLicense.equalsIgnoreCase("ja");

                            Member newMember = new Member(firstName, lastName, statusLevel, license); // läggger till medlem i registret
                            memberRegistry.add(newMember);
                            System.out.println("Medlem har lagt till!");
                            System.out.println("Id: " + newMember.getId());
                            System.out.println("Firstname: " + newMember.getFirstname());
                            System.out.println("Lastname: " + newMember.getLastname());
                            System.out.println("Körkort: " + (license ? "ja" : "nej"));
                            break;

                        case 3: // ÄNDRA MEDLEM---------------------------------------------
                            System.out.println("Ange id på medlem som ska ändras: ");
                            String idString = scanner.nextLine().trim();
                            if (!idString.matches("\\d+")) {
                                System.out.println("ogiltigt id.");
                                break;
                            }
                            int editId = Integer.parseInt(idString);

                            Optional<Member> editIdOpt = memberRegistry.findById(editId);
                            if (editIdOpt.isEmpty()) {
                                System.out.println("Hittar ej id");
                                break;
                            }

                            Member editMember = editIdOpt.get();
                            System.out.println("Uppdatera information: ");

                            System.out.println("Nytt förnamn: ");
                            String newFirstName = scanner.nextLine().trim();
                            System.out.println("Nytt efternamn: ");
                            String newLastName = scanner.nextLine().trim();
                            System.out.println("Nytt status: ");
                            String newStatusLevel = scanner.nextLine().trim().toUpperCase();
                            if (newStatusLevel.isBlank()) newStatusLevel = StatusLevel.STANDARD;

                            System.out.println("Körkort? (ja eller nej): ");
                            String stringNewLicense = scanner.nextLine().trim();
                            boolean newLicense = stringNewLicense.equalsIgnoreCase("ja");

                            editMember.setFirstname(newFirstName);
                            editMember.setLastname(newLastName);
                            editMember.setLevel(newStatusLevel);
                            editMember.setLicense(newLicense);

                            System.out.println("MEdlem är nu ändrad!");
                            System.out.println(editMember);
                            break;


                        default:
                            System.out.println("ogiltigt val");
                    }
                    break;

                case 2: // lista och filtrera items
                    System.out.println("Alla items skrivs ut");
                    List<Item> items = invetory.findAll();
                    for (Item item : items) {
                        item.info(); //skriver ut varje info var för sig
                    }

                    System.out.println("Filtrera efter fordon som kräver körkort? (ja eller nej): ");
                    String filterLicense = scanner.nextLine();
                    if (filterLicense.equalsIgnoreCase("ja")) {
                        boolean found = false;
                        for (Item item : items) {
                            if (item.requiresLicense()) {
                                item.info();
                            }
                        }

                    }
                    break;


                case 3: // boka och avsluta uthyrning
                    System.out.println("Vad vill du göra? ");
                    System.out.println("[1]Boka");
                    System.out.println("[2]Avsluta");
                    System.out.println("Välj: ");
                    String choice3 = scanner.nextLine().trim();
                    if (choice3.isBlank()) {
                        System.out.println("Skriv en siffra 1-2.");
                    }
                    int menuChoice3 = Integer.parseInt(choice3);

                    // för att boka
                    if (menuChoice3 == 1) {
                        System.out.println("Ange medlems id: ");
                        String membchoice = scanner.nextLine().trim();
                        if (membchoice.isBlank()) {
                            System.out.println("ogiltigt.");
                            break;
                        }
                        int memberID = Integer.parseInt(membchoice);

                        Optional<Member> memberOpt = memberRegistry.findById(memberID);
                        if (memberOpt.isEmpty()) {
                            System.out.println("hittar ej medlem med samma id.");
                            break;
                        }

                        //skriver ut alla items med id för bokning.
                        System.out.println("Tillgängliga items: ");
                        for (Item item : invetory.findAll()) {
                            System.out.println("Id: " + item.itemId() + " | Namn: " + item.getItemName() + "| pris: " + item.getPrice());
                        }
                        System.out.println("Ange item id: ");
                        String itemID = scanner.nextLine().trim().toUpperCase();

                        System.out.println("Ange slut datum (YYYY-MM-DD): )");
                        String stringEndDate = scanner.nextLine().trim();
                        LocalDate endDate = LocalDate.parse(stringEndDate);

                        Optional<Rental> rentalOpt = rentalService.book(memberID, itemID, endDate);

                        if (rentalOpt.isPresent()) {
                            Rental rental = rentalOpt.get();
                            System.out.println("Bokningen är klar!");
                            System.out.println("uthyrnings id: " + rental.getRentalId());
                            System.out.println(rental);
                        } else {
                            System.out.println("Bokningen misslyckades");
                        }

                    }

                    //för att avsluta
                    else if (menuChoice3 == 2) {
                        System.out.println("Ange medlems id: ");
                        int memberID = Integer.parseInt(scanner.nextLine().trim());

                        Optional<Member> memberOpt = memberRegistry.findById(memberID);
                        Member member = memberOpt.get();

                        List<Rental> rentals = rentalService.getAllRentalals();
                        boolean anyRental = false;
                        System.out.println("Uthyrningar: ");
                        for (Rental rental : rentals) {
                            if (rental.getMember().getId() == member.getId() && rental.isActive()) {
                                System.out.println(rental.getRentalId() + ": " + rental);
                                anyRental = true;
                            }
                        }
                        if (!anyRental) {
                            System.out.println("Denna medlem har inge aktvi uthyrning.");
                            break;
                        }

                        System.out.println("Ange uthyrnings id att avsluta: ");
                        String rentIdChoice = scanner.nextLine().trim();
                        int rentalID = Integer.parseInt(rentIdChoice);

                        Rental selected = null;

                        for (Rental rental : rentals) {
                            if (rental.getRentalId() == rentalID && rental.isActive()) {
                                selected = rental;
                                break;
                            }
                        }

                        if (selected == null) {
                            System.out.println("Denna medlem har ingen aktiv uthyrning.");
                            break;
                        } else {
                            selected.finish(LocalDate.now());
                            System.out.println("uthyrning avslutad: " + selected);
                        }
                    } else {
                        System.out.println("ogiltigt val ");

                    }
                    break;

                case 4: //summera intäkter
                    System.out.println("Summerade intäkter:");

                    double totalIncome = 0;
                    for (Rental rental : rentalService.getAllRentalals()) {
                        totalIncome += rental.getTotalPrice();
                    }

                    System.out.println("Totala intäkter: " + totalIncome + " kr");
                    break;

                case 5:
                    System.out.println("Pogrammet avslutas.");
                    menu = false;
                    break;
            }

        }
    }

}

