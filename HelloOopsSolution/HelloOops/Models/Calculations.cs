
namespace HelloOops.API;

public class Calculations
{
    public int a;
    public int b;

    public int result;

    public Calculations(int x)
    {
        this.a = x;
        this.b = x;
        this.result = x + x;
    }

    public int Plus()
    {
        return a + b;
    }
}