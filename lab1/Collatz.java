/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {
    public static void main(String[] args) {
        int n = 5;
        System.out.print(n+ " "); // print out the first number before print the rest*/
        nextNumber(n); //* call the nextNumber method and pass in the starting variable n */
    }

    public static int nextNumber(int n){
        int nextN = 0; //* create a int variable to track the next number */

        while(nextN != 1){ //* when the next number is 1 then the while loop stop*/
            if(n % 2 == 0){ //* check to see if the number is even */
                n = n / 2; //* if is even then divided by 2 */
                System.out.print(n + " "); //* print it out */
                nextN = n; //* update the next number to the new number */
            }else { //* check to see if the number is odd */
                n = 3 * n + 1; //* if it is odd then multiply by 3 then add 1 */
                System.out.print(n + " "); //* print it out */
                nextN = n; //* update the next number to the new number */
            }
        }
        return nextN;//* return the next number */
    }
}

