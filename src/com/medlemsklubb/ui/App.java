package com.medlemsklubb.ui;


import com.medlemsklubb.businesslogic.MemberService;
import com.medlemsklubb.businesslogic.RentalService;
import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.exception.InvalidMemberDataException;
import com.medlemsklubb.head.*;
import com.medlemsklubb.data.Inventory;
import com.medlemsklubb.history.Rental;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.medlemsklubb.data.DataLoader;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App extends Application {

    //Service lager och data lager
    private MemberService memberService;
    private MemberRegistry memberRegistry;
    private Inventory inventory;
    private RentalService rentalService;
    //ui komponenter
    private ListView<Member> memberListView;
    private Label errorLabel;

    @Override
    public void start(Stage stage) {

        //initierar data och services
        memberRegistry = new MemberRegistry();
        memberService = new MemberService(memberRegistry);

        inventory = new Inventory();
        rentalService = new RentalService(inventory, memberRegistry);

        // läser in fil från start
        DataLoader.loadMember(memberRegistry);
        DataLoader.loadInventory(inventory);

        //skapar första scenen
        stage.setTitle("Medlemsklubb");
        stage.setScene(createMainMenuScene(stage));
        stage.show();

    }

    //MAIN MENY
    private Scene createMainMenuScene(Stage stage) {

        Label titleLabel = new Label("Huvudmeny");

        Button firstButton = new Button("Hantera Medlemmar");
        Button secondButton = new Button("Föremål");
        Button thirdButton = new Button("Boka/Avsluta medlemmar");
        Button fourthButton = new Button("Summera intäkter");
        Button fifthButton = new Button("Avsluta");

        //kopplar knappar till vyer
        firstButton.setOnAction(event -> stage.setScene(createManageMemberMenuScene(stage)));
        secondButton.setOnAction(event -> stage.setScene(createItemMenuScene(stage)));
        thirdButton.setOnAction(event -> stage.setScene(createRentalMenuScene(stage)));
        fourthButton.setOnAction(event -> stage.setScene(createSumScene(stage)));
        fifthButton.setOnAction(event -> stage.close());

        VBox vBox = new VBox(10, titleLabel, firstButton, secondButton, thirdButton, fourthButton, fifthButton);
        vBox.setPadding(new Insets(20));

        return new Scene(vBox, 400, 300);

    }


    //MENY FÖR HANTERA MEDLEMMAR
    private Scene createManageMemberMenuScene(Stage stage) {

        Label titleLabel = new Label("Hantera Medlemmar");

        Button addButton = new Button("Lägg till medlem");
        Button searchButton = new Button("Sök medlem");
        Button changeButton = new Button("Ändra medlem");
        Button backButton = new Button("Tillbaka");

        //navigering mellan scener
        addButton.setOnAction(event -> stage.setScene(createAddMemberScene(stage)));
        searchButton.setOnAction(event -> stage.setScene(createSearchMemberScene(stage)));
        changeButton.setOnAction(event -> stage.setScene(createChangeMemberScene(stage)));
        backButton.setOnAction(event -> stage.setScene(createMainMenuScene(stage)));

        VBox vbox = new VBox(10, titleLabel, addButton, searchButton, changeButton, backButton);
        vbox.setPadding(new Insets(20));

        return new Scene(vbox, 400, 300);

    }

    //Lägg till meldemmar
    private Scene createAddMemberScene(Stage stage) {
        Label titleLabel = new Label("Lägg till medlem");

        //formulär för medlemsuppgifter
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Förnamn");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Efternamn");

        //val av medlemsnivå
        ChoiceBox<String> levelchoiceBox = new ChoiceBox<>();
        levelchoiceBox.getItems().addAll(
                StatusLevel.STANDARD, StatusLevel.STUDENT, StatusLevel.SENIOR);
        levelchoiceBox.setValue(StatusLevel.STANDARD);

        CheckBox licenseCheckbox = new CheckBox("Har körkort");
        Button addMemberButton = new Button("Skapa medlem");
        Button backButton = new Button("Tillbaka");

        //label för felmeddelanden
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        //lista för medlemmar
        memberListView = new ListView<>();

        addMemberButton.setOnAction(e -> {
            errorLabel.setText("");


            try {
                Member member = memberService.create(
                        firstNameField.getText(), lastNameField.getText(), levelchoiceBox.getValue(), licenseCheckbox.isSelected());

                //lägg till i listan
                memberListView.getItems().add(member);

                //Rensa fältet om skapalse är lyckad
                firstNameField.clear();
                lastNameField.clear();
                levelchoiceBox.setValue(StatusLevel.STANDARD);
                licenseCheckbox.setSelected(false);
            } catch (InvalidMemberDataException ex) {
                errorLabel.setText(ex.getMessage());
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            stage.setScene(createManageMemberMenuScene(stage));
        });

//layout med gridpane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Förnamn:"), 0, 0);
        gridPane.add(firstNameField, 1, 0);
        gridPane.add(new Label("Efternamn:"), 0, 1);
        gridPane.add(lastNameField, 1, 1);
        gridPane.add(new Label("Level:"), 0, 2);
        gridPane.add(levelchoiceBox, 1, 2);

        gridPane.add(licenseCheckbox, 1, 3);
        gridPane.add(addMemberButton, 1, 4);

        //knappar
        HBox hBox = new HBox(10, addMemberButton, backButton);
        VBox vBox = new VBox(10, new Label("Skapa medlem:"), gridPane, hBox, errorLabel, new Separator(), new Label("Medlemmar"), memberListView);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 400, 300);
    }

    //Sökmedlem
    private Scene createSearchMemberScene(Stage stage) {

        Label titleLabel = new Label("Sök medlem");

        TextField idField = new TextField();
        idField.setPromptText("Skriv medlems ID");

        Button searchButton = new Button("Sök");
        Button backButton = new Button("Tillbaka");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Label resultLabel = new Label();

        TextArea history = new TextArea();
        history.setEditable(false);
        history.setPrefRowCount(10);

        searchButton.setOnAction(e -> {
            errorLabel.setText("");
            resultLabel.setText("");
            history.clear();

            String text = idField.getText().trim();

            if (!text.matches("\\d+")) {
                errorLabel.setText("ogiltigt ID");
                return;
            }

            int id = Integer.parseInt(text);

            Optional<Member> opt = memberRegistry.findById(id);
            if (opt.isEmpty()) {
                errorLabel.setText("Hittar ingen medlem med detta id");
                return;
            }

            Member member = opt.get();
            resultLabel.setText(member.toString());

            if (member.getHistory().isEmpty()) {
                history.setText("Hittar ingen historik");
            } else {
                String textHistory = member.getHistory().stream().collect(java.util.stream.Collectors.joining("\n"));
                history.setText(textHistory);
            }


        });
        backButton.setOnAction(e -> stage.setScene(createManageMemberMenuScene(stage)));

        VBox vBox = new VBox(10, titleLabel, idField, new HBox(10, searchButton, backButton), errorLabel, new Separator(), new Label("Resultat"), resultLabel, new Label("Historik"), history);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 400, 300);
    }

    // ändra Medlem
    private Scene createChangeMemberScene(Stage stage) {

        Label titleLabel = new Label("Ändra medlem");
        TextField idField = new TextField();
        idField.setPromptText("Skriv medlems ID");

        Button loadBtn = new Button("Hämta medlem");
        Button saveBtn = new Button("spara");
        Button backButton = new Button("Tillbaka");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Förnamn");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Efternamn");

        ChoiceBox<String> levelchoiceBox = new ChoiceBox<>();
        levelchoiceBox.getItems().addAll(StatusLevel.STANDARD, StatusLevel.STUDENT, StatusLevel.SENIOR);
        levelchoiceBox.setValue(StatusLevel.STANDARD);

        CheckBox licenseCheckbox = new CheckBox();

        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        //array för att kunna uppdater<
        final Member[] selectedMembers = new Member[1];

        //inaktivera fält
        firstNameField.setDisable(true);
        lastNameField.setDisable(true);
        levelchoiceBox.setDisable(true);
        licenseCheckbox.setDisable(true);
        saveBtn.setDisable(true);

        //hämta medlem
        loadBtn.setOnAction(e -> {
            error.setText("");

            String text = idField.getText().trim();
            if (!text.matches("\\d+")) {
                error.setText("Ogiltigt ID");
                return;
            }
            int id = Integer.parseInt(text);
            Optional<Member> opt = memberRegistry.findById(id);
            if (opt.isEmpty()) {
                error.setText("Hittar ingen medlem med detta id");
                return;
            }

            // spara vald medlem
            Member member = opt.get();
            selectedMembers[0] = member;

            firstNameField.setText(member.getFirstname());
            lastNameField.setText(member.getLastname());
            levelchoiceBox.setValue(member.getLevel());
            licenseCheckbox.setSelected(member.hasLicense());

            firstNameField.setDisable(false);
            lastNameField.setDisable(false);
            levelchoiceBox.setDisable(false);
            licenseCheckbox.setDisable(false);
            saveBtn.setDisable(false);

        });
