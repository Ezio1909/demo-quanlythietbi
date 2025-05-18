package quanlythietbi.ui.components;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateTimePicker extends JPanel {
    private final JDateChooser dateChooser;
    private final JSpinner timeSpinner;

    public DateTimePicker() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Initialize date chooser
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(120, 30));
        dateChooser.setDateFormatString("yyyy-MM-dd");

        // Initialize time spinner
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(70, 30));

        // Add components
        add(dateChooser);
        add(timeSpinner);
    }

    public void setValue(LocalDateTime dateTime) {
        if (dateTime != null) {
            dateChooser.setDate(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
            timeSpinner.setValue(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            dateChooser.setDate(null);
            timeSpinner.setValue(new Date());
        }
    }

    public LocalDateTime getValue() {
        Date date = dateChooser.getDate();
        if (date == null) {
            return null;
        }

        Date time = (Date) timeSpinner.getValue();
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));

        return LocalDateTime.ofInstant(dateCal.toInstant(), ZoneId.systemDefault());
    }
} 