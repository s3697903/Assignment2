package main.java.interfaces;

import main.java.helpers.TiReceipt;
import main.java.models.Journey;

/**
 * The travel view model interface
 */
public interface ITravelViewModel {
    TiReceipt startNewJourney(Journey journey);
    String toString();
}