//spara ändring
        saveBtn.setOnAction(e -> {
            error.setText("");

            Member member = selectedMembers[0];

            //säkerhetskontroll
            if (member == null) {
                error.setText("Hämta medlem först");
                return;
            }

            String firstname = firstNameField.getText().trim();
            String lastname = lastNameField.getText().trim();
            String level = levelchoiceBox.getValue().trim();

            if (firstname.isEmpty() || lastname.isEmpty()) {
                error.setText("Förnamn och efternamn behövs fyllas i");
                return;
            }

            // uppdatera via memberservice
            boolean bName = false;
            try {
                bName = memberService.updateName(member.getId(), firstname, lastname);
            } catch (InvalidMemberDataException ex) {
                throw new RuntimeException(ex);
            }
            boolean bLevel = false;
            try {
                bLevel = memberService.updateLevel(member.getId(), level);
            } catch (InvalidMemberDataException ex) {
                throw new RuntimeException(ex);
            }


            //Körkort
            member.setLicense(licenseCheckbox.isSelected());

            if (!bName || !bLevel) {
                error.setText("Funkade ej att uppdatera");
            }

            showInfo("Klart", "Medlemmen är nu uppdaterad: " + member.getId());

        });

        backButton.setOnAction(e -> stage.setScene(createManageMemberMenuScene(stage)));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(new Label("Medlems Id:"), 0, row);
        gridPane.add(idField, 1, row);
        gridPane.add(loadBtn, 2, row);
        row++;

        gridPane.add(new Label("Förnamn:"), 0, row);
        gridPane.add(firstNameField, 1, row);
        row++;

        gridPane.add(new Label("Efternamn:"), 0, row);
        gridPane.add(lastNameField, 2, row);
        row++;

        gridPane.add(new Label("Level:"), 0, row);
        gridPane.add(levelchoiceBox, 1, row);
        row++;

        gridPane.add(licenseCheckbox, 0, row);
        row++;

        HBox hBox = new HBox(10, saveBtn, backButton);

        VBox vBox = new VBox(10, titleLabel, gridPane, hBox, error);
        vBox.setPadding(new Insets(20));

        return new Scene(vBox);
    }

    // meny för föremål
    private Scene createItemMenuScene(Stage stage) {

        Label title = new Label("Föremål");

        Button addItemBtn = new Button("Lägg till föremål");
        Button listFilterBtn = new Button("Lista/filtrera föremål");
        Button backButton = new Button("Tillbaka");

        addItemBtn.setOnAction(e -> stage.setScene(createAddItemScene(stage)));
        listFilterBtn.setOnAction(e -> stage.setScene(createListFilterItemsScene(stage)));
        backButton.setOnAction(e -> stage.setScene(createMainMenuScene(stage)));

        VBox vBox = new VBox(10, title, addItemBtn, listFilterBtn, backButton);
        vBox.setPadding(new Insets(20));

        return new Scene(vBox, 400, 300);

    }

    //lägg till föremål
    private Scene createAddItemScene(Stage stage) {

        Label title = new Label("Lägg till föremål");

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Bil", "Elskooter");
        choiceBox.setValue("Bil");


        //gemensamma fält
        TextField idField = new TextField();
        idField.setPromptText("ID (Exempel B003 om bil, S003 om skooter");

        TextField nameField = new TextField();
        nameField.setPromptText("Namn");

        TextField priceField = new TextField();
        priceField.setPromptText("Pris per timme. (Exempel. 150)");

        //bil fält
        TextField modelField = new TextField();
        modelField.setPromptText("Märke");

        TextField colorField = new TextField();
        colorField.setPromptText("Färg");

        CheckBox licenseCheck = new CheckBox("kräver körkort");

        //Elskooter fält
        TextField scooterTypeField = new TextField();
        scooterTypeField.setPromptText("Typ av scooter (Exempel Elskooter)");

        TextField maxSpeedField = new TextField();
        maxSpeedField.setPromptText("Maxhastighet");

        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        Button saveBtn = new Button("Spara");
        Button backButton = new Button("Tillbaka");

        //aktiverar fält beroende på vilken item
        Runnable updateVisibleFields = () -> {
            boolean isCar = "Bil".equals(choiceBox.getValue());

            modelField.setDisable(!isCar);
            colorField.setDisable(!isCar);

            scooterTypeField.setDisable(isCar);
            maxSpeedField.setDisable(isCar);

        };

        updateVisibleFields.run();
        choiceBox.setOnAction(e -> updateVisibleFields.run());

        //spara item
        saveBtn.setOnAction(e -> {
            error.setText("");

            String id = idField.getText().trim().toUpperCase();
            String name = nameField.getText().trim().toUpperCase();

            if (id.isEmpty()) {
                error.setText("Saknas ID");
                return;

            }
            if (name.isEmpty()) {
                error.setText("Saknas namn");
                return;
            }
            if (priceField.getText().isEmpty()) {
                error.setText("Pris per timme saknas");
                return;
            }
            double price = Double.parseDouble(priceField.getText().trim());

            //spara subklass beroende på val i choice box
            if ("Bil".equals(choiceBox.getValue())) {
                String model = modelField.getText().trim();
                String color = colorField.getText().trim();

                if (model.isEmpty() || color.isEmpty()) {
                    error.setText("Märke och färg måste fyllas in");
                    return;
                }

                Bil car = new Bil(id, name, price, model, color, licenseCheck.isSelected());
                inventory.add(car);

                showInfo("Klart", "Bil är sparad: " + id);

            } else { //Elskooter
                String scooterType = scooterTypeField.getText().trim();
                if (scooterType.isEmpty()) {
                    error.setText("Typ av scooter måste fyllas in");
                    return;
                }
                if (maxSpeedField.getText().trim().isEmpty()) {
                    error.setText("Maxhastighet måste fyllas in");
                    return;
                }
                if (!maxSpeedField.getText().trim().matches("\\d+")) {
                    error.setText("Maxhastighet måste vara ett heltal");
                    return;
                }
                int maxSpeed = Integer.parseInt(maxSpeedField.getText().trim());

                Elskooter scooter = new Elskooter(id, name, price, scooterType, licenseCheck.isSelected(), maxSpeed);
                inventory.add(scooter);

                showInfo("Klart", "Elskooter är sparad: " + id);
            }

            //rensa fält
            idField.clear();
            nameField.clear();
            priceField.clear();
            modelField.clear();
            colorField.clear();
            scooterTypeField.clear();
            maxSpeedField.clear();
            licenseCheck.setSelected(false);

            //återställ val
            choiceBox.setValue("Bil");
            updateVisibleFields.run();


        });

        backButton.setOnAction(e -> stage.setScene(createItemMenuScene(stage)));

        //gridpane layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;

        gridPane.add(new Label("Typ: "), 0, row);
        gridPane.add(choiceBox, 1, row++);

        gridPane.add(new Label("ID:"), 0, row);
        gridPane.add(idField, 1, row++);

        gridPane.add(new Label("Name:"), 0, row);
        gridPane.add(nameField, 1, row++);

        gridPane.add(new Label("Price:"), 0, row);
        gridPane.add(priceField, 1, row++);

        gridPane.add(new Label("Model (bil):"), 0, row);
        gridPane.add(modelField, 1, row++);

        gridPane.add(new Label("Color: (bil)"), 0, row);
        gridPane.add(colorField, 1, row++);

        gridPane.add(new Label("Scooter Type:"), 0, row);
        gridPane.add(scooterTypeField, 1, row++);

        gridPane.add(new Label("Max Speed:"), 0, row);
        gridPane.add(maxSpeedField, 1, row++);

        gridPane.add(licenseCheck, 0, row++);

        HBox buttons = new HBox(10, saveBtn, backButton);

        VBox box = new VBox(10, title, gridPane, buttons, error);
        box.setPadding(new Insets(20));

        return new Scene(box, 600, 500);

    }

    //Lista/filtrera föremål
    private Scene createListFilterItemsScene(Stage stage) {

        Label title = new Label("Lista/Filter föremål");

        ListView<String> listView = new ListView<>();
        Label infoLabel = new Label();

        //Filter
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll("Alla", "Bil", "Elskooter");
        typeChoice.setValue("Alla");

        Button applyBtn = new Button("Filtrera");
        Button clearBtn = new Button("Rensa");
        Button backButton = new Button("Tillbaka");

        Runnable refresh = () -> {
            listView.getItems().clear();

            String type = typeChoice.getValue();

            //Stream för att iterera över invetory listan
            List<String> rows = inventory.findAll().stream()
                    .filter(item ->
                            "Alla".equals(type) ||
                                    ("Bil".equals(type) && item instanceof Bil) ||
                                    ("Elskooter".equals(type) && item instanceof Elskooter))
                    .map(item -> "Id: " + item.itemId()
                            + " Namn: " + item.getItemName()
                            + " Pris: " + item.getPrice()
                            + " Krävs körkort: " + (item.requiresLicense() ? "Ja" : "Nej"))
                    .toList();
// lägg rader i listan
            listView.getItems().addAll(rows);

            //träffar
            infoLabel.setText("Visar " + rows.size() + " föremål");
        };


        refresh.run();

        applyBtn.setOnAction(e -> refresh.run());

        //här rensar vi filter
        clearBtn.setOnAction(e -> {
            typeChoice.setValue("Alla");
            refresh.run();
        });

        backButton.setOnAction(e -> stage.setScene(createItemMenuScene(stage)));

        HBox filters = new HBox(10, new Label("Type: "), typeChoice, applyBtn, clearBtn, backButton);

        VBox box = new VBox(10, title, filters, infoLabel, listView);
        box.setPadding(new Insets(20));
        return new Scene(box, 600, 500);
    }


    //metoder för skriva ut information ->>>>>


    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //MEny för uthyrning
    private Scene createRentalMenuScene(Stage stage) {

        Label title = new Label("Rental Menu");

        Button bookBtn = new Button("Boka");
        Button finsishBtn = new Button("Avsluta");
        Button backButton = new Button("Tillbaka");

        bookBtn.setOnAction(e -> stage.setScene(createBookRentalMenuScene(stage)));
        finsishBtn.setOnAction(e -> stage.setScene(createFinishRentalMenuScene(stage)));
        backButton.setOnAction(e -> stage.setScene(createMainMenuScene(stage)));

        VBox vBox = new VBox(10, bookBtn, finsishBtn, backButton);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 600, 500);
    }

    //Boka uthyrning
    private Scene createBookRentalMenuScene(Stage stage) {
        Label title = new Label("Boka uthyrning");
        TextField memberIdField = new TextField();
        memberIdField.setPromptText("Medlems ID");

        ChoiceBox<Item> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(inventory.findAll());

        //väljer det första item som finns automatiskt
        if (!choiceBox.getItems().isEmpty()) {
            choiceBox.setValue(choiceBox.getItems().get(0));
        }

        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Item item) {
                if (item == null) return "";
                return item.itemId() + " " + item.getItemName() + " " + item.getPrice() + " kr";
            }

            @Override
            public Item fromString(String string) {
                return null;
            }
        });


        DatePicker endDatePicker = new DatePicker(); //här väljs slutdatum

        Label error = new Label(); //felmedelande
        error.setStyle("-fx-text-fill: red");

        Button bookBtn = new Button("Boka");
        Button backButton = new Button("Tillbaka");

        bookBtn.setOnAction(event -> { //boka
            try {
                String memberText = memberIdField.getText().trim();
                if (!memberText.matches("\\d+")) {
                    error.setText("Ogiltigt medlems-ID (bara siffror).");
                    return;
                }
                int memberId = Integer.parseInt(memberText);

                Item item = choiceBox.getValue(); //det valda item

                if (item == null) {
                    error.setText("Fälten behövs fyllas in");
                    return;
                }

                //ett slutdatum måste väljas
                if (endDatePicker.getValue() == null) {
                    error.setText("Välj ett slutdatum.");
                    return;
                }

                Rental rental = rentalService.book(
                        memberId, item.itemId(), endDatePicker.getValue()
                );

                showInfo("klart", "Uthyrning är nu skapas" + rental.getRentalId());
            } catch (Exception ex) {
                error.setText(ex.getMessage());
            }
        });
        backButton.setOnAction(event -> stage.setScene(createRentalMenuScene(stage)));

        VBox vBox = new VBox(10, title, memberIdField, choiceBox, endDatePicker, bookBtn, backButton, error);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 600, 500);
    }

    //Avsluta uthyrning
    private Scene createFinishRentalMenuScene(Stage stage) {
        Label title = new Label("Avsluta uthyrning");
        TextField rentalIdField = new TextField();
        rentalIdField.setPromptText("Uthyrnings ID");

        Label error = new Label();
        error.setStyle("-fx-text-fill: red");

        Button finishBtn = new Button("Avsluta");
        Button backButton = new Button("Tillbaka");

        //avsluta
        finishBtn.setOnAction(event -> {
            try {
                int rentalId = Integer.parseInt(rentalIdField.getText().trim());
                rentalService.finishRental(rentalId);
                showInfo("klart", "uthyrning är avslutad");
            } catch (Exception ex) {
                error.setText(ex.getMessage());
            }
        });
        backButton.setOnAction(event -> stage.setScene(createRentalMenuScene(stage)));

        VBox vBox = new VBox(10, title, rentalIdField, finishBtn, backButton, error);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 600, 500);

    }

    //summera intäkter
    private Scene createSumScene(Stage stage) {

        Label title = new Label("Sumera intäkter");

        Label totalLabel = new Label();

        Button refreshBtn = new Button("Uppdatera");
        Button backBtn = new Button("Tillbaka");

        //amvänder mig av stream
        Runnable refresh = () -> {
            double sum = rentalService.getAllRentalals().stream()
                    .mapToDouble(Rental::getTotalPrice)
                    .sum();

            totalLabel.setText("Totala intäkter: " + sum + " kr");
        };


        refresh.run(); //visa summan när scen öppnas

        refreshBtn.setOnAction(event -> refresh.run());
        backBtn.setOnAction(event -> stage.setScene(createMainMenuScene(stage)));

        HBox actions = new HBox(10, refreshBtn, backBtn);

        VBox vBox = new VBox(10, title, totalLabel, actions);
        vBox.setPadding(new Insets(20));
        return new Scene(vBox, 600, 500);
    }

    ;


    public static void main(String[] args) {
        launch(args);
    }


}

