package com.Controller.services.impl;

import com.Controller.services.RateService;
import com.Models.ActiveRate;
import com.Models.Rate;

import java.util.List;

public class RateServiceImplementation implements RateService {
    @Override
    public double getRate() {
        return 0;
    }

    @Override
    public void addRate() {

    }

    @Override
    public void deactivateRate() {

    }

    @Override
    public Rate save(Rate entity) {
        return null;
    }

    @Override
    public Rate update(Rate entity) {
        return null;
    }

    @Override
    public void delete(Rate entity) {

    }

    @Override
    public ActiveRate save(ActiveRate entity) {
        return null;
    }

    @Override
    public ActiveRate update(ActiveRate entity) {
        return null;
    }

    @Override
    public void delete(ActiveRate entity) {

    }

    @Override
    public void delete(Long id) {

    }


    @Override
    public void deleteInBatch(List<ActiveRate> entities) {

    }

    @Override
    public ActiveRate find(Long id) {
        return null;
    }

    @Override
    public List<ActiveRate> findAll() {
        return null;
    }
}
