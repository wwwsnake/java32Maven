import junit.framework.TestCase;
import org.junit.*;

public class DeterminantTest {

    @Before         //отрабатывает перед @Test
    @After          //удаление временных данных после теста
    @BeforeClass    //отрабатывает перед @Before
    @AfterClass     //

    @Test //(timeout = 12) - должен отработать за 12 милисекунд (excepted = NullPointerException.class) - должно выдать исключение
    public void determinant1(){
        double [][] nums = {{2,3,4},{5,6,7},{2,4,6}};
    Matrix matrix = new Matrix(nums);

    double resultFact = matrix.determinant(); //фактический результат
    double resultWait = 0.0; //ожидаемый результат

        TestCase.assertEquals(resultFact, resultWait); //сравнение результатов
    }
}
