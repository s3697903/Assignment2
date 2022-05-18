package Interfaces;

import Helpers.TiReceipt;
import Models.Journey;

public interface ITravelViewModel {
    TiReceipt startNewJourney(Journey journey);
    String toString();
}
