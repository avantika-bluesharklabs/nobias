package com.nobias.businesslogic.rx;

import io.reactivex.Scheduler;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
