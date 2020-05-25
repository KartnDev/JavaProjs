package com.KartonDCP.DatabaseWorker.Mapper;

import org.modelmapper.ModelMapper;

import java.util.LinkedHashSet;

public class Mapper {

    private LinkedHashSet<Object> queueToMap;
    private ModelMapper modelMapper;
    public Mapper(){
        modelMapper = new ModelMapper();
    }

    public boolean mapQueue(){
        if (queueToMap.size() > 0) {
            for (var item : queueToMap) {
                modelMapper.map(item, item.getClass());
            }
            return true;
        }
        return false;
    }

    public void addToMap(Object item){
        queueToMap.add(item);
    }

}
