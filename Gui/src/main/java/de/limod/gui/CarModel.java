/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.limod.gui;

import de.limod.portals.Car;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dominic
 */
public class CarModel extends AbstractTableModel {

    private List<Car> cars = new ArrayList<>();
    private final String[] columnNames = {"ID", "Titel", "Preis",
        "Online seit", "Anbieter"};

    public CarModel(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car c = cars.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getId();
            case 1:
                return c.getTitle();
            case 2:
                return c.getPrice();
            case 3:
                return c.getCreated();
            case 4:
                return c.getProvider();
//            case 5:
//                return c.getUrl();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return super.getColumnClass(i); //To change body of generated methods, choose Tools | Templates.
    }

    public Car getRow(int i){
        return this.getCars().get(i);
        
    }
    public Color getRowColour(int row) {
        Car c = this.getCars().get(row);
//        return null;
        return c.getIsNew() ? Color.GREEN : Color.WHITE;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car c) {
        this.cars.add(c);
    }
    public void addCar(int index,Car c) {
        this.cars.add(index,c);
    }

}
