package passwordgenerator;/*
 * @author T848
 */

import pojo.Password;
import pojo.Procedure;
import pojo.User;

import java.util.Calendar;

public class Passgen {

    // Format and range specified for pin and unique identifier
    private final int LENGTH_UID = 15;
    private final int MIN_RANGE_UID = 0;
    private final int MAX_RANGE_UID = 15;
    private final int MAX_RANGE_PIN = 4;
    private final int MIN_RANGE_PIN = 0;

    // Getting all the objects
    
    // Date of birth object for generating password using date of birth
    Dob dob = new Dob();
    // Password checking class
    Check check = new Check();
    // Generation of password using Name
    Name name = new Name();
    // Password decryptor
    Decrypt decrypter = new Decrypt();
    // Encryption class duh
    Encrypt encrypt = new Encrypt();

   // Storing data being used for creation of password
    private int USER_DATE;
    private int USER_MONTH;
    private int USER_YEAR;
    private int USER_CHOICE_DATE = 1;
    private int USER_CHOICE_MONTH = 2;
    private int USER_CHOICE_YEAR = 4;
    private String USER_FIRST;
    private String USER_SECOND;
    private int USER_CHOICE_1 = 1;
    private int USER_CHOICE_2 = 4;
    private int sumOfFirstName;
    private int sumOfSecondName;
    private String USER_UNIQUE_MODIFIER;

    // Data to be used by Decrypt and Encrypt
     static String PARSED_USER_ID;
     static int USER_PIN;

    // Items to be sent back
    private String encryptedPassword;
    private String Password;

    // Dycrypting the password
    private void decrypt() {


        decrypter.reverseDOB(new StringBuilder(encryptedPassword));

        DecrypterUpdateValues();

        generateUsingDob();

    }

    private void DecrypterUpdateValues() {

        USER_DATE = decrypter.getDate();
        USER_MONTH = decrypter.getMonth();
        USER_YEAR = decrypter.getYear();
        USER_CHOICE_DATE = decrypter.getColor1();
        USER_CHOICE_MONTH = decrypter.getColor2();
        USER_CHOICE_YEAR = decrypter.getColor3();

    }

    // if the strength of passwords is equal try changing dob of user to get different passwords
    private void increaseDob() {

        if ((USER_DATE <= 99) && (USER_MONTH <= 99)) {

            USER_DATE++;
            USER_MONTH++;
            USER_CHOICE_YEAR++;

        }

    }

    // Process to generate password using date of birth of the user
    private String generateUsingDob() {
        String passwordDob;

        // Create password using DOB
        passwordDob = dob.getPassword(USER_DATE, USER_MONTH, USER_YEAR, USER_CHOICE_DATE, USER_CHOICE_MONTH,
                USER_CHOICE_YEAR);


        return passwordDob;

    }


    // Choosing the Best password generated amonsgt both of them
    private String pickBestOne(int strengthDob,int strengthName,String name, String dob){
        String temp = null;

        // if strength of name is greater than strength of DOB
        if (strengthName > strengthDob) { // encrypt
            temp = encrypt.encryptName(name, sumOfFirstName, sumOfSecondName, USER_CHOICE_1, USER_CHOICE_2,
                    USER_PIN, PARSED_USER_ID);
            Password = name;
        }

        // Update values till Dob password is better 
        else if (strengthName == strengthDob && strengthName > 50) {
            increaseDob();
            generatePassword();
        } 
        
        // Default case just encrypt DOB.
        else { 
            temp = encrypt.encryptDOB(dob, USER_DATE, USER_MONTH, USER_YEAR, USER_CHOICE_DATE,
                    USER_CHOICE_MONTH, USER_CHOICE_YEAR, USER_PIN, PARSED_USER_ID);
            Password = dob;
        }

        return temp;
    }

    private void updateValues(User info, String service) {

        // Calender Object to translate Date of birth
        Calendar calObject = Calendar.getInstance();
        calObject.setTimeInMillis(info.getBirthdate());

        // Update Values
        USER_DATE = calObject.get(Calendar.DAY_OF_MONTH);
        System.out.println("date = " + USER_DATE);
        USER_MONTH = calObject.get(Calendar.MONTH);
        System.out.println("month = " + USER_MONTH);
        USER_YEAR = calObject.get(Calendar.YEAR);
        System.out.println(USER_YEAR);
        USER_UNIQUE_MODIFIER = info.getUserID();
        //USER_CHOICE_DATE = decrypter.getColor1();
        //USER_CHOICE_MONTH = decrypter.getColor2();
        //USER_CHOICE_YEAR = decrypter.getColor3();
        USER_FIRST = info.getFirstName();
        USER_SECOND = info.getLastName();

    }

