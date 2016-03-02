/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.limod.gui;

import de.limod.portals.Car;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Class to highlight new results in the table
 *
 * @author dominic
 */
public class CarCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        CarModel model = (CarModel) table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Car car1 = model.getRow(row);
        if(car1.getIsNew()){
            c.setBackground(Color.GREEN);
            c.setForeground(Color.GRAY);
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }
        if (isSelected) {
            c.setForeground(Color.DARK_GRAY);
        } else {
             c.setForeground(table.getForeground());
        }
//        return super.getTableCellRendererComponent(table, value, hasFocus, hasFocus, row, column);
        return c;
    }

}
