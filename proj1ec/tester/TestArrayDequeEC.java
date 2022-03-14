package tester;

import static org.junit.Assert.*;

import org.junit.Test;
import student.StudentArrayDeque;


public class TestArrayDequeEC {
    double randomNumber;
    StringBuilder allMessage = new StringBuilder();
    StudentArrayDeque<Integer> buggyAd1 = new StudentArrayDeque<Integer>();
    ArrayDequeSolution<Integer> correctAd1 = new ArrayDequeSolution<Integer>();

    @Test
    public void ArrayAddTest() {
        for (int i = 0; i < 999999; i++) {
            randomNumber = (Math.random() * 4) + 1;
            //generate a number between 0 and 1;
            if (randomNumber <= 1) {
                buggyAd1.addLast(i);
                correctAd1.addLast(i);
                allMessage.append("addLast(" + i + ")\n");
                assertEquals(allMessage.toString(),
                        buggyAd1.get(buggyAd1.size() - 1), correctAd1.get(correctAd1.size() - 1));
            } else if (randomNumber <= 2) {
                buggyAd1.addFirst(i);
                correctAd1.addFirst(i);
                allMessage.append("addFirst(" + i + ")\n");
                assertEquals(allMessage.toString(),
                        buggyAd1.get(0), correctAd1.get(0));
            } else if (!buggyAd1.isEmpty() && !correctAd1.isEmpty()
                    && randomNumber <= 4) {
                Integer bRemoveLastValue = buggyAd1.removeLast();
                Integer cRemoveLastValue = correctAd1.removeLast();
                allMessage.append("removeLast()\n");
                assertEquals(allMessage.toString(), bRemoveLastValue, cRemoveLastValue);
            } else if (!buggyAd1.isEmpty() && !correctAd1.isEmpty()
                    && randomNumber <= 3) {
                Integer bRemoveFirstValue = buggyAd1.removeFirst();
                Integer cRemoveFirstValue = correctAd1.removeFirst();
                allMessage.append("removeFirst()\n");
                assertEquals(allMessage.toString(),
                        bRemoveFirstValue, cRemoveFirstValue);
            }

        }
    }

}