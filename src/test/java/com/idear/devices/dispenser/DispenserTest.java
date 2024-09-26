package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.ErrorParsingDispenserStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Scanner;

import static com.idear.devices.dispenser.command.MoveCardCommand.Position.*;
import static com.idear.devices.dispenser.command.SetDispenserModeCommand.DispenserMode.*;

class DispenserTest {
    private SyncotekDispenser dispenser;

    @Test
    void TestDispenserFunctions() throws DispenserException {
        dispenser = new SyncotekDispenser("COM5");
        do {
            try {
                showMenu();
                Scanner in = new Scanner(System.in);
                int option = Integer.valueOf(in.nextLine());
                evaluateOption(option);
            } catch (InterruptedException | NumberFormatException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                //dispensadorTest.dispenser.close();
            }
        } while (true);
    }

    private final int RESET = 1;
    private final int MOVE_CARD_WITHOUT_HOLDING = 2;
    private final int MOVE_CARD_HOLDING = 3;
    private final int MOVE_CARD_READ_POSITION = 4;
    private final int CAPTURE_CARD_MODE = 5;
    private final int PROHIBITED = 6;
    private final int MODE_CARD_TO_CAPTURE_BOX = 7;
    private final int MODE_CARD_TO_READ_POSITION = 8;
    private final int GET_ENTRY_MODE = 9;
    private final int GET_STATUS_FLAGS = 10;
    private final int EXIT = 11;

    private void showMenu() {
        System.out.println("***********  MENU DE DISPENSADOR **************");
        System.out.println(RESET + "   Reset");
        System.out.println(MOVE_CARD_WITHOUT_HOLDING + "   Move card to front without holding");
        System.out.println(MOVE_CARD_HOLDING + "   Move card to front with holding");
        System.out.println(MOVE_CARD_READ_POSITION + "   Move card to read position");
        System.out.println(CAPTURE_CARD_MODE + "   Capture card");
        System.out.println(PROHIBITED + "   Prohibited");
        System.out.println(MODE_CARD_TO_CAPTURE_BOX + "   Mode card to captured box");
        System.out.println(MODE_CARD_TO_READ_POSITION + "   Mode card to read position ");
        System.out.println(GET_ENTRY_MODE + "   Get entry mode");
        System.out.println(GET_STATUS_FLAGS + "  Get status flags");
        System.out.println(EXIT + "  Exit");

        System.out.print("Elije una opción: ");
    }

    private void evaluateOption(int option) throws InterruptedException {
        try {
            switch (option) {
                case RESET:
                    dispenser.reset();
                    showResult("SUCCESS");
                    break;
                case MOVE_CARD_WITHOUT_HOLDING:
                    dispenser.moveCard(FRONT_WITHOUT_HOLDING_CARD);
                    showResult("SUCCESS");
                    break;
                case MOVE_CARD_HOLDING:
                    dispenser.moveCard(FRONT_HOLDING_CARD);
                    showResult("SUCCESS");
                    break;
                case MOVE_CARD_READ_POSITION:
                    dispenser.moveCard(READ_WRITE_SCAN);
                    showResult("SUCCESS");
                    break;
                case CAPTURE_CARD_MODE:
                    dispenser.captureCard();
                    showResult("SUCCESS");
                    break;
                case PROHIBITED:
                    dispenser.setSetDispenserMode(DISABLE);
                    showResult("SUCCESS");
                    break;
                case MODE_CARD_TO_CAPTURE_BOX:
                    dispenser.setSetDispenserMode(CAPTURE_CARD);
                    showResult("SUCCESS");
                    break;
                case MODE_CARD_TO_READ_POSITION:
                    dispenser.setSetDispenserMode(READ_WRITE_CARD);
                    showResult("SUCCESS");
                case GET_ENTRY_MODE:
                    showResult(dispenser.getDispenserMode().name());
                    break;
                case GET_STATUS_FLAGS:
                    showResult(getStatusFlags());
                    break;
                case EXIT:
                    System.exit(1);
            }
        } catch (DispenserException exception) {
            showResult(exception.getMessage());
        }

    }

    private ArrayList<String> getStatusFlags() throws  DispenserException {

        DispenserStatus dispenserStatus = new DispenserStatus();
        try {
            dispenserStatus = dispenser.getStatus();
        } catch (ErrorParsingDispenserStatus e) {
            System.out.println("Error parsing status: " + e.getMessage());
        }
        ArrayList<String> result = new ArrayList<>();
        result.add("Capture card box full " + (dispenserStatus.isCaptureCardBoxFull() ? "[O]" : "[X]"));
        result.add("Dispensing card " + (dispenserStatus.isDispensingCard() ? "[O]" : "[X]"));
        result.add("Capturing card " + (dispenserStatus.isCapturingCard() ? "[O]" : "[X]"));
        result.add("Dispense error " + (dispenserStatus.isDispenseError() ? "[O]" : "[X]"));
        result.add("Capture error " + (dispenserStatus.isCaptureError() ? "[O]" : "[X]"));
        result.add("Card overlapped " + (dispenserStatus.isCardOverlapped() ? "[O]" : "[X]"));
        result.add("Card jammed " + (dispenserStatus.isCardJammed() ? "[O]" : "[X]"));
        result.add("Card pre-empty " + (dispenserStatus.isCardPreEmpty() ? "[O]" : "[X]"));
        result.add("Card stacker empty " + (dispenserStatus.isCardStackerEmpty() ? "[O]" : "[X]"));
        result.add("Sensor status 3 " + (dispenserStatus.isSensorThreeActive() ? "[O]" : "[X]"));
        result.add("Sensor status 2 " + (dispenserStatus.isSensorTwoActive() ? "[O]" : "[X]"));
        result.add("Sensor status 1 " + (dispenserStatus.isSensorOneActive() ? "[O]" : "[X]"));
        return result;

    }

    private void showResult(String result) {
        System.out.println("-----------------------------");
        System.out.println("");
        System.out.println("RESULT ");
        System.out.println(result);
        System.out.println("");
        System.out.println("------------------------------");
    }

    private void showResult(ArrayList<String> result) {
        System.out.println("-------------------------------");
        System.out.println("");
        System.out.println("RESULT ");
        result.forEach(System.out::println);
        System.out.println("");
        System.out.println("--------------------------------");
    }

}