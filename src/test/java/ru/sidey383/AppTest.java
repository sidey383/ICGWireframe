package ru.sidey383;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.sidey383.model.math.PerspectiveProjectionMatrix;
import ru.sidey383.model.math.Vector;
import ru.sidey383.model.math.VectorRecord;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{

    public AppTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        PerspectiveProjectionMatrix m = new PerspectiveProjectionMatrix(Math.PI / 2, 1, 5, 20);
        Vector v = m.multiply(new VectorRecord(1, 1, 20, 1));
        System.out.println(v.get(0) / v.get(3) + " " + v.get(1) / v.get(3) + " " + v.get(2) / v.get(3));
    }
}
