package utils;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.Plot;
import com.googlecode.charts4j.Plots;




public class StockGrapher {

 
    public static void main(String[] args) throws IOException {
    	
    	ExcelStockParser esp = new ExcelStockParser();
    	
    	Data d = new Data();
    	
    	
        Plot plot = Plots.newPlot(Data.newData(0, 66.6, 33.3, 100));
        LineChart chart = GCharts.newLineChart(plot);
        displayUrlString(chart.toURLString());
    }

    private static void displayUrlString(final String urlString) throws IOException{
        JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(urlString))));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
