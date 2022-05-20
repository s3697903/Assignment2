package Interfaces;

import Helpers.TiReceipt;
import Models.Journey;

/**
 * The travel view model interface
 */
public interface ITravelViewModel {
    TiReceipt startNewJourney(Journey journey);
    String toString();
}
