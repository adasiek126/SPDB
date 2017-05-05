package pl.edu.pw.elka.spdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.pw.elka.spdb.population.IDataPopulator;

public class DataPopulatorEntryPoint {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/context.xml");
        IDataPopulator populator = context.getBean(IDataPopulator.class);

        populator.populate();
    }
}
