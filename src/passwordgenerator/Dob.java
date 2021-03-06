package passwordgenerator;

/**
 * Created by t848 on 11-Jan-16.
 */
public class Dob {

    // Main date of birth password generation algorithm
    public String getPassword(int Date,int Month, int Year, int choiceDate, int choiceMonth, int choiceYear) {
        int mixSelection;
        String d,m,y,pass;
        
        // Switching object
        Switch sw = new Switch();

        // Converting all the values
        d = sw.switcher(choiceDate,Date, null);
        m = sw.switcher(choiceMonth,Month , null);
        y = sw.switcher(choiceYear,Year, null);

        // Choosing mixer variable
        mixSelection = mixSelector(Date, Month, Year);

        // Mixing the generated values and concatting them into a password
        pass = mixer(d, m, y, mixSelection);

        return pass;
    }
    
    // Simple method to select a mixing digit 
    private int mixSelector(int Date, int Month, int Year) {
        int sum, pick;

        sum = Date + Month + Year;
        pick = sum % 1000;

        // Loops till it gets a number under 6 or 6 itself
        while (pick > 6)
            pick = pick / 2;

        return pick;
    }
    

    // mixer method to mix dob 
    private String mixer(String d,String m,String y,int ch)
    {
        String password;

        //Mixing the DOB
        switch  (ch)
        {
            case 1: password = d+m+y;
                break;

            case 2: password = d+y+m;
                break;

            case 3 : password = m+d+y;
                break;

            case 4 : password = m+y+d;
                break;

            case 5: password = y+d+m;
                break;

            case 6: password = y+m+d;
                break;

            default: password = d+m+y;
                System.out.println("wrong input so choice 1 ");
                break;

        }

        return password;
    }
}
