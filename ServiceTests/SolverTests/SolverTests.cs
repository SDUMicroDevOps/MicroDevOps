namespace ServiceTests;

[TestClass]
public class SolverTests
{
    [TestMethod]
    public void HealthCheck_Works()
    {
        //Arrange
        var client = new HttpClient();

        //Act
        await var response = client.GetAsync();

        //Assert
    }
}