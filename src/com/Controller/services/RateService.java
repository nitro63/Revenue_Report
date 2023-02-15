package com.Controller.services;

import com.Models.ActiveRate;
import com.Models.Rate;

import java.util.UUID;

public interface RateService extends GenericService<ActiveRate> {

double getRate ();

void addRate();

void deactivateRate();


    Rate save(Rate entity);

    Rate update(Rate entity);

    void delete(Rate entity);
}
