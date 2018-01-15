package org.miage.placesearcher;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Baptiste on 15/01/2018.
 */

public class EventBusManager {
    public static Bus BUS = new Bus(ThreadEnforcer.ANY);
}
