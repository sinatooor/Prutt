package model;

/**
 * Placeholder-klass för Command Pattern.
 *
 * Denna klass används INTE i den nuvarande implementationen som använder
 * en Stack av GameState-objekt för att hantera Ångra-funktionen.
 *
 * Om man istället skulle använda Command Pattern, skulle denna klass (eller subklasser)
 * representera ett specifikt drag (t.ex. flytta vänster). Den skulle innehålla
 * logik för att både utföra (`execute`) och ångra (`undo`) draget, kanske genom
 * att spara GameState *innan* draget utfördes.
 *
 * Exempel på fält och metoder:
 * private Board board;         // Referens till brädet som kommandot påverkar
 * private Direction direction; // Vilket drag som ska göras
 * private GameState stateBefore; // Brädets tillstånd *innan* execute()
 *
 * public void execute() {
 * stateBefore = board.getState(); // Spara tillstånd innan
 * board.move(direction);
 * board.addRandomTile(); // Osv...
 * }
 *
 * public void undo() {
 * board.setState(stateBefore); // Återställ till sparat tillstånd
 * }
 *
 * Fördelar med Command Pattern kan vara bättre separation av ansvar och
 * möjlighet att enkelt logga/spara/köa kommandon, men det är mer komplext
 * än att bara spara hela GameState.
 */
public class MoveCommand {
    // Tom klass i denna implementation.
}