    // Process of generating the password using Name of the user
    private String generateUsingName() {
        String passwordName;

        // Create password using Name
        passwordName = name.getPassword(USER_FIRST, USER_SECOND, USER_CHOICE_1, USER_CHOICE_2);

        sumOfFirstName = name.getSumOfFirstName();
        sumOfSecondName = name.getSumOfSecondName();

        System.out.println("sum of first number in Passgen = " + sumOfFirstName);
        System.out.println("sum of second number in Passgen = " + sumOfSecondName);

        return passwordName;
    }

    // Parsing unique id of the user to pin and IMEI
    private void uniqueIdParser() {
        /*
        *we need to do some string manipulationhence stringbuilders
        */
        StringBuilder idReceived = new StringBuilder(USER_UNIQUE_MODIFIER);
        StringBuilder uniqueIdentifier = new StringBuilder();

        // Taking out all the Digits from the string
        for (int i = 0; i < idReceived.length(); i++)
            if (Character.isDigit(idReceived.charAt(i)))
                uniqueIdentifier.append(idReceived.charAt(i));

        // if the length is shorter than 15
        // Then add padding as digit 6 
        if (uniqueIdentifier.length() < LENGTH_UID) {
            while (uniqueIdentifier.length() < LENGTH_UID)
                uniqueIdentifier.append("6");
            PARSED_USER_ID = new String(uniqueIdentifier);
        } 
        // if length of parsed string is more than the maximum acceptable length
        // Then take out the substring by using range provided

        else if (uniqueIdentifier.length() > LENGTH_UID) {
            PARSED_USER_ID = uniqueIdentifier.substring(MIN_RANGE_UID, MAX_RANGE_UID);
        } 
        
        // If there is no problem then just use the parsed string as it is.
        else
            PARSED_USER_ID = new String(uniqueIdentifier);

        // Set PIN and UniqueIdentfier
        USER_PIN = Integer.valueOf(uniqueIdentifier.substring(MIN_RANGE_PIN, MAX_RANGE_PIN));

        // test print remove before deployment 
        System.out.println("ID Parsed = " + uniqueIdentifier);
        System.out.println("pin Parsed = " + USER_PIN);

    }

    // Process to generate password
    private void generatePassword() {

        // variables
        String name, dob;
         int strengthName, strengthDob;

        // Generate  password using Dob
        dob = generateUsingDob();

        // Generate using name
        //name = generateUsingName();
        name = "disabled name for now";

        // Check strength of both
        strengthName = check.strength(name);
        strengthDob = check.strength(dob);

        // Using the Strength pick the best password to be used 
        encryptedPassword = pickBestOne(strengthDob,strengthName,name,dob);
        
        // Display
        System.out.println("Strength of password dob = " + strengthDob);
        System.out.println("Strength of password name = " + strengthName);

    }

    public Password decryptPassword(Procedure procedure) {
       
        // parse the acquired user ID
        uniqueIdParser();

        // set Encrypted password
        encryptedPassword = procedure.getProcedure();

        // Decrypt values before generating
        decrypt();

        //Generate pojo.User pojo.Password
        Password = generateUsingDob();

        Password password = new Password();

        password.setPassword(this.Password);
        password.setOwner(procedure.getOwner());
        password.setId(procedure.getId());
        password.setServiceName(procedure.getServiceName());

        return password;




    }


    public Procedure encryptPassword(User userInfo, String service) {// MAIN FUNCTION
        // Update values
        updateValues(userInfo, service);
        // parse the acquired user ID
        uniqueIdParser();
        // pojo.Procedure to generate password for Dob
        generatePassword();

        Procedure procedure = new Procedure();
        procedure.setServiceName(service);
        procedure.setOwner(userInfo.getUserID());
        procedure.setProcedure(this.encryptedPassword);

        // Flushing values
        PARSED_USER_ID = null;
        USER_PIN = 0;

        return procedure;
    }

        /*else if(a[0].equals("3")){
        
            third.namePlusDob();
            System.out.println("\n your password is " + third.pass);// DISPLAYING GENERATED PASSWORD
            int a = check.strength(third.pass);//CHECKING FOR PASSWORD STRENGTH
            System.out.println(""+a);
        
        }*/

        public String getUserID(){
            return PARSED_USER_ID;
    }
        public int getPin(){
            return USER_PIN;
    }

}
