import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import scala.collection.mutable.ListBuffer;

import javax.swing.BorderFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;



class LineChartEx extends JFrame {

    public LineChartEx(scala.collection.immutable.List list, int month) {

        initUI(list, month);
    }

    private void initUI(scala.collection.immutable.List list, int month) {

        XYDataset dataset = createDataset(list, month);
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset(scala.collection.immutable.List list, int month) {

        XYSeries series = new XYSeries("Цена токена");
        for(int i = 1; i<=month; i++) {
        	series.add(i, (double)list.apply(i-1));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Поведение цены токена",
                "Количество месяцев",
                "Цена токена",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Поведение цены токена",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }

}
public class Interface {

	private JFrame frame;
	
    Platform platform = new Platform();
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		Address addr1 = new Address("Ukraine", "Kherson", 18);
		Address addr2 = new Address("Ukraine", "Kherson", 2);
	    Address addr3 = new Address("Ukraine", "Kherson", 18);
	    Smartcontract smartcontract_1 = new Smartcontract(500, 4, 10);
	    Smartcontract smartcontract_2 = new Smartcontract(500, 3, 5);
	    Teacher h1 = new Teacher("Natalia", "Vinnikova", 34, "natvin@gmail.com", addr1, "Natalia", "12345", 5, 1000, smartcontract_1, 10);
	    Teacher h2 = new Teacher("Kristina", "Nevada", 34, "kristina@gmail.com", addr1, "Kristina", "12345", 1, 1000, smartcontract_2, 20);
	    Student s1 = new Student("Lisa", "Laferova", 17, "lislaf@gmail.com", addr2, "Lisa", "123", 3, 1000, smartcontract_1);
	    Student s2 = new Student("Marta", "Zarova", 18, "marta@gmail.com", addr3, "Marta", "456", 2, 1000, smartcontract_1);
	    Student s3 = new Student("Viktor", "Tritakov", 18, "viktor@gmail.com", addr3, "Viktor", "456", 0, 1000, smartcontract_1);
	    Student s4 = new Student("Lisa", "Krasnova", 18, "lisa@gmail.com", addr3, "Lisa", "456", 0, 1000, smartcontract_1);
	    Student s5 = new Student("Nikita", "Vasilkov", 18, "nikita@gmail.com", addr3, "Nikita", "456", 0, 1000, smartcontract_2);
	    Student s6 = new Student("Daria", "Novikova", 18, "daria@gmail.com", addr3, "Daria", "456", 0, 1000, smartcontract_2);
	    Student s7 = new Student("Simon", "Listrov", 18, "simon@gmail.com", addr3, "Simon", "456", 0, 1000, smartcontract_2);
	    Student s8 = new Student("Veronika", "Krivzova", 18, "veronika@gmail.com", addr3, "Veronika", "456", 0, 1000, smartcontract_2);
	    Student s9 = new Student("Mikhail", "Kotov", 18, "mikhail@gmail.com", addr3, "Mikhail", "456", 0, 1000, smartcontract_2);
	    Student s10 = new Student("Rita", "Naumova", 18, "rita@gmail.com", addr3, "Rita", "456", 0, 1000, smartcontract_2);
	    Birga birga = new Birga();
	    Platform platform = new Platform();
	    h1.addinlist(s1);
	    h1.addinlist(s2);
	    h1.addinlist(s3);
	    h1.addinlist(s4);
	    h2.addinlist(s5);
	    h2.addinlist(s6);
	    h2.addinlist(s7);
	    //h2.addinlist(s8);
	    //h2.addinlist(s9);
	    //h2.addinlist(s10);
	    System.out.println(h1.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(h1.showlist());
	    System.out.println("-----------------------------------------------------------------------");
	    /*System.out.println(h2.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(h2.showlist());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(birga.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(platform.Show());
	    System.out.println("-----------------------------------------------------------------------");*/
	    h1.evaluation(birga, platform);
	    System.out.println(h1.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(h1.showlist());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(birga.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(platform.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    
	    //ListBuffer list = (ListBuffer) h1.evaluation(birga, platform);
	    ListBuffer list = (ListBuffer) h1.evaluation(birga, platform);
	    
	    //list.append((ListBuffer)h2.evaluation(birga, platform));
	    
	    /*System.out.println("-----------------------------------------------------------------------");
	    System.out.println(h2.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(h2.showlist());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(birga.Show());
	    System.out.println("-----------------------------------------------------------------------");
	    System.out.println(platform.Show());
	    System.out.println("-----------------------------------------------------------------------");*/
	    
	      /*if(h1.Smartcontract().Period() > h2.Smartcontract().Period()) {*/
	    	  LineChartEx ex = new LineChartEx(list.toList(), h1.Smartcontract().Period());
	          ex.setVisible(true);
	      /*}
	      else {
		 LineChartEx ex = new LineChartEx(list.toList(),h2.Smartcontract().Period());
         ex.setVisible(true);}*/
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Address addr1 = new Address("Ukraine", "Kherson", 18);
		Address addr2 = new Address("Ukraine", "Kherson", 2);
	    Address addr3 = new Address("Ukraine", "Kherson", 18);
	    Smartcontract smartcontract_1 = new Smartcontract(500, 6, 10);
	    Smartcontract smartcontract_2 = new Smartcontract(500, 6, 5);
	    Teacher h1 = new Teacher("Natalia", "Vinnikova", 34, "natvin@gmail.com", addr1, "Natalia", "12345", 0, 1000, smartcontract_1, 15);
	    Teacher h2 = new Teacher("Kristina", "Nevada", 34, "kristina@gmail.com", addr1, "Kristina", "12345", 0, 1000, smartcontract_2, 20);
	    Student s1 = new Student("Lisa", "Laferova", 17, "lislaf@gmail.com", addr2, "Lisa", "123", 0, 1000, smartcontract_1);
	    Student s2 = new Student("Marta", "Zarova", 18, "marta@gmail.com", addr3, "Marta", "456", 0, 1000, smartcontract_1);
	    Student s3 = new Student("Viktor", "Tritakov", 18, "viktor@gmail.com", addr3, "Viktor", "456", 0, 1000, smartcontract_1);
	    Student s4 = new Student("Lisa", "Krasnova", 18, "lisa@gmail.com", addr3, "Lisa", "456", 0, 1000, smartcontract_1);
	    Student s5 = new Student("Nikita", "Vasilkov", 18, "nikita@gmail.com", addr3, "Nikita", "456", 0, 1000, smartcontract_2);
	    Student s6 = new Student("Daria", "Novikova", 18, "daria@gmail.com", addr3, "Daria", "456", 0, 1000, smartcontract_2);
	    Student s7 = new Student("Simon", "Listrov", 18, "simon@gmail.com", addr3, "Simon", "456", 0, 1000, smartcontract_2);
	    Student s8 = new Student("Veronika", "Krivzova", 18, "veronika@gmail.com", addr3, "Veronika", "456", 0, 1000, smartcontract_2);
	    Student s9 = new Student("Mikhail", "Kotov", 18, "mikhail@gmail.com", addr3, "Mikhail", "456", 0, 1000, smartcontract_2);
	    Student s10 = new Student("Rita", "Naumova", 18, "rita@gmail.com", addr3, "Rita", "456", 0, 1000, smartcontract_2);
	    Birga birga = new Birga();
	    Platform platform = new Platform();
	    h1.addinlist(s1);
	    h1.addinlist(s2);
	    h1.addinlist(s3);
	    //h1.addinlist(s4);
	    h2.addinlist(s5);
	    h2.addinlist(s6);
	    h2.addinlist(s7);
	    //h2.addinlist(s8);
	    //h2.addinlist(s9);
		/*frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/}

}
