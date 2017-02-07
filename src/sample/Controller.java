package sample;

import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Nishant Sharma on 9/27/2016.
 */
public class Controller {
  //  Main obj=new Main();
    int arr[][];
    String check = "";
    String test = "";
    String correct = "";

    /* public static void main(String[] args) throws IOException {
          file obj = new file();
          try {
              //long t=System.currentTimeMillis();
           //   obj.auto();
              //System.out.println(System.currentTimeMillis()-t);
          }
          catch(ArrayIndexOutOfBoundsException ae)
          {
              System.out.println("haha");
          }
      }*/
    static int min(int x, int y, int z) {
        if (x < y && x < z) return x;
        if (y < x && y < z) return y;
        else return z;
    }

    static int editDistDP(String str1, String str2, int m, int n) {
        // Create a table to store results of subproblems
        int dp[][] = new int[m + 1][n + 1];

        // Fill d[][] in bottom up manner
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                // If first string is empty, only option is to
                // isnert all characters of second string
                if (i == 0)
                    dp[i][j] = j;  // Min. operations = j

                    // If second string is empty, only option is to
                    // remove all characters of second string
                else if (j == 0)
                    dp[i][j] = i; // Min. operations = i

                    // If last characters are same, ignore last char
                    // and recur for remaining string
                else if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];

                    // If last character are different, consider all
                    // possibilities and find minimum
                else
                    dp[i][j] = 1 + min(dp[i][j - 1],  // Insert
                            dp[i - 1][j],  // Remove
                            dp[i - 1][j - 1]); // Replace
            }
        }

        return dp[m][n];
    }


    public Vector<Button> auto(String check) throws IOException {
        int temp, min = 100;
       File file1 = new File("words.txt");
        BufferedReader br = new BufferedReader(new FileReader(file1));
        //  tempo obj = new tempo();
        // Scanner sc = new Scanner(System.in);
        //  check = sc.next();
        Vector<Button> list = new Vector<>();
        int len = check.length();
        while ((test = br.readLine()) != null) {
            if (Math.abs(test.length() - len) < 2) {
                try {
                    temp = editDistDP(check, test, check.length(), test.length());
                    // temp=edit_distance(check,test);
                    if (temp == min && !(test.equalsIgnoreCase(check))) {
                        Button b = new Button(test);

                        list.add(b);
                    }
                    if (temp < min && !(test.equalsIgnoreCase(check))) {
                        Button b = new Button(test);
                        min = temp;
                        list.clear();
                        list.add(b);
                    }
                } catch (ArrayIndexOutOfBoundsException ae) {
                    System.out.println(test);
                }
            }
        }

        return list;


    }


